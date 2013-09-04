package com.edison.test.impl;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.edison.test.TestDAO;
import com.edison.test.ifc.TestDbManager;

@Singleton
@Startup
public class MainProcess {

	@EJB
	TestDbManager dbManager;
	
	@EJB
	TimerScheduler ts;
	
	@PostConstruct
	public void startup()
	{
		for(int i=0;i<1;i++)
		{
			TestDAO td = new TestDAO();
			td.setAttribute("test");
			
			td = dbManager.create(td);
			
			UpdateEntityJob updateJob = new UpdateEntityJob(td);

			ts.schedule(2000l, updateJob);
			
		}
	}
	
}
