package com.edi.test.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edi.test.bean.MyEventRetryTO;
import com.edi.test.bean.MyEventTO;
import com.edi.test.events.NbiEventCacheUtil;
import com.edi.test.ifc.MyEventDbManager;
import com.edi.test.ifc.MyEventSenderLogic;

@Stateless()
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class MyEventSenderLogicImpl implements MyEventSenderLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyEventSenderLogicImpl.class);
    
    @EJB
    private MyEventDbManager myEventDbManager = null;
    
    @Override
    public MyEventRetryTO createMyEvent(MyEventRetryTO myEventRetryTO) throws Exception {
        return myEventDbManager.persistMyEvent(myEventRetryTO);
    }

    @Override
    public MyEventRetryTO updateMyEvent(MyEventRetryTO myEventRetryTO) throws Exception {
        return myEventDbManager.updateMyEvent(myEventRetryTO);
    }

    @Override
    public MyEventRetryTO removeMyEvent(Long id) throws Exception {
        return myEventDbManager.removeMyEvent(id);
    }

    @Override
    public List<MyEventRetryTO> findEvents() throws Exception {
        List<MyEventRetryTO> foundMyEvents = myEventDbManager.findEvents();
        if (foundMyEvents == null) {
            foundMyEvents = new ArrayList<MyEventRetryTO>();
        }
        return foundMyEvents;
    }

    @Override
    public void send(MyEventTO event) {

        if (event == null) {
            LOGGER.error("Event object can not be null.");
            return;
        }
        MyEventRetryTO myEventRetryTO = new MyEventRetryTO(event);
        myEventRetryTO.setRetryTimes(0);
        myEventRetryTO.setRetryStartTime(new Date(System.currentTimeMillis()));
        myEventRetryTO.setRetryUpdateTime(new Date(System.currentTimeMillis()));

        MyEventRetryTO myEvent = null;
        try {
            myEvent = createMyEvent(myEventRetryTO);
            if (myEvent == null) {
                return;
            }
            myEvent.getLock().lock();
            NbiEventCacheUtil.addEventToCache(myEvent);
            myEvent.getLock().unlock();
        } catch (Exception e1) {
            LOGGER.error("", e1);
            return;
        }

    }
}
