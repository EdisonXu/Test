package com.edison.test.impl;

import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.edison.test.TestDAO;
import com.edison.test.ifc.MyJob;
import com.edison.test.ifc.TestDbManager;

public class RemoveEntityJob implements MyJob {

	private TestDAO td;
	private TestDbManager dbManager;
	
	public RemoveEntityJob(TestDAO td) {
		super();
		this.td = td;
	}

	@Override
	public void execute() {
		
		try {
			InitialContext ctx = new InitialContext();
			dbManager = (TestDbManager)ctx.lookup("java:module/TestDbManagerImpl");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		Date now = new Date();
		dbManager.lock(td.getId());
		Date after = new Date();
		if(!now.equals(after))
		{
			System.out.println("I'm waiting! "+(after.getTime()-now.getTime()));
		}
		dbManager.remove(td.getId());
		System.out.println("Removed cost "+(new Date().getTime()-now.getTime()));
	}

}
