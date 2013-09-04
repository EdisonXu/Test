/**
 * Copyright (c) Ericsson AB, 2011.
 *
 * All Rights Reserved. Reproduction in whole or in part is prohibited
 * without the written consent of the copyright owner.
 *
 * ERICSSON MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. ERICSSON SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.edison.test.schduler;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.ifc.MyScheduler;
import com.edison.test.ifc.MyJob;

@Stateless(mappedName = MyScheduler.JNDI_NAME)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Local(MyScheduler.class)
public class MySchedulerImpl implements MyScheduler {

    private final static Logger LOGGER = LoggerFactory.getLogger(MySchedulerImpl.class);

    public static AtomicInteger onGoingTimers = new AtomicInteger();
    public static AtomicInteger waitingTimers = new AtomicInteger();
    
    @Resource
    private TimerService timerService;

    @Override
    public Timer schedule(Date time, MyJob job) {

        TimerConfig timerConfig = new TimerConfig();

        timerConfig.setPersistent(false);

        job.setTargetDate(time);
        
        timerConfig.setInfo(job);

        Timer timer = this.timerService.createSingleActionTimer(time, timerConfig);
        LOGGER.info("Current waiting timers, " + waitingTimers.incrementAndGet());
        return timer;
    }

    @Timeout
    public void timeoutCallback(Timer timer) {

    	LOGGER.info("Current waiting timers, " + waitingTimers.decrementAndGet());
    	
    	LOGGER.info("Current on going timers, " + onGoingTimers.incrementAndGet());
        MyJob job = (MyJob) timer.getInfo();

        Date beforeExe = new Date();
        try
        {
            job.execute();
        }
        catch (Exception ex)
        {
            LOGGER.debug("Executing Job["+job+"] exception.",ex);
        }
        
        Date afterExe = new Date();
        
        LOGGER.info("Current on going timers, " + onGoingTimers.decrementAndGet());
        
        String sessionId = null;
        
        if(job.getSession()!=null)
        {
        	sessionId = String.valueOf(job.getSession().getId());
        }
        
        LOGGER.info("Job," + job + ",Session," + sessionId  
        		+ ",execute time," + (afterExe.getTime()-beforeExe.getTime()) 
        		+ ",delta," + (beforeExe.getTime()-job.getTargetDate().getTime()));
    }
}
