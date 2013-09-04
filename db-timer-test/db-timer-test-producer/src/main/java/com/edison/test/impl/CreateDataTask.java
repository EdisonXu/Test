package com.edison.test.impl;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.MySessionDAO;
import com.edison.test.config.ConfigParameters;
import com.edison.test.config.TrafficModel;
import com.edison.test.ifc.MySessionDbManager;
import com.edison.test.util.MyEJBHelper;

public class CreateDataTask implements Runnable {

	public static Date currentFetchTime = new Date();
	
	public static ScheduledExecutorService service = Executors.newScheduledThreadPool(100);
	
	Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	//@EJB
	MySessionDbManager dbManager;
	
	//@Asynchronous
	/*public void begin()
	{
		for(int i=0;i<ConfigParameters.MAX_SESSION;i++)
		{
			LOGGER.info("REQUEST : [" + i + "]");
			Date startTime = new Date(System.currentTimeMillis()+ConfigParameters.WAIT_TIME);
			Date stopTime = new Date(startTime.getTime()+ConfigParameters.DURATION);
			MySessionDAO td = new MySessionDAO(startTime, stopTime);
			
			td = dbManager.create(td);
			
			try {
				Thread.sleep(ConfigParameters.CREATE_DATA_INTERVAL);
			} catch (InterruptedException e) {
				LOGGER.debug("", e);
			}
		}
	}*/

	private TrafficModel tm;
	private int counter = 0;
	
	public CreateDataTask() {
		super();
	}
	
	public CreateDataTask(TrafficModel tm) {
		super();
		this.tm = tm;
	}

	public CreateDataTask(TrafficModel tm, int counter) {
		super();
		this.tm = tm;
		this.counter = counter;
	}

	@Override
	public void run() {
		counter++;
		if(counter<tm.getMaxSession())
		{
			CreateDataTask creatDateTask = new CreateDataTask(tm,counter);
			service.schedule(creatDateTask,tm.getCreateDataInterval(), TimeUnit.MILLISECONDS);
		}
		if(dbManager==null)
		{
			dbManager = MyEJBHelper.getMySessionDbManager();
			Date startTime = new Date(System.currentTimeMillis()+tm.getWaitTime());
			Date stopTime = new Date(startTime.getTime()+tm.getDuration());
			MySessionDAO td = new MySessionDAO(startTime, stopTime);
			
			td = dbManager.create(td);
		}
	}
}
