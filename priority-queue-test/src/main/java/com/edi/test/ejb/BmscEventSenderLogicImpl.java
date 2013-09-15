package com.edi.test.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.edi.test.BmscEventRetryTO;
import com.edi.test.BmscEventTO;
import com.edi.test.NbiEventCacheUtil;
import com.edi.test.ifc.BmscEventDbManager;
import com.edi.test.ifc.BmscEventSender;
import com.edi.test.ifc.BmscEventSenderRemote;

@Stateless(mappedName = BmscEventSenderRemote.JNDI_NAME)
@Local(BmscEventSender.class)
@Remote(BmscEventSenderRemote.class)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class BmscEventSenderLogicImpl implements BmscEventSender {

    @EJB
    private BmscEventDbManager bmscEventDbManager = null;
    
    @Override
    public BmscEventRetryTO createBmscEvent(BmscEventRetryTO bmscEventRetryTO) throws Exception {
        return bmscEventDbManager.createBmscEvent(bmscEventRetryTO);
    }

    @Override
    public BmscEventRetryTO updateBmscEvent(BmscEventRetryTO bmscEventRetryTO) throws Exception {
        return bmscEventDbManager.updateBmscEvent(bmscEventRetryTO);
    }

    @Override
    public BmscEventRetryTO removeBmscEvent(Long id) throws Exception {
        return bmscEventDbManager.removeBmscEvent(id);
    }

    @Override
    public List<BmscEventRetryTO> findEvents() throws Exception {
        List<BmscEventRetryTO> foundBmscEvents = bmscEventDbManager.findEvents();
        if (foundBmscEvents == null) {
            foundBmscEvents = new ArrayList<BmscEventRetryTO>();
        }
        return foundBmscEvents;
    }

    @Override
    public void send(BmscEventTO event) {

        if (event == null) {
            System.out.println("Event object can not be null.");
            return;
        }
        BmscEventRetryTO bmscEventRetryTO = new BmscEventRetryTO(event);
        bmscEventRetryTO.setRetryTimes(0);
        bmscEventRetryTO.setRetryStartTime(new Date(System.currentTimeMillis()));
        bmscEventRetryTO.setRetryUpdateTime(new Date(System.currentTimeMillis()));

        BmscEventRetryTO bmscEvent = null;
        try {
            bmscEvent = createBmscEvent(bmscEventRetryTO);
            if (bmscEvent == null) {
                return;
            }
            bmscEvent.getLock().lock();
            NbiEventCacheUtil.addEventToCache(bmscEvent);
            bmscEvent.getLock().unlock();
        } catch (Exception e1) {

            e1.printStackTrace();
            return;
        }

    }
}
