package com.edison.test.impl;

import java.util.Date;

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
		for(int i=0;i<500;i++)
		{
			TestDAO td = new TestDAO();
			td.setAttribute("test");
			ts.schedule(new Date(), new CreateEntityJob(td));
		}
		System.out.println("Create finished.");
	}
	
}
