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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton(mappedName = MyScheduler.JNDI_NAME)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Local(MyScheduler.class)
public class MySchedulerImpl implements MyScheduler {

    private final static Logger LOGGER = LoggerFactory.getLogger(MySchedulerImpl.class);
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(20);
    @Resource
    private TimerService timerService;

    /*@Override
    public Timer schedule(Date time, MyJob job) {

        TimerConfig timerConfig = new TimerConfig();

        timerConfig.setPersistent(false);

        timerConfig.setInfo(job);

        Timer timer = this.timerService.createSingleActionTimer(time, timerConfig);
        return timer;
    }*/
    
    @Override
    public void scheduleTask(Date time, Runnable task)
    {
    	System.out.println("Schedule task at" + time);
    	executor.schedule(task, time.getTime(), TimeUnit.MILLISECONDS);
    }

    /*@Timeout
    public void timeoutCallback(Timer timer) {

        MyJob job = (MyJob) timer.getInfo();

        try
        {
            job.execute();
        }
        catch (Exception ex)
        {
            LOGGER.debug("Executing Job["+job+"] exception.",ex);
        }

    }*/
}
