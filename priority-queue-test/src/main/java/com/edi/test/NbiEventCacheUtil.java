package com.ericsson.ecds.bcc.prov.events;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ericsson.bmsc.common.constant.ErrorCode;
import com.ericsson.bmsc.oam.config.ifc.BccConfigManagerRemote;
import com.ericsson.bmsc.oam.logging.BmscLogger;
import com.ericsson.ecds.bcc.prov.common.data.BmscEventRetryTO;
import com.ericsson.ecds.bcc.prov.common.ejb.BmscEventHttpSender;
import com.ericsson.ecds.bcc.prov.common.ejb.BmscEventHttpSenderRemote;
import com.ericsson.ecds.bcc.prov.common.ejb.BmscEventSenderRemote;
import com.ericsson.ecds.bcc.prov.common.exception.ProvisioningException;

public class NbiEventCacheUtil {

    public static final int SENDER_POOL_SIZE = 20;
    
    private static ExecutorService servicePool = Executors.newFixedThreadPool(SENDER_POOL_SIZE);
    private static Context context = null;
    private static BccConfigManagerRemote bccConfig = null;
    
    public static void addEventToCache(BmscEventRetryTO event) {
        NbiEventCache.offer(event);
        if(bccConfig==null)
        {
            synchronized (NbiEventCacheUtil.class) {
                if(bccConfig==null){
                    bccConfig = getBccConfig();
                }
            }
        }
        
        servicePool.submit(new NbiEventSendTask(bccConfig.getRetryConntectionTimes(), bccConfig.getRetryIntervalTime()));
    }

    public static BmscEventSenderRemote getSenderLogic() {
        try {
            return (BmscEventSenderRemote) getContext()
                    .lookup(BmscEventSenderRemote.GLOBAL_JNDI_NAME);
        } catch (NamingException e) {
            BmscLogger.eventError(BmscLogger.PROVISIONING,
                    ErrorCode.JNDI_LOOKUP_FAILED, BmscEventSenderRemote.GLOBAL_JNDI_NAME,
                    e.getMessage());
            BmscLogger.eventDebug(BmscLogger.PROVISIONING,
                    "NbiEventCacheUtil:getSenderLogic", e);
            return null;
        }
    }
    
    public static BmscEventHttpSenderRemote getSender() {
        try {
            return (BmscEventHttpSenderRemote) getContext()
                    .lookup(BmscEventHttpSenderRemote.GLOBAL_PORTABLE_NAME);
        } catch (NamingException e) {
            BmscLogger.eventError(BmscLogger.PROVISIONING,
                    ErrorCode.JNDI_LOOKUP_FAILED, BmscEventHttpSender.GLOBAL_PORTABLE_NAME,
                    e.getMessage());
            BmscLogger.eventDebug(BmscLogger.PROVISIONING,
                    "NbiEventCacheUtil:getSender", e);
            return null;
        }
    }
    
    public static BccConfigManagerRemote getBccConfig() {
        try {
            return (BccConfigManagerRemote) getContext()
                    .lookup(BccConfigManagerRemote.GLOBAL_JNDI_NAME);
        } catch (NamingException e) {
            BmscLogger.eventError(BmscLogger.PROVISIONING,
                    ErrorCode.JNDI_LOOKUP_FAILED, BccConfigManagerRemote.GLOBAL_JNDI_NAME,
                    e.getMessage());
            BmscLogger.eventDebug(BmscLogger.PROVISIONING,
                    "NbiEventCacheUtil:getBccConfig", e);
            return null;
        }
    }
    
    public static void updateBmscEventInDB(BmscEventRetryTO bmscEvent) {
        try {
            bmscEvent.getLock().lock();
            if (bmscEvent.getRetryStartTime() == null) {
                bmscEvent.setRetryStartTime(new Date(System.currentTimeMillis()));
            }
            bmscEvent.setRetryUpdateTime(new Date(System.currentTimeMillis()));
            getSenderLogic().updateBmscEvent(bmscEvent);
        } catch (ProvisioningException e) {
            BmscLogger.eventWarn(BmscLogger.PROVISIONING, "Failed to update retry time for BDC event " + bmscEvent.getId()
                    + ", type is " + bmscEvent.getNotificationType());
            BmscLogger.eventDebug(BmscLogger.PROVISIONING, "NbiEventCacheUtil:handleSendEventFailed", e);
        } finally
        {
            bmscEvent.getLock().unlock();
        }
    }
    
    public static void removeBmscEventInDB(BmscEventRetryTO bmscEvent) {
        
        Long bmscEventId = bmscEvent.getId();
        try {
            bmscEvent.getLock().lock();
            if (bmscEventId == null) {
                BmscLogger.eventWarn(BmscLogger.PROVISIONING, "Cannot remove BDC event by null ID");
                return;
            }
            getSenderLogic().removeBmscEvent(bmscEventId);
        } catch (ProvisioningException e) {
            BmscLogger.eventWarn(BmscLogger.PROVISIONING, "Failed to remove BDC event " + bmscEventId + ", reason: " + e.getMessage());
            BmscLogger.eventDebug(BmscLogger.PROVISIONING, "NbiEventCacheUtil:removeBmscEvent", e);
        }finally
        {
            bmscEvent.getLock().unlock();
        }
    }
    
    private static Context getContext() throws NamingException
    {
        if(context == null)
        {
            synchronized (NbiEventCacheUtil.class) {
                if(context == null){
                    context = new InitialContext();
                }
            }
        }
        return context;
    }
}
