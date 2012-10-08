package com.edison.test.schduler;

import java.util.Date;

import javax.ejb.Timer;

import org.quartz.SimpleTrigger;

public interface BccScheduler {

    public final static String JNDI_NAME = "BccScheduler/remote";

    public final static String NAME = "BccScheduler/local";

    public Timer schedule(Date time, BccJob job);
    
    public Timer schedule(BccJob job, SimpleTrigger trigger);
}

