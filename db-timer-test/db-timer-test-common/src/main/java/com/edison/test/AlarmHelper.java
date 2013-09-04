package com.edison.test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.ifc.AlarmEventHandlerRemote;


public class AlarmHelper {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AlarmHelper.class);
	
	private static Context context;
	
	private static Context getContext() throws NamingException {
		if (context == null) {
			context = new InitialContext();
		}
		return context;
	}
	
	public static void clearAlarm()
	{
		AlarmEventHandlerRemote eventHandler = null;
		
		if(eventHandler==null)
    	{
    		try {
    			Context context = new InitialContext();
    			eventHandler = (AlarmEventHandlerRemote) context
    					.lookup("java:global/db-timer-test-producer-ear/db-timer-test-producer/AlarmEventHandlerImpl!com.edison.test.ifc.AlarmEventHandlerRemote");
    		} catch (NamingException e) {
    			LOGGER.error("", e);
    		}
    	}
		
        if (eventHandler != null) {
            eventHandler.clearAlarm("The application can successfully access the Oracle database server.");
        }
	}
}
