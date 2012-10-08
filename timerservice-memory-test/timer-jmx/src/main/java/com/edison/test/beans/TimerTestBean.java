package com.edison.test.beans;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless(mappedName = TimerTestBeanIfc.JNDI_NAME)
@Remote(TimerTestBeanRemoteIfc.class)
@Local(TimerTestBeanIfc.class)
public class TimerTestBean implements TimerTestBeanIfc {

	private Logger LOGGER = LoggerFactory.getLogger(TimerTestBean.class);
	
	@Resource
	private TimerService timerService;

	@Override
	public void schedule() {
		TimerConfig timerConfig = new TimerConfig();

        timerConfig.setPersistent(false);

        int n = TestCounter.getCounter().getAndIncrement();
        
        if(n<800)
        {
        	timerConfig.setInfo(new TestJob(n));
        	this.timerService.createSingleActionTimer(new Date(System.currentTimeMillis() + 5), timerConfig);
        	LOGGER.info("schedule " + n + "th task");
        }
        else
        {
        	LOGGER.info("Enough!!!");
        }
	}
	
	@Timeout
	public void timeout(Timer timer) {
		TestJob job = (TestJob)timer.getInfo();
		System.out.println("Executing Job :" + job.getN());
		job = null;
		timer.cancel();
		schedule();
	}

}
