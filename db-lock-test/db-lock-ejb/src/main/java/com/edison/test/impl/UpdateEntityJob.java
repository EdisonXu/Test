package com.edison.test.impl;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.edison.test.TestDAO;
import com.edison.test.ifc.MyJob;
import com.edison.test.ifc.TestDbManager;

public class UpdateEntityJob implements MyJob {

	private TestDAO td;
	private TestDbManager dbManager;
	
	
	public UpdateEntityJob(TestDAO td) {
		super();
		this.td = td;
	}

	@Override
	public void execute() {
		TimerScheduler ts = null;
		try {
			InitialContext ctx = new InitialContext();
			ts = (TimerScheduler)ctx.lookup("java:module/TimerScheduler");
			dbManager = (TestDbManager)ctx.lookup("java:module/TestDbManagerImpl");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		//dbManager.lock(td.getId());
		td.setAttribute("Update");
		ReupdateEntityJob job = new ReupdateEntityJob(td);
		ts.schedule(2l, job);
		try {
			Thread.sleep(1l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Execute Update " + td.getId());
		dbManager.update(td);
	}

}
