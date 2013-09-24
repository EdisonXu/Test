package com.edi.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.edi.test.bean.MyEventRetryTO;
import com.edi.test.bean.MyEventTO;
import com.edi.test.bean.NotificationTypeEnum;
import com.edi.test.events.NbiEventCache;
import com.edi.test.events.NbiEventCacheUtil;
import com.edi.test.events.NbiEventSendTask;
import com.edi.test.ifc.MyConfig;
import com.edi.test.ifc.MyEventHttpSender;
import com.edi.test.ifc.MyEventSenderLogic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ NbiEventCacheUtil.class, NbiEventSendTask.class })
public class NbiEventSendTaskTest {

    private MyEventSenderLogic sendLogic;
    private ExecutorService servicePool = Executors.newFixedThreadPool(NbiEventCacheUtil.SENDER_POOL_SIZE);
    private int retryConntectionTimes = 10;
    private int retryIntervalTime = 100;
    private static MyConfig bccConfig = null;
    public static Map<Long, ArrayList<MyEventTO>> eventMap = new HashMap<Long, ArrayList<MyEventTO>>();;
    public static boolean readyToSend = true;
    
    @Before
    public void setUp() throws Exception {
        sendLogic = Mockito.mock(MyEventSenderLogic.class);
        bccConfig = Mockito.mock(MyConfig.class);
        PowerMockito.when(sendLogic.updateMyEvent(Mockito.any(MyEventRetryTO.class))).thenReturn(null);
        PowerMockito.when(sendLogic.removeMyEvent(Mockito.anyLong())).thenReturn(null);
        PowerMockito.when(bccConfig.getRetryConntectionTimes()).thenReturn(retryConntectionTimes);
        PowerMockito.when(bccConfig.getRetryIntervalTime()).thenReturn(retryIntervalTime);
        PowerMockito.mockStatic(NbiEventCacheUtil.class);
        PowerMockito.when(NbiEventCacheUtil.getSenderLogic()).thenReturn(sendLogic);
        PowerMockito.doNothing().when(NbiEventCacheUtil.class, "updateMyEventInDB", Mockito.any(MyEventRetryTO.class));
    }
    
    /*
     * Test the situation that all events have a random eMBMS session id.
     */
    @Test
    public void testSendRadomEmbmsSessionEvents() throws InterruptedException {
        System.out.println("testSendRadomEmbmsSessionEvents begin.");
        MyEventHttpSender sender1 = new SendSimulater(0);
        MyEventHttpSender sender2 = new SendSimulater(0);
        MyEventHttpSender sender3 = new SendSimulater(0);
        MyEventHttpSender sender4 = new SendSimulater(0);
        MyEventHttpSender sender5 = new SendSimulater(0);
        PowerMockito.when(NbiEventCacheUtil.getSender()).thenReturn(sender1, sender2, sender3, sender4, sender5);
        
        reset();
        int numOfLowPEvent = 1000;
        int numOfHighPEvent = 1500;
        int numOfTotalEvent = numOfLowPEvent*2 + numOfHighPEvent;
        
        generateLowPriorityEvents(0, numOfLowPEvent, true);
        generateHighPriorityEvents(numOfLowPEvent, numOfHighPEvent, true);
        generateLowPriorityEvents(numOfLowPEvent+numOfHighPEvent, numOfLowPEvent, true);
        
        Assert.assertEquals(numOfTotalEvent, NbiEventCache.size());
        submitEvents();
        
        servicePool.shutdown();
        servicePool.awaitTermination(5, TimeUnit.MINUTES);
        Assert.assertEquals(numOfTotalEvent, getTotalRecEventNum());
        System.out.println("testSendRadomEmbmsSessionEvents end.");
    }
    
    /*
     * Test the situation that all events have a same eMBMS session id.
     */
    @Test
    public void testSendSameEmbmsSessionEvents() throws Exception {
        System.out.println("testSendSameEmbmsSessionEvents begin.");
        MyEventHttpSender sender1 = new SendSimulater(0);
        MyEventHttpSender sender2 = new SendSimulater(0);
        MyEventHttpSender sender3 = new SendSimulater(0);
        MyEventHttpSender sender4 = new SendSimulater(0);
        MyEventHttpSender sender5 = new SendSimulater(0);
        PowerMockito.when(NbiEventCacheUtil.getSender()).thenReturn(sender1, sender2, sender3, sender4, sender5);
        
        reset();
        int numOfLowPEvent = 500;
        for(int i=0;i< numOfLowPEvent;i++)
        {
            MyEventRetryTO lowPEvent = new MyEventRetryTO();
            lowPEvent.setSessionId(1L);
            lowPEvent.setId(Long.valueOf(i));
            lowPEvent.setRetryTimes(0);
            lowPEvent.setNotificationType(NotificationTypeEnum.DELIVERY_REMOVE_OK);
            NbiEventCache.offer(lowPEvent);
        }
        Assert.assertEquals(numOfLowPEvent, NbiEventCache.size());
        submitEvents();
        servicePool.shutdown();
        servicePool.awaitTermination(15, TimeUnit.MINUTES);
        Assert.assertEquals(numOfLowPEvent, getTotalRecEventNum());
        System.out.println("testSendSameEmbmsSessionEvents end.");
    }
    
    /*
     * Test the situation that all events have a unique eMBMS session id.
     */
    @Test
    public void testSendNoSameEmbmsSessionEvents() throws Exception {
        System.out.println("testSendNoSameEmbmsSessionEvents begin.");
        MyEventHttpSender sender1 = new SendSimulater(0);
        MyEventHttpSender sender2 = new SendSimulater(0);
        MyEventHttpSender sender3 = new SendSimulater(0);
        MyEventHttpSender sender4 = new SendSimulater(0);
        MyEventHttpSender sender5 = new SendSimulater(0);
        PowerMockito.when(NbiEventCacheUtil.getSender()).thenReturn(sender1, sender2, sender3, sender4, sender5);
        
        reset();
        int numOfLowPEvent = 500;
        for(int i=0;i< numOfLowPEvent;i++)
        {
            MyEventRetryTO lowPEvent = new MyEventRetryTO();
            lowPEvent.setSessionId(Long.valueOf(i));
            lowPEvent.setId(Long.valueOf(i));
            lowPEvent.setRetryTimes(0);
            lowPEvent.setNotificationType(NotificationTypeEnum.DELIVERY_REMOVE_OK);
            NbiEventCache.offer(lowPEvent);
        }
        Assert.assertEquals(numOfLowPEvent, NbiEventCache.size());
        submitEvents();
        servicePool.shutdown();
        servicePool.awaitTermination(15, TimeUnit.MINUTES);
        Assert.assertEquals(numOfLowPEvent, getTotalRecEventNum());
        System.out.println("testSendNoSameEmbmsSessionEvents end.");
    }
    
    /*
     * Test the situation that all events don't have a same eMBMS session id.
     */
    @Test
    public void testSendNoEmbmsSessionIdEvents() throws Exception {
        System.out.println("testSendNoEmbmsSessionIdEvents begin.");
        MyEventHttpSender sender1 = new SendSimulater(0);
        MyEventHttpSender sender2 = new SendSimulater(0);
        MyEventHttpSender sender3 = new SendSimulater(0);
        MyEventHttpSender sender4 = new SendSimulater(0);
        MyEventHttpSender sender5 = new SendSimulater(0);
        PowerMockito.when(NbiEventCacheUtil.getSender()).thenReturn(sender1, sender2, sender3, sender4, sender5);
        
        reset();
        int numOfLowPEvent = 500;
        for(int i=0;i< numOfLowPEvent;i++)
        {
            MyEventRetryTO lowPEvent = new MyEventRetryTO();
            lowPEvent.setId(Long.valueOf(i));
            lowPEvent.setRetryTimes(0);
            lowPEvent.setNotificationType(NotificationTypeEnum.DELIVERY_REMOVE_OK);
            NbiEventCache.offer(lowPEvent);
        }
        Assert.assertEquals(numOfLowPEvent, NbiEventCache.size());
        submitEvents();
        servicePool.shutdown();
        servicePool.awaitTermination(15, TimeUnit.MINUTES);
        Assert.assertEquals(numOfLowPEvent, getTotalRecEventNum());
        System.out.println("testSendNoEmbmsSessionIdEvents end.");
    }
    
    private void submitEvents()
    {
        int size = NbiEventCache.size();
        for(int i=0;i<size;i++)
        {
            NbiEventSendTask task = new NbiEventSendTask(retryConntectionTimes, retryIntervalTime);
            servicePool.submit(task);
        }
    }
    
    /*
     * Make sure the events with same id will be sent within order.
     */
    @Test
    public void testEventSequence() throws InterruptedException {
        System.out.println("testEventSequence begin.");
        reset();
        MyEventHttpSender sender1 = new SendSimulater();
        MyEventHttpSender sender2 = new SendSimulater();
        MyEventHttpSender sender3 = new SendSimulater();
        MyEventHttpSender sender4 = new SendSimulater();
        MyEventHttpSender sender5 = new SendSimulater();
        PowerMockito.when(NbiEventCacheUtil.getSender()).thenReturn(sender1, sender2, sender3, sender4, sender5);
        
        MyEventRetryTO event1 = new MyEventRetryTO();
        event1.setSessionId(1L);
        event1.setId(1L);
        event1.setNotificationType(NotificationTypeEnum.BEARER_ACTIVATED);
        event1.setRetryTimes(0);
        NbiEventCache.offer(event1);
        
        MyEventRetryTO event2 = new MyEventRetryTO();
        event2.setSessionId(2L);
        event2.setId(2L);
        event2.setRetryTimes(0);
        event2.setNotificationType(NotificationTypeEnum.BEARER_CHECKING_TIME_OK);
        NbiEventCache.offer(event2);
        
        MyEventRetryTO event3 = new MyEventRetryTO();
        event3.setSessionId(3L);
        event3.setId(3L);
        event3.setRetryTimes(0);
        event3.setNotificationType(NotificationTypeEnum.FILE_ADD_OK);
        NbiEventCache.offer(event3);

        MyEventRetryTO event4 = new MyEventRetryTO();
        event4.setSessionId(1L);
        event4.setId(4L);
        event4.setRetryTimes(0);
        event4.setNotificationType(NotificationTypeEnum.FILE_DOWNLOADED);
        NbiEventCache.offer(event4);

        MyEventRetryTO event5 = new MyEventRetryTO();
        event5.setSessionId(4L);
        event5.setId(5L);
        event5.setRetryTimes(0);
        event5.setNotificationType(NotificationTypeEnum.BEARER_DELETED);
        NbiEventCache.offer(event5);

        submitEvents();
        
        servicePool.shutdown();
        servicePool.awaitTermination(60, TimeUnit.SECONDS);
        
        Assert.assertEquals(2, eventMap.get(1L).size());
        Assert.assertEquals(NotificationTypeEnum.FILE_DOWNLOADED, eventMap.get(1L).get(1).getNotificationType());
        System.out.println("testEventSequence end.");
    }
    
    /*
     * Try make the queue OVERLOADED, and then check the event actually received.
     */
    @Test
    public void testOverload() throws InterruptedException 
    {
        System.out.println("testOverload begin.");
        MyEventHttpSender sender1 = new SendSimulater(0);
        MyEventHttpSender sender2 = new SendSimulater(0);
        MyEventHttpSender sender3 = new SendSimulater(0);
        MyEventHttpSender sender4 = new SendSimulater(0);
        MyEventHttpSender sender5 = new SendSimulater(0);
        PowerMockito.when(NbiEventCacheUtil.getSender()).thenReturn(sender1, sender2, sender3, sender4, sender5);
        
        reset();
        int numOfLowPEvent = NbiEventCache.HEALTH_LINE + NbiEventCacheUtil.SENDER_POOL_SIZE;
        
        generateLowPriorityEvents(0, numOfLowPEvent, true);
        
        Assert.assertEquals(numOfLowPEvent, NbiEventCache.size());
        
        submitEvents();
        
        servicePool.shutdown();
        servicePool.awaitTermination(5, TimeUnit.MINUTES);
        Assert.assertEquals(NbiEventCache.HEALTH_LINE, getTotalRecEventNum());
        System.out.println("testOverload end.");
    }
    
    /*
     * Make the queue almost full, and make the sender slow to send event. Then add new high priority events
     * into queue, the queue will be cleared correctly, and the number of the event actually received should 
     * be the sender pool size.
     */
    @Test
    public void testFull() throws Exception
    {
        System.out.println("testFull begin.");
        readyToSend = false;
        MyEventHttpSender sender1 = new SendSimulater(2000);
        MyEventHttpSender sender2 = new SendSimulater(2000);
        MyEventHttpSender sender3 = new SendSimulater(2000);
        MyEventHttpSender sender4 = new SendSimulater(2000);
        MyEventHttpSender sender5 = new SendSimulater(2000);
        PowerMockito.when(NbiEventCacheUtil.getSender()).thenReturn(sender1, sender2, sender3, sender4, sender5);
        
        reset();
        Assert.assertEquals(0, NbiEventCache.size());
        
        int numOfHighPEvent = NbiEventCache.MAX_SIZE -1;
        
        generateHighPriorityEvents(0, numOfHighPEvent, true);
        
        Assert.assertEquals(numOfHighPEvent, NbiEventCache.size());
        submitEvents();
        
        int numOfNewEvent = NbiEventCacheUtil.SENDER_POOL_SIZE + 10;
        Random r = new Random();
        for(int i=numOfHighPEvent;i<numOfHighPEvent+numOfNewEvent;i++)
        {
            MyEventRetryTO highPEvent = new MyEventRetryTO();
            highPEvent.setSessionId(Long.valueOf(r.nextInt(100)));
            highPEvent.setId(Long.valueOf(i));
            highPEvent.setRetryTimes(0);
            highPEvent.setNotificationType(NotificationTypeEnum.BEARER_ACTIVATED);
            NbiEventCache.offer(highPEvent);
            NbiEventSendTask task = new NbiEventSendTask(retryConntectionTimes, retryIntervalTime);
            servicePool.submit(task);
        }
        readyToSend = true;
        while(NbiEventCache.size()!=0)
        {
            // hold here to wait the clear operation done. 
            // otherwise, the NbiEventSendTask may be faster than the thread doing clear, so it will
            // pick a new value from the 2nd level filtered cache.
        }
        servicePool.shutdown();
        servicePool.awaitTermination(5, TimeUnit.MINUTES);
        /*
         * Initial size is MAX_SIZE-1, so all senders will pick one event to send normally.
         * Set the time of send as 2s, and meanwhile add enough events quickly into the queue to
         * make it exceed the maximum size, so the queue will be cleared.
         * Then allow the sender to send event successfully, after this, the sender will not pick any
         * new event to send.
         * Thus, the actual number of events sent is the size of the sender pool.  
         */
        Assert.assertEquals(NbiEventCacheUtil.SENDER_POOL_SIZE, getTotalRecEventNum());
        System.out.println("testFull end.");
    }
    
    private void generateLowPriorityEvents(int beginIndex, int numOfLowPEvent, boolean allowDuplicate)
    {
        Random r = new Random();
        for(int i=beginIndex;i< beginIndex+numOfLowPEvent;i++)
        {
            MyEventRetryTO lowPEvent = new MyEventRetryTO();
            if(allowDuplicate)
                lowPEvent.setSessionId(Long.valueOf(r.nextInt(100)));
            else
                lowPEvent.setSessionId(Long.valueOf(i));
            lowPEvent.setId(Long.valueOf(i));
            lowPEvent.setRetryTimes(0);
            lowPEvent.setNotificationType(NotificationTypeEnum.DELIVERY_REMOVE_OK);
            NbiEventCache.offer(lowPEvent);
        }
    }
    
    private void generateHighPriorityEvents(int beginIndex, int numOfHighPEvent, boolean allowDuplicate)
    {
        Random r = new Random();
        for(int i=beginIndex;i<beginIndex+numOfHighPEvent;i++)
        {
            MyEventRetryTO highPEvent = new MyEventRetryTO();
            if(allowDuplicate)
                highPEvent.setSessionId(Long.valueOf(r.nextInt(100)));
            else
                highPEvent.setSessionId(Long.valueOf(i));
            highPEvent.setId(Long.valueOf(i));
            highPEvent.setRetryTimes(0);
            highPEvent.setNotificationType(NotificationTypeEnum.BEARER_ACTIVATED);
            NbiEventCache.offer(highPEvent);
        }
    }
    
    /*
     * Make sure the getTotalRecEventNum() method working correctly under multiple threads.
     */
    @Test
    @Ignore
    public void testGetTotalRecEventNum() throws InterruptedException
    {
        System.out.println("testGetTotalRecEventNum begin.");
        reset();
        for(int i=0;i<5000;i++)
        {
            servicePool.submit(new Runnable() {
                
                @Override
                public void run() {
                    Random r = new Random();
                    SendSimulater sender = new SendSimulater(0);
                    MyEventTO event = new MyEventTO();
                    event.setSessionId(Long.valueOf(r.nextInt(100)));
                    sender.send(event);
                }
            });
        }
        
        servicePool.shutdown();
        servicePool.awaitTermination(300, TimeUnit.SECONDS);
        Assert.assertEquals(5000, getTotalRecEventNum());
        System.out.println("testGetTotalRecEventNum end.");
    }
    
    private int getTotalRecEventNum ()
    {
        int total = 0;
        for(ArrayList<?> e: eventMap.values())
        {
            total +=e.size();
        }
        return total;
    }
    
    private void reset()
    {
        eventMap.clear();
        NbiEventCache.reset();
    }
   
}

class SendSimulater implements MyEventHttpSender
{
    private AtomicInteger count = new AtomicInteger();
    private long sleepTime;
    
    @Override
    public boolean send(MyEventTO event) {
        try {
            do {
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } while (!NbiEventSendTaskTest.readyToSend);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count.incrementAndGet();
        synchronized (NbiEventSendTaskTest.eventMap) {
            ArrayList<MyEventTO> list = NbiEventSendTaskTest.eventMap.get(event.getSessionId());
            if(list == null)
            {
                list = new ArrayList<>();
            }
            list.add(event);
            NbiEventSendTaskTest.eventMap.put(event.getSessionId(), list);
        }
        return true;
    }
    
    public int sumOfEventReceived() {
        return count.get();
    }

    public long getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public SendSimulater(long sleepTime) {
        super();
        this.sleepTime = sleepTime;
    }
    public SendSimulater() {
        super();
        this.sleepTime = 200;
    }
}
