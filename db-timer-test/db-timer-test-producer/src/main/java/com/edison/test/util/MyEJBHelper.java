package com.edison.test.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.ifc.MyScheduler;
import com.edison.test.ifc.MySessionDbManager;
import com.edison.test.impl.AlarmEventHandlerImpl;
import com.edison.test.impl.MySessionManager;

public class MyEJBHelper {

	static final Logger LOGGER = LoggerFactory.getLogger(MyEJBHelper.class);
	
	private static Object lookup(String jndiName)
	{
		Context context = null;
        try {
//            context = getJnpAwareContext();
            context = new InitialContext();
            return context.lookup(jndiName);
        } catch (NamingException ex) {
        	LOGGER.error("jndi lookup failed.", ex);
        } finally {
            try {
                context.close();
            } catch (NamingException ex) {
            	LOGGER.error("close context error.", ex);
            }
        }
        return null;
	}
	
	public static MyScheduler getScheduler()
	{
		Object result = lookup("java:module/MySchedulerImpl!com.edison.test.ifc.MyScheduler");
		if(result!=null)
		{
			return (MyScheduler) result;
		}else
		{
			return null;
		}
	}
	
	public static MySessionDbManager getMySessionDbManager()
	{
		//Object result = lookup("java:module/MySessionDbManagerImpl!com.edison.test.ifc.MySessionDbManager");
		Object result = lookup("java:global/db-timer-test-producer-ear/db-timer-test-producer/MySessionDbManagerImpl!com.edison.test.ifc.MySessionDbManager");
		
		if(result!=null)
		{
			return (MySessionDbManager) result;
		}else
		{
			return null;
		}
	}
	
	public static MySessionManager getMySessionManager()
	{
		Object result = lookup("java:module/MySessionManager!com.edison.test.impl.MySessionManager");
		if(result!=null)
		{
			return (MySessionManager) result;
		}else
		{
			return null;
		}
	}
	
}
