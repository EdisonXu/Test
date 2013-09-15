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

import com.edi.test.ifc.BccConfigManagerRemote;
import com.edi.test.ifc.BmscEventHttpSenderRemote;
import com.edi.test.ifc.BmscEventSenderRemote;

public class NbiEventCacheUtilTest {

    private Mockery ctxt = null;
    private NbiEventCacheUtil util;
    private Context context;
    private BccConfigManagerRemote bccConfig;
    private BmscEventSenderRemote sendLogic;
    private BmscEventHttpSenderRemote sender;
    private ExecutorService servicePool;
    private int retryConntectionTimes = 10;
    private int retryIntervalTime = 100;
    @Before
    public void setup() throws Exception {
        ctxt = new Mockery();
        context = ctxt.mock(Context.class);
        sendLogic = ctxt.mock(BmscEventSenderRemote.class);
        sender = ctxt.mock(BmscEventHttpSenderRemote.class);
        bccConfig = ctxt.mock(BccConfigManagerRemote.class);
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
                allowing(context).lookup(BccConfigManagerRemote.GLOBAL_JNDI_NAME);
                will(returnValue(bccConfig));
                allowing(context).lookup(BmscEventHttpSenderRemote.GLOBAL_PORTABLE_NAME);
                will(returnValue(sender));
                allowing(context).lookup(BmscEventSenderRemote.GLOBAL_JNDI_NAME);
                will(returnValue(sendLogic));
                
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
            NbiEventCacheUtil.addEventToCache(new BmscEventRetryTO());
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
    public void testGetBccConfig() {
        Assert.assertNotNull(NbiEventCacheUtil.getBccConfig());
    }

    @Test
    public void testUpdateBmscEventInDB() {
        try {
            ctxt.checking(new Expectations(){
                {
                    allowing(sendLogic).updateBmscEvent(with(any(BmscEventRetryTO.class)));
                }
            });
            NbiEventCacheUtil.updateBmscEventInDB(new BmscEventRetryTO());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
        
    }
    
    @Test
    public void testUpdateBmscEventInDBNegative() {
        try {
            ctxt.checking(new Expectations(){
                {
                    allowing(sendLogic).updateBmscEvent(with(any(BmscEventRetryTO.class)));
                    will(throwException(new Exception("UNKNOWN")));
                }
            });
            NbiEventCacheUtil.updateBmscEventInDB(new BmscEventRetryTO());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
        
    }

    @Test
    public void testRemoveBmscEventInDB() {
        try {
            ctxt.checking(new Expectations(){
                {
                    allowing(sendLogic).removeBmscEvent(with(any(Long.class)));
                }
            });
            NbiEventCacheUtil.removeBmscEventInDB(new BmscEventRetryTO());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }

    @Test
    public void testRemoveBmscEventInDBNegative() {
        try {
            ctxt.checking(new Expectations(){
                {
                    allowing(sendLogic).removeBmscEvent(with(any(Long.class)));
                    will(throwException(new Exception()));
                }
            });
            NbiEventCacheUtil.removeBmscEventInDB(new BmscEventRetryTO());
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertFalse(true);
        }
    }
}
