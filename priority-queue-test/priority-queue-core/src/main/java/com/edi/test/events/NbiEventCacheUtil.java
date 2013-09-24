package com.edi.test.events;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.edi.test.bean.MyEventRetryTO;
import com.edi.test.ifc.MyEventHttpSender;
import com.edi.test.ifc.MyConfig;
import com.edi.test.ifc.MyEventSenderLogic;

public class NbiEventCacheUtil {

    public static final int SENDER_POOL_SIZE = 20;
    public static final int MAX_RETRY_TIMES = 3;
    public static final int RETRY_INTERVAL_IN_SEC = 12;
    
    private static ExecutorService servicePool = Executors.newFixedThreadPool(SENDER_POOL_SIZE);
    private static Context context = null;
    
    
    public static void addEventToCache(MyEventRetryTO event) {
        NbiEventCache.offer(event);
        servicePool.submit(new NbiEventSendTask(MyConfig.RETRY_CONNECTION_TIMES, MyConfig.RETRY_INTERVAL_TIME_IN_SEC));
    }

    public static MyEventSenderLogic getSenderLogic() {
        try {
            return (MyEventSenderLogic) getContext()
                    .lookup(MyEventSenderLogic.GLOBAL_JNDI_NAME);
        } catch (NamingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static MyEventHttpSender getSender() {
        try {
            return (MyEventHttpSender) getContext()
                    .lookup(MyEventHttpSender.GLOBAL_PORTABLE_NAME);
        } catch (NamingException e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    public static void updateMyEventInDB(MyEventRetryTO bmscEvent) {
        try {
            bmscEvent.getLock().lock();
            if (bmscEvent.getRetryStartTime() == null) {
                bmscEvent.setRetryStartTime(new Date(System.currentTimeMillis()));
            }
            bmscEvent.setRetryUpdateTime(new Date(System.currentTimeMillis()));
            getSenderLogic().updateMyEvent(bmscEvent);
        } catch (Exception e) {
        	e.printStackTrace();
        } finally
        {
            bmscEvent.getLock().unlock();
        }
    }
    
    public static void removeMyEventInDB(MyEventRetryTO bmscEvent) {
        
        Long bmscEventId = bmscEvent.getId();
        try {
            bmscEvent.getLock().lock();
            if (bmscEventId == null) {
                System.out.println("Cannot remove BDC event by null ID");
                return;
            }
            getSenderLogic().removeMyEvent(bmscEventId);
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
