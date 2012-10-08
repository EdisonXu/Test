package com.edison.test.schduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.beans.SingletonTestBean;
import com.edison.test.domain.Message;

@SuppressWarnings("serial")
public class DecreaseJob implements MyJob {

	private Logger LOGGER = LoggerFactory.getLogger(DecreaseJob.class);

	private Message msg;
	
	private SingletonTestBean stb;
	
	public DecreaseJob(Message msg, SingletonTestBean stb) {
		super();
		this.msg = msg;
		this.stb = stb;
	}

	@Override
	public void execute() {
		stb.decrease(msg);
		//scheduleNext();
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
