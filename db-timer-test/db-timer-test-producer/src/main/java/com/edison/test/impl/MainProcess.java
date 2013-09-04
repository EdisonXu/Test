package com.edison.test.impl;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.config.ConfigParameters;
import com.edison.test.config.TrafficModel;
import com.edison.test.ifc.MyScheduler;
import com.edison.test.schduler.RetrieveJob;

@Singleton
@Startup
public class MainProcess {

	final Logger LOGGER = LoggerFactory.getLogger(MainProcess.class);
	//CreateDataTask creatDateTask;
	
	ExecutorService pool = Executors.newFixedThreadPool(20);
	
	@EJB
	MySessionManager sessionManager;
	
	@EJB
	MyScheduler schduler;
	
	@PostConstruct
	public void startup()
	{
		
		RetrieveJob rj = new RetrieveJob(sessionManager);
		schduler.schedule(new Date(), rj);
		
		TrafficModel tm1 = new TrafficModel();
		TrafficModel tm2 = new TrafficModel();
		tm2.setCreateDataInterval(8600);
		tm2.setDuration(500000);
		TrafficModel tm3 = new TrafficModel();
		tm3.setCreateDataInterval(15200);
		tm3.setDuration(300000);
		
		/*for(int i=0;i<=ConfigParameters.MAX_SESSION;i++)
		{
			CreateDataTask creatDateTask = new CreateDataTask(tm1,0);
			pool.execute(creatDateTask);
			//new Thread(creatDateTask).start();
			//LOGGER.info("REQUEST : [" + i + "]");
			try {
				Thread.sleep(ConfigParameters.CREATE_DATA_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		CreateDataTask creatDateTask1 = new CreateDataTask(tm1);
		CreateDataTask creatDateTask2 = new CreateDataTask(tm2);
		CreateDataTask creatDateTask3 = new CreateDataTask(tm3);
		pool.execute(creatDateTask1);
		pool.execute(creatDateTask2);
		pool.execute(creatDateTask3);
	}
	
	@PreDestroy
	public void destroy()
	{
		pool.shutdown();
	}
}