package com.ericsson.ecds.bcc.prov.events;

import java.util.List;

import com.ericsson.bmsc.common.constant.ErrorCode;
import com.ericsson.bmsc.common.oam.OamConstants;
import com.ericsson.bmsc.common.oam.event.EventHandlerRemote;
import com.ericsson.bmsc.oam.logging.BccAlarmHelper;
import com.ericsson.bmsc.oam.logging.BmscLogger;
import com.ericsson.ecds.bcc.prov.common.data.BmscEventRetryTO;
import com.ericsson.ecds.bcc.prov.common.ejb.BmscEventHttpSenderRemote;

/**
 * A stand alone thread task to send NBI event to BMC.
 *
 */
public class NbiEventSendTask implements Runnable{
    
    private int configuredRetryTime = -1;
    private int retryInterval = 0;
    
    public NbiEventSendTask() {
        super();
    }
    
    public NbiEventSendTask(int configuredRetryTime, int retryInterval) {
        super();
        this.configuredRetryTime = configuredRetryTime;
        this.retryInterval = retryInterval;
    }
    
    private void handleEvent(BmscEventRetryTO event)
    {
        
        switch(NbiEventCache.getStatus())
        {
        case OVERLOAD:
            if(event==null)
            {
                event = NbiEventCache.poll();
            }
            System.out.println(Thread.currentThread().getName() + " discard event " + event + " queue size" + NbiEventCache.size());
            handleOverload(event);
            break;
        case OK:
            if(event==null)
                event = NbiEventCache.poll();
            handleOK(event);
            break;
        case FULL:
            BmscLogger.eventWarn(BmscLogger.PROVISIONING, "NBI event queue is full, will clear all data!");
            removeFromDb(NbiEventCache.clear());
            return;
        default:
            // should never come here
            BmscLogger.eventError(BmscLogger.PROVISIONING, "Wrong status of NBI event queue.");
        }
    }
    
    private void handleOK(BmscEventRetryTO event)
    {
        if(event==null) return;
        sendEvent(event);
    }
    
    private void handleOverload(BmscEventRetryTO event) {
        if(event==null) return;
        
        BmscLogger.eventWarn(BmscLogger.PROVISIONING, 
                "NBI event queue is OVERLOADED, will discard low-priority event!");
        
        if(event.getNotificationType().getPriority()==0)
        {
            /*
             * If it's low priority event, discard directly, and try to get next event in the 2nd level filtered
             * cache to handle.
             */
            BmscLogger.eventDebug(BmscLogger.PROVISIONING, Thread.currentThread().getName(),
                    "Discard NBI event '" + event.toString() + "', current queue size: " 
                            + NbiEventCache.size());
            BmscLogger.eventWarn(BmscLogger.PROVISIONING, Thread.currentThread().getName() +
                    " Discard NBI event '" + event.toString() + "', current queue size: " 
                            + NbiEventCache.size());
            removeFromDb(event);
            event = NbiEventCache.removeAndGetNext(event);
            if(event!=null)
                handleEvent(event);
        }else
        {
            /*
             * If it's high priority event, send it.
             */
            sendEvent(event);
        }
    }
    
    private void sendEvent(BmscEventRetryTO event) {
        doSend(event);
        // check whether if there's a event with a same eMBMS session id
        // if yes, peek it and continue to send in current thread
        event = NbiEventCache.removeAndGetNext(event);
        if(event!=null)
            handleEvent(event);
    }
    
    private void doSend(BmscEventRetryTO event){
        // send event
        BmscEventHttpSenderRemote bmscEventSender = NbiEventCacheUtil.getSender();
        if(bmscEventSender == null)
        {
            BmscLogger.eventError(BmscLogger.PROVISIONING, 
                    "Cannot get a instance of BmscEventHttpSender when trying to send BDC event.");
            return;
        }
        int retrytimes = event.getRetryTimes();
        while (true) {
            boolean isSendOK = bmscEventSender.send(event);

            if (isSendOK) {
                BmscLogger.eventDebug(BmscLogger.PROVISIONING, Thread.currentThread().getName(),
                        "Successful to send NBI event '" + event.toString() + "', current queue size: " 
                                + NbiEventCache.size());
                break;
            }

            if (retrytimes >= configuredRetryTime) {
                BmscLogger.eventWarn(BmscLogger.PROVISIONING, "Discard BDC event " + event.getNotificationType()
                        + " due to exceeding configured max retry time " + configuredRetryTime);
                EventHandlerRemote eventHandler = BccAlarmHelper.getEventHandler();
                if (eventHandler != null) {
                    eventHandler.raiseAlarm(OamConstants.ALARM_mID_MDFCP, OamConstants.ALARM_ec_MDFCP2EMBM,
                            OamConstants.ALARM_rsrcID_MDFCP,
                            "The MDF-CP failed to send event notification message to the EMBM after retry " + configuredRetryTime + " times");
                }
                break;
            }

            if (retrytimes > 0) {
                event.setRetryTimes(retrytimes);
                NbiEventCacheUtil.updateBmscEventInDB(event);
            }

            retrytimes++;
            BmscLogger.eventDebug(BmscLogger.PROVISIONING, Thread.currentThread().getName(), 
                    "Wait for re-sending the event '" + event.toString() + "' with retry time " + retrytimes );           
            waitForRetry();
        }
        removeFromDb(event);
    }
    
    private void waitForRetry() {
        try {
            Thread.sleep(retryInterval * 1000);
        } catch (InterruptedException e) {
            BmscLogger.eventWarn(BmscLogger.PROVISIONING, "Interrupted when waiting for re-send BDC event to BMC");
        }
    }
    
    private void removeFromDb(BmscEventRetryTO event) {
        NbiEventCacheUtil.removeBmscEventInDB(event);
    }
    
    private void removeFromDb(List<BmscEventRetryTO> list) {
        for(BmscEventRetryTO e:list)
            removeFromDb(e);
    }

    @Override
    public void run() {
        try {
            handleEvent(null);
        } catch (Exception e) {
            BmscLogger.eventError(BmscLogger.PROVISIONING, ErrorCode.SENDER_TASK_EXECUTE_FAILED, e);
            BmscLogger.eventDebug(BmscLogger.PROVISIONING, 
                    Thread.currentThread().getName() + ": NbiEventSendTask:run", e);
            e.printStackTrace();
        } finally
        {
            NbiEventCache.releaseLock();
        }
    }
}
