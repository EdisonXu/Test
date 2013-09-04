package com.edison.test.schduler;

import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.beans.SingletonTestBean;
import com.edison.test.beans.TimerTestBeanIfc;
import com.edison.test.domain.Message;

@SuppressWarnings("serial")
public class RequestMonitorJob implements MyJob {

	private Logger LOGGER = LoggerFactory.getLogger(RequestMonitorJob.class);

	private SingletonTestBean stb;

	public RequestMonitorJob(SingletonTestBean stb) {
		super();
		this.stb = stb;
	}

	@Override
	public void execute() {
		try {
			Context contxt = new InitialContext();
			MyScheduler scheduler = (MyScheduler)contxt.lookup("java:app/timer-ejb-1.0/MySchedulerImpl!com.edison.test.schduler.MyScheduler");
			//TimerTestBeanIfc ttb = (TimerTestBeanIfc)contxt.lookup(TimerTestBeanIfc.JNDI_NAME);
			TimerTestBeanIfc ttb = (TimerTestBeanIfc)contxt.lookup("java:app/timer-ejb-1.0/TimerTestBean!com.edison.test.beans.TimerTestBeanIfc");
			
			Message msg = new Message(stb.getCounter().getAndIncrement(), "Test");
			//if(msg.getId() < 800l)
			/*{
				//start
				scheduler.schedule(new Date(System.currentTimeMillis()), new IncreaseJob(msg, ttb.getStb()));
				
				scheduler.schedule(new Date(System.currentTimeMillis()), new EmptyJob());
				
				LOGGER.info("Schedule Decrease Job");
				scheduler.schedule(new Date(System.currentTimeMillis() + 20*1000), new DecreaseJob(msg, ttb.getStb()));
				
				scheduler.schedule(new Date(System.currentTimeMillis() + 10), new RequestMonitorJob(ttb.getStb()));
			}
			*/
			for(int i = 0;i<800;i++)
			{
				System.out.println("Schdule DelayJob: " + i);
				scheduler.schedule(new Date(new Date().getTime() + 2*1000), new DelayJob(new Date(new Date().getTime() + 2*1000)));
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
