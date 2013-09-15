package com.edi.test;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.edi.test.ifc.BccConfigManagerRemote;
import com.edi.test.ifc.BmscEventHttpSenderRemote;
import com.edi.test.ifc.BmscEventSenderRemote;

public class NbiEventCacheUtil {

    public static final int SENDER_POOL_SIZE = 20;
    public static final int MAX_RETRY_TIMES = 3;
    public static final int RETRY_INTERVAL_IN_SEC = 12;
    
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
            e.printStackTrace();
            return null;
        }
    }
    
    public static BmscEventHttpSenderRemote getSender() {
        try {
            return (BmscEventHttpSenderRemote) getContext()
                    .lookup(BmscEventHttpSenderRemote.GLOBAL_PORTABLE_NAME);
        } catch (NamingException e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    public static BccConfigManagerRemote getBccConfig() {
        try {
            return (BccConfigManagerRemote) getContext()
                    .lookup(BccConfigManagerRemote.GLOBAL_JNDI_NAME);
        } catch (NamingException e) {
        	e.printStackTrace();
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
        } catch (Exception e) {
        	e.printStackTrace();
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
                System.out.println("Cannot remove BDC event by null ID");
                return;
            }
            getSenderLogic().removeBmscEvent(bmscEventId);
        } catch (Exception e) {
        	e.printStackTrace();
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
