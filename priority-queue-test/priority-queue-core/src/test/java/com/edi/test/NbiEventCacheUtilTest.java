package com.edi.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;

import javax.naming.Context;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.edi.test.bean.MyEventRetryTO;
import com.edi.test.events.NbiEventCacheUtil;
import com.edi.test.events.NbiEventSendTask;
import com.edi.test.ifc.MyConfig;
import com.edi.test.ifc.MyEventHttpSender;
import com.edi.test.ifc.MyEventSenderLogic;

public class NbiEventCacheUtilTest {

    private Mockery ctxt = null;
    private NbiEventCacheUtil util;
    private Context context;
    private MyConfig bccConfig;
    private MyEventSenderLogic sendLogic;
    private MyEventHttpSender sender;
    private ExecutorService servicePool;
    private int retryConntectionTimes = 10;
    private int retryIntervalTime = 100;
    @Before
    public void setup() throws Exception {
        ctxt = new Mockery();
        context = ctxt.mock(Context.class);
        sendLogic = ctxt.mock(MyEventSenderLogic.class);
        sender = ctxt.mock(MyEventHttpSender.class);
        bccConfig = ctxt.mock(MyConfig.class);
        servicePool = ctxt.mock(ExecutorService.class);
        util = new NbiEventCacheUtil();
        
        Field f1 = util.getClass().getDeclaredField("context");
        f1.setAccessible(true);
        f1.set(util, context);
        
        Field f2 = util.getClass().getDeclaredField("servicePool");
        f2.setAccessible(true);
        f2.set(util, servicePool);
        
        
        ctxt.checking(new Expectations(){
            {
                allowing(context).lookup(MyConfig.GLOBAL_JNDI_NAME);
                will(returnValue(bccConfig));
                allowing(context).lookup(MyEventSenderLogic.GLOBAL_JNDI_NAME);
                will(returnValue(sendLogic));
                allowing(context).lookup(MyEventHttpSender.GLOBAL_PORTABLE_NAME);
                will(returnValue(sender));
                
                allowing(bccConfig).getRetryConntectionTimes();
                will(returnValue(retryConntectionTimes));
                allowing(bccConfig).getRetryIntervalTime();
                will(returnValue(retryIntervalTime));
                
                allowing(servicePool).submit(with(any(NbiEventSendTask.class)));
                
            }
        });
    }
    
    @Test
    public void testAddEventToCache() {
        
        try {
            NbiEventCacheUtil.addEventToCache(new MyEventRetryTO());
            assertTrue(true);
        } catch (Exception e) {
            assertFalse(true);
        }
    }

    @Test
    public void testGetSenderLogic() {
        Assert.assertNotNull(NbiEventCacheUtil.getSenderLogic());
    }

    @Test
    public void testGetSender() {
        Assert.assertNotNull(NbiEventCacheUtil.getSender());
    }

    @Test
    public void testUpdateMyEventInDB() {
        try {
            ctxt.checking(new Expectations(){
                {
                    allowing(sendLogic).updateMyEvent(with(any(MyEventRetryTO.class)));
                }
            });
            NbiEventCacheUtil.updateMyEventInDB(new MyEventRetryTO());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
        
    }
    
    @Test
    public void testUpdateMyEventInDBNegative() {
        try {
            ctxt.checking(new Expectations(){
                {
                    allowing(sendLogic).updateMyEvent(with(any(MyEventRetryTO.class)));
                    will(throwException(new Exception("UNKNOWN")));
                }
            });
            NbiEventCacheUtil.updateMyEventInDB(new MyEventRetryTO());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
        
    }

    @Test
    public void testRemoveMyEventInDB() {
        try {
            ctxt.checking(new Expectations(){
                {
                    allowing(sendLogic).removeMyEvent(with(any(Long.class)));
                }
            });
            NbiEventCacheUtil.removeMyEventInDB(new MyEventRetryTO());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    public void testRemoveMyEventInDBNegative() {
        try {
            ctxt.checking(new Expectations(){
                {
                    allowing(sendLogic).removeMyEvent(with(any(Long.class)));
                    will(throwException(new Exception()));
                }
            });
            NbiEventCacheUtil.removeMyEventInDB(new MyEventRetryTO());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }
}
