package com.edison.test.schduler;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.beans.SingletonTestBean;
import com.edison.test.domain.Message;

public class DecreaseJob implements BccJob {

	private Logger LOGGER = LoggerFactory.getLogger(DecreaseJob.class);

	private JobDetail jd;
	
	public DecreaseJob() {
		super();
	}

	public DecreaseJob(Message msg, SingletonTestBean stb) {
		super();
		
		JobDataMap map = new JobDataMap();
		map.put("SingletonTestBean", stb);
		map.put("Message", msg);
		jd = new JobDetail("DecreaseJob" + msg.getId(), DecreaseJob.class);
		jd.setJobDataMap(map);
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("Executing DecreaseJob");
		
		JobDataMap map = context.getMergedJobDataMap();
		SingletonTestBean stb = (SingletonTestBean)map.get("SingletonTestBean");
		Message msg = (Message)map.get("Message");
		stb.decrease(msg);
		//scheduleNext();
	}

	@Override
	public JobDetail getJobDetail() {
		return jd;
	}
	
	@Override
	public void setJobDetail(JobDetail jd) {
		
		this.jd =  jd;
	}
	
	/*private void scheduleNext() {
		try {
			Context contxt = new InitialContext();
			BccScheduler scheduler = (BccScheduler)contxt.lookup("java:app/timer-server-1.0/BccSchedulerImpl!com.edison.test.schduler.BccScheduler");
			//TimerTestBeanIfc ttb = (TimerTestBeanIfc)contxt.lookup(TimerTestBeanIfc.JNDI_NAME);
			TimerTestBeanIfc ttb = (TimerTestBeanIfc)contxt.lookup("java:app/timer-server-1.0/TimerTestBean!com.edison.test.beans.TimerTestBeanIfc");
			
			scheduler.schedule(new Date(System.currentTimeMillis() + 3*1000), new DecreaseJob(ttb.getStb()));
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}*/

}
