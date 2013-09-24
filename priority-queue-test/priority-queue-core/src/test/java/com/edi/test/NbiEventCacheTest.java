package com.edi.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.edi.test.bean.MyEventRetryTO;
import com.edi.test.events.NbiEventCache;


public class NbiEventCacheTest {

    @Test
    public void testOffer() {
        NbiEventCache.reset();
        NbiEventCache.offer(new MyEventRetryTO());
        Assert.assertEquals(1, NbiEventCache.size());
    }

    @Test
    public void testPoll() {
        NbiEventCache.clear();
        MyEventRetryTO event = new MyEventRetryTO();
        event.setId(1L);
        event.setSessionId(3L);
        event.setDescription("Test");
        NbiEventCache.offer(event);
        
        MyEventRetryTO polled = NbiEventCache.poll();
        Assert.assertEquals(true, event.equals(polled));
    }

    @Test
    public void testClear() {
        NbiEventCache.clear();
        int size = 5;
        for(int i=0;i<size;i++)
        {
            MyEventRetryTO event = new MyEventRetryTO();
            event.setId(Long.valueOf(i));
            NbiEventCache.offer(event);
        }
        
        List<MyEventRetryTO> list = NbiEventCache.clear();
        
        assertEquals(0, list.size());
        
        assertEquals(size, NbiEventCache.size());
    }

}
