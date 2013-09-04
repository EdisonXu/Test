package com.edison.test.ifc;

import java.util.Date;

import javax.ejb.Timer;

public interface MyScheduler {

    public final static String JNDI_NAME = "MyScheduler/remote";

    public final static String NAME = "MyScheduler/local";

    public Timer schedule(Date time, MyJob job);
    
}

