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

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless(mappedName = BccScheduler.JNDI_NAME)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Local(BccScheduler.class)
public class BccSchedulerImpl implements BccScheduler {

    private final static Logger LOGGER = LoggerFactory.getLogger(BccSchedulerImpl.class);

    @Resource(mappedName="/Quartz")
    private Scheduler scheduler;
    
    public Timer schedule(Date time, BccJob job) {
    	
    	if(job.getJobDetail()==null)
    	{
    		LOGGER.error("No JobDetail!");
    		return null;
    	}
    	
    	SimpleTrigger trigger = new SimpleTrigger(job.getJobDetail().getName()+" Triger",time);
    	try {
			scheduler.scheduleJob(job.getJobDetail(), trigger);
		} catch (SchedulerException e) {
			LOGGER.error("",e);
		}
    	return null;
    }
    
    public Timer schedule(BccJob job, SimpleTrigger trigger) {
    	
    	if(job.getJobDetail()==null)
    	{
    		LOGGER.error("No JobDetail!");
    		return null;
    	}
    	
    	if(trigger == null)
    	{
    		LOGGER.error("No trigger");
    		return null;
    	}
    	
    	try {
			scheduler.scheduleJob(job.getJobDetail(), trigger);
		} catch (SchedulerException e) {
			LOGGER.error("",e);
		}
    	return null;
    }
    /*@Override
    public Timer schedule(Date time, BccJob job) {

        TimerConfig timerConfig = new TimerConfig();

        timerConfig.setPersistent(false);

        timerConfig.setInfo(job);

        Timer timer = this.timerService.createSingleActionTimer(time, timerConfig);
        return timer;
    }

    @Timeout
    public void timeoutCallback(Timer timer) {

        BccJob job = (BccJob) timer.getInfo();

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
