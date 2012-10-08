package com.edison.test.schduler;

import java.util.Date;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.beans.SingletonTestBean;
import com.edison.test.beans.TimerTestBeanIfc;
import com.edison.test.domain.Message;

public class RequestMonitorJob implements BccJob {

	private Logger LOGGER = LoggerFactory.getLogger(RequestMonitorJob.class);

	private JobDetail jd;
	
	private UUID uid = new UUID(10000l, 0l);
	
	public RequestMonitorJob() {
		super();
	}

	public RequestMonitorJob(SingletonTestBean stb) {
		super();
		
		JobDataMap map = new JobDataMap();
		map.put("SingletonTestBean", stb);
		jd = new JobDetail("RequestMonitorJob" + uid.randomUUID(), RequestMonitorJob.class);
		jd.setJobDataMap(map);
	}

	@Override
	public void execute(JobExecutionContext ctx) throws JobExecutionException {
		
		LOGGER.info("Executing RequestMonitorJob");
		
		JobDataMap map = ctx.getMergedJobDataMap();
		SingletonTestBean stb = (SingletonTestBean)map.get("SingletonTestBean");
		
		try {
			Context contxt = new InitialContext();
			BccScheduler scheduler = (BccScheduler)contxt.lookup("java:global/timer-server-1.0/BccSchedulerImpl!com.edison.test.schduler.BccScheduler");
			//TimerTestBeanIfc ttb = (TimerTestBeanIfc)contxt.lookup(TimerTestBeanIfc.JNDI_NAME);
			TimerTestBeanIfc ttb = (TimerTestBeanIfc)contxt.lookup("java:global/timer-server-1.0/TimerTestBean!com.edison.test.beans.TimerTestBeanIfc");
			
			Message msg = new Message(stb.getCounter().getAndIncrement(), "Test");
			//if(msg.getId() < 800l)
			{
				//start
				scheduler.schedule(new Date(System.currentTimeMillis()), new IncreaseJob(msg, ttb.getStb()));
				
				//scheduler.schedule(new Date(System.currentTimeMillis()), new EmptyJob());
				scheduler.schedule(new Date(System.currentTimeMillis() + 20*1000), new DecreaseJob(msg, ttb.getStb()));
			}/*else
			{
				LOGGER.info("Exceed maximum!");
			}*/
			
			scheduler.schedule(new Date(System.currentTimeMillis() + 10), new RequestMonitorJob(ttb.getStb()));
			
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	@Override
	public JobDetail getJobDetail() {
		return jd;
	}
	
	@Override
	public void setJobDetail(JobDetail jd) {
		
		this.jd =  jd;
	}

}
