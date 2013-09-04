package com.edison.test.beans;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.edison.test.schduler.MyScheduler;
import com.edison.test.schduler.RequestMonitorJob;
import com.edison.test.schduler.SchedulerPool;

@Singleton(mappedName=TimerTestBeanIfc.JNDI_NAME)
@Remote(TimerTestBeanRemoteIfc.class)
@Local(TimerTestBeanIfc.class)
@Startup()
public class TimerTestBean implements TimerTestBeanIfc{

	@EJB
	MyScheduler scheduler;
	
	@PostConstruct
	@Override
	public void start() {
		/*scheduler.schedule(new Date(), new IncreaseJob(stb));
		scheduler.schedule(new Date(System.currentTimeMillis() + 3*1000), new DecreaseJob(stb));*/
		System.out.println("Start begin!");
		//scheduler.scheduleTask(new Date(System.currentTimeMillis()+3*1000), new RequestMonitorJob());
		//SchedulerPool.getInsance().scheduleTask(1l, new RequestMonitorJob());
		SchedulerPool.getInsance().scheduleTask(new Date(), new RequestMonitorJob());
		//executor.schedule(new RequestMonitorJob(executor), 1, TimeUnit.SECONDS);
		
		System.out.println("Start end!");
	}


}
