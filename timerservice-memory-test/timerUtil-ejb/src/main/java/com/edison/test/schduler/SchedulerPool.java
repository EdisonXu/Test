package com.edison.test.schduler;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerPool {

	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2000);
	
	private static SchedulerPool instance = new SchedulerPool();
	
	private SchedulerPool () 
	{
		
	}
	
	public static SchedulerPool getInsance()
	{
		return instance;
	}
	public void scheduleTask(Date time, Runnable task)
    {
		Date now = new Date();
		if(time.before(now))
		{
			executor.schedule(task, 0, TimeUnit.MILLISECONDS);
		}else
		{
			executor.schedule(task, (time.getTime()-now.getTime()), TimeUnit.MILLISECONDS);
		}
    	System.out.println("Schedule task at" + time);
    	executor.schedule(new Runnable(){

			@Override
			public void run() {
				System.out.println("Run");
			}
    		
    	}
    	, 1, TimeUnit.MILLISECONDS);
    }
	
	public void scheduleTask(Long time, Runnable task)
    {
    	System.out.println("Schedule task at" + time);
    	executor.schedule(task, time, TimeUnit.MILLISECONDS);
    	executor.schedule(new Runnable(){

			@Override
			public void run() {
				System.out.println("Run");
			}
    		
    	}
    	, 1, TimeUnit.MILLISECONDS);
    }
}
