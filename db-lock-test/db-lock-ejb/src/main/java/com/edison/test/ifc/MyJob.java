package com.edison.test.ifc;

import java.io.Serializable;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.edison.test.impl.TimerScheduler;

public abstract class MyJob implements Serializable{

	public abstract void execute();
	
	public TimerScheduler getTimer()
	{
	    TimerScheduler ts = null;
        try {
            InitialContext ctx = new InitialContext();
            ts = (TimerScheduler)ctx.lookup("java:module/TimerScheduler");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return ts;
	}
	
	public TestDbManager getDbManager()
	{
	    TestDbManager dbManager = null;
        try {
            InitialContext ctx = new InitialContext();
            dbManager = (TestDbManager)ctx.lookup("java:module/TestDbManagerImpl");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return dbManager;
	}
	
}
