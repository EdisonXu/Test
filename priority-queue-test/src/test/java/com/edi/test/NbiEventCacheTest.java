package com.ericsson.ecds.bcc.prov.events;

import static org.junit.Assert.*;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.ericsson.ecds.bcc.prov.common.data.BmscEventRetryTO;

public class NbiEventCacheTest {

    @Test
    public void testOffer() {
        NbiEventCache.clear();
        NbiEventCache.offer(new BmscEventRetryTO());
        Assert.assertEquals(1, NbiEventCache.size());
    }

    @Test
    public void testPoll() {
        NbiEventCache.clear();
        BmscEventRetryTO event = new BmscEventRetryTO();
        event.setId(1L);
        event.setContentGroupId(2L);
        event.setEMBMSSessionId(3L);
        event.setDescription("Test");
        NbiEventCache.offer(event);
        
        BmscEventRetryTO polled = NbiEventCache.poll();
        Assert.assertEquals(true, event.equals(polled));
    }

    @Test
    public void testClear() {
        NbiEventCache.clear();
        int size = 5;
        for(int i=0;i<size;i++)
        {
            BmscEventRetryTO event = new BmscEventRetryTO();
            event.setId(Long.valueOf(i));
            NbiEventCache.offer(event);
        }
        
        List<BmscEventRetryTO> list = NbiEventCache.clear();
        
        assertEquals(size, list.size());
        
        assertEquals(0, NbiEventCache.size());
    }

}
