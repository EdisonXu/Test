package com.edison.test.schduler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.beans.TimerTestBeanIfc;

@SuppressWarnings("serial")
public class RequestMonitorJob implements MyJob,Runnable {

	private Logger LOGGER = LoggerFactory.getLogger(RequestMonitorJob.class);

	private ScheduledExecutorService executor;
	
	public RequestMonitorJob()
	{
		
	}
	
	public RequestMonitorJob (ScheduledExecutorService executor)
	{
		this.executor = executor;
	}
	
	@Override
	public void execute() {
		
	}

	@Override
	public void run() {
		try {
			System.out.println("IN");
			Context contxt = new InitialContext();
			//MyScheduler scheduler = (MyScheduler)contxt.lookup("java:app/timerUtil-ejb-1.0/MySchedulerImpl!com.edison.test.schduler.MyScheduler");
			//TimerTestBeanIfc ttb = (TimerTestBeanIfc)contxt.lookup(TimerTestBeanIfc.JNDI_NAME);
			
			/*//if(msg.getId() < 800l)
			{
				//start
				scheduler.schedule(new Date(System.currentTimeMillis()), new IncreaseJob(msg, ttb.getStb()));
				
				scheduler.schedule(new Date(System.currentTimeMillis()), new EmptyJob());
				
				LOGGER.info("Schedule Decrease Job");
				scheduler.schedule(new Date(System.currentTimeMillis() + 20*1000), new DecreaseJob(msg, ttb.getStb()));
				
				scheduler.schedule(new Date(System.currentTimeMillis() + 10), new RequestMonitorJob(ttb.getStb()));
			}*/
			
			for(int i = 0;i<800;i++)
			{
				System.out.println("Schdule DelayJob: " + i);
				//scheduler.scheduleTask(new Date(new Date().getTime() + 2*1000), new DelayJob(new Date(new Date().getTime() + 2*1000)));
				SchedulerPool.getInsance().scheduleTask(new Date(System.currentTimeMillis()+2*1000), new DelayJob(new Date(new Date().getTime() + 2*1000)));
				//executor.schedule(new DelayJob(new Date(new Date().getTime() + 2*1000)), 2000, TimeUnit.MILLISECONDS);
				try {
					// 4*1000/800 = 5 (ms)
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
				JobSizeCounter.increaseJobCounter(DelayJob.class);
			}
			
			
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

}
