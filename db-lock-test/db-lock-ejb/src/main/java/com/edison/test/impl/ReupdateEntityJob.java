package com.edison.test.impl;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.edison.test.TestDAO;
import com.edison.test.ifc.MyJob;
import com.edison.test.ifc.TestDbManager;

public class ReupdateEntityJob implements MyJob {

	private TestDAO td;
	private TestDbManager dbManager;
	
	
	public ReupdateEntityJob(TestDAO td) {
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
		dbManager.lock(td.getId());
		td.setAttribute("Reupdate");
		RemoveEntityJob removeJob = new RemoveEntityJob(td);
		ts.schedule(0l, removeJob);
		try {
			Thread.sleep(150l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Execute Reupdate " + td.getId());
		dbManager.update(td);
	}

}
