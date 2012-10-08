package com.edison.test.beans;

public interface TimerTestBeanRemoteIfc {

	public final static String JNDI_NAME = "TimerTestBean/remote";

    public final static String NAME = "TimerTestBean/local";
    
	public void start();
	
	public SingletonTestBean getStb();
}
