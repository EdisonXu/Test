package com.edi.test;

import java.util.List;

import com.edi.test.ifc.BmscEventHttpSenderRemote;

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
    
    private void handleEvent(NbiEventCache.STATUS status, BmscEventRetryTO event)
    {
        switch(status)
        {
        case OVERLOAD:
            event = NbiEventCache.poll(event);
            //System.out.println(Thread.currentThread().getName() + " discard event " + event + " queue size" + NbiEventCache.size());
            handleOverload(event);
            break;
        case OK:
        	event = NbiEventCache.poll(event);
            handleOK(event);
            break;
        case FULL:
            System.out.println("event queue is full, will clear all data!");
            removeFromDb(NbiEventCache.clear());
            return;
        default:
            // should never come here
        	System.out.println("Wrong status of NBI event queue.");
        }
    }
    
    private void send()
    {
    	handleEvent(NbiEventCache.getStatus(),null);
    }
    
    private void handleNext(BmscEventRetryTO old) {
    	/*
    	 * If the old event don't have a eMBMS session id, it will not have a queue in the 
    	 * 2nd level filtered cache, so return directly.
    	 */
    	if(old==null || old.getEMBMSSessionId()==null) return;
    	handleEvent(NbiEventCache.getStatus(), old);
    }
    
    private void handleOK(BmscEventRetryTO event)
    {
        if(event==null) return;
        sendEvent(event);
    }
    
    private void handleOverload(BmscEventRetryTO event) {
        if(event==null) return;
        
        //System.out.println("NBI event queue is OVERLOADED, will discard low-priority event!");
        
        if(event.getNotificationType().getPriority()==0)
        {
            /*
             * If it's low priority event, discard directly, and try to get next event in the 2nd level filtered
             * cache to handle.
             */
/*            System.out.println(Thread.currentThread().getName() +
                    " Discard NBI event '" + event.toString() + "', current queue size: " 
                            + NbiEventCache.size());
*/            System.out.println(Thread.currentThread().getName() +
                    " Discard NBI event ' Id=" + event.getId() + ", eMBMSSessionId=" + event.getEMBMSSessionId() + "', current queue size: " 
                            + NbiEventCache.size());

            removeFromDb(event);
            handleNext(event);
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
        /*event = NbiEventCache.removeAndGetNext(event);
        if(event!=null)
            handleEvent(event);*/
        handleNext(event);
    }
    
    private void doSend(BmscEventRetryTO event){
        // send event
        BmscEventHttpSenderRemote bmscEventSender = NbiEventCacheUtil.getSender();
        if(bmscEventSender == null)
        {
        	System.out.println("Cannot get a instance of BmscEventHttpSender when trying to send BDC event.");
            return;
        }
        int retrytimes = event.getRetryTimes();
        while (true) {
            boolean isSendOK = bmscEventSender.send(event);

            if (isSendOK) {
            	/*System.out.println(Thread.currentThread().getName()+
                        " Successful to send NBI event '" + event.toString() + "', current queue size: " 
                                + NbiEventCache.size());*/
                break;
            }

            if (retrytimes >= configuredRetryTime) {
                System.out.println("Discard BDC event " + event.getNotificationType()
                        + " due to exceeding configured max retry time " + configuredRetryTime);
                break;
            }

            if (retrytimes > 0) {
                event.setRetryTimes(retrytimes);
                NbiEventCacheUtil.updateBmscEventInDB(event);
            }

            retrytimes++;
            System.out.println(Thread.currentThread().getName()+ 
                    "Wait for re-sending the event '" + event.toString() + "' with retry time " + retrytimes);
            waitForRetry();
        }
        removeFromDb(event);
    }
    
    private void waitForRetry() {
        try {
            Thread.sleep(retryInterval * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        	send();
        } catch (Exception e) {
            e.printStackTrace();
        } finally
        {
            NbiEventCache.releaseLock();
        }
    }
}
