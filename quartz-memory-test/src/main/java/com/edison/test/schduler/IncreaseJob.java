package com.edison.test.schduler;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.beans.SingletonTestBean;
import com.edison.test.domain.Message;

public class IncreaseJob implements BccJob {

	private Logger LOGGER = LoggerFactory.getLogger(IncreaseJob.class);
	
	private JobDetail jd;
	
	public IncreaseJob() {
		super();
	}


	public IncreaseJob(Message msg, SingletonTestBean stb) {
		super();
		
		JobDataMap map = new JobDataMap();
		map.put("SingletonTestBean", stb);
		map.put("Message", msg);
		jd = new JobDetail("IncreaseJob" + msg.getId(), IncreaseJob.class);
		jd.setJobDataMap(map);
	}
	
	@Override
	public void setJobDetail(JobDetail jd) {
		
		this.jd =  jd;
	}


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("Executing IncreaseJob");
		
		JobDataMap map = context.getMergedJobDataMap();
		SingletonTestBean stb = (SingletonTestBean)map.get("SingletonTestBean");
		Message msg = (Message)map.get("Message");
		stb.increase(msg);
		//scheduleNext();
	}


	@Override
	public JobDetail getJobDetail() {
		return jd;
	}

	/*private void scheduleNext() {
		try {
			Context contxt = new InitialContext();
			BccScheduler scheduler = (BccScheduler)contxt.lookup("java:app/timer-server-1.0/BccSchedulerImpl!com.edison.test.schduler.BccScheduler");
			//TimerTestBeanIfc ttb = (TimerTestBeanIfc)contxt.lookup(TimerTestBeanIfc.JNDI_NAME);
			TimerTestBeanIfc ttb = (TimerTestBeanIfc)contxt.lookup("java:app/timer-server-1.0/TimerTestBean!com.edison.test.beans.TimerTestBeanIfc");
			
			scheduler.schedule(new Date(System.currentTimeMillis() + 3*1000), new IncreaseJob(ttb.getStb()));
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}*/
}
