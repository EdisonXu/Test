package com.edison.test.beans;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.edison.test.schduler.MyScheduler;
import com.edison.test.schduler.RequestMonitorJob;

@Singleton(mappedName=TimerTestBeanIfc.JNDI_NAME)
@Remote(TimerTestBeanRemoteIfc.class)
@Local(TimerTestBeanIfc.class)
@Startup()
@DependsOn("SingletonTestBean")
public class TimerTestBean implements TimerTestBeanIfc{

	@EJB
	MyScheduler scheduler;
	
	@EJB
	SingletonTestBean stb;
	
	@PostConstruct
	@Override
	public void start() {
		/*scheduler.schedule(new Date(), new IncreaseJob(stb));
		scheduler.schedule(new Date(System.currentTimeMillis() + 3*1000), new DecreaseJob(stb));*/
		scheduler.schedule(new Date(System.currentTimeMillis()), new RequestMonitorJob(stb));
	}

	@Override
	public SingletonTestBean getStb() {
		return stb;
	}

}
