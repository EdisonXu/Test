package com.ericsson.ecds.bcc.prov.bl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.ericsson.bmsc.common.constant.ErrorCode;
import com.ericsson.bmsc.oam.logging.BmscLogger;
import com.ericsson.ecds.bcc.prov.common.data.BmscEventRetryTO;
import com.ericsson.ecds.bcc.prov.common.data.BmscEventTO;
import com.ericsson.ecds.bcc.prov.common.dbmgr.BmscEventDbManager;
import com.ericsson.ecds.bcc.prov.common.ejb.BmscEventSender;
import com.ericsson.ecds.bcc.prov.common.ejb.BmscEventSenderRemote;
import com.ericsson.ecds.bcc.prov.common.exception.ProvisioningException;
import com.ericsson.ecds.bcc.prov.events.NbiEventCacheUtil;

@Stateless(mappedName = BmscEventSenderRemote.JNDI_NAME)
@Local(BmscEventSender.class)
@Remote(BmscEventSenderRemote.class)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class BmscEventSenderLogicImpl implements BmscEventSender {

    @EJB
    private BmscEventDbManager bmscEventDbManager = null;
    
    @Override
    public BmscEventRetryTO createBmscEvent(BmscEventRetryTO bmscEventRetryTO) throws ProvisioningException {
        return bmscEventDbManager.createBmscEvent(bmscEventRetryTO);
    }

    @Override
    public BmscEventRetryTO updateBmscEvent(BmscEventRetryTO bmscEventRetryTO) throws ProvisioningException {
        return bmscEventDbManager.updateBmscEvent(bmscEventRetryTO);
    }

    @Override
    public BmscEventRetryTO removeBmscEvent(Long id) throws ProvisioningException {
        return bmscEventDbManager.removeBmscEvent(id);
    }

    @Override
    public List<BmscEventRetryTO> findEvents() throws ProvisioningException {
        List<BmscEventRetryTO> foundBmscEvents = bmscEventDbManager.findEvents();
        if (foundBmscEvents == null) {
            foundBmscEvents = new ArrayList<BmscEventRetryTO>();
        }
        return foundBmscEvents;
    }

    @Override
    public void send(BmscEventTO event) {

        if (event == null) {
            BmscLogger.eventWarn(BmscLogger.PROVISIONING, "Event object can not be null.");
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
                BmscLogger.eventError(BmscLogger.PROVISIONING, ErrorCode.PERSISTENCE_BMC_EVENT_FAILED, 
                        bmscEventRetryTO.getNotificationType().getDesciption());
                return;
            }
            bmscEvent.getLock().lock();
            NbiEventCacheUtil.addEventToCache(bmscEvent);
            bmscEvent.getLock().unlock();
        } catch (ProvisioningException e1) {

            BmscLogger.eventError(BmscLogger.PROVISIONING, ErrorCode.PERSISTENCE_BMC_EVENT_FAILED, bmscEventRetryTO.getNotificationType().getDesciption());
            BmscLogger.eventDebug(BmscLogger.PROVISIONING,
                    this.getClass().getName() + ":send", e1);
            return;
        }

    }
}
