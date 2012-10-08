package com.edison.test.mbean;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.beans.TimerTestBeanIfc;

public class TimerServiceMemoryTest implements TimerServiceMemoryTestMBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimerServiceMemoryTest.class);
    
    TimerTestBeanIfc ttb;
    
    @Override
    public void test() {
    	try {
            if(ttb==null)
            {
                InitialContext ctx = new InitialContext();
                ttb = (TimerTestBeanIfc)ctx.lookup("java:global/timer-jmx-1.0/timer-jmx-1.0/TimerTestBean!com.edison.test.beans.TimerTestBeanIfc");
            }
        } catch (NamingException e) {
            LOGGER.error("Unable to find TimerTestBeanIfc.", e);
        }
    	ttb.schedule();
    }
    
}
