package com.edison.test.schduler;

import java.util.Date;

import javax.ejb.Timer;

public interface MyScheduler {

    public final static String JNDI_NAME = "MyScheduler/remote";

    public final static String NAME = "MyScheduler/local";

    public void scheduleTask(Date time, Runnable task);
    
}

