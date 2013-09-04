package com.edison.test.schduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.edison.test.schduler.DelayJob;
import com.edison.test.schduler.JobSizeCounter;

public class DelayJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Execute DelayJob at: " + new Date(System.currentTimeMillis()));
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JobSizeCounter.decreaseJobCounter(DelayJob.class);
		System.out.println("DelayJob ends at: " + new Date(System.currentTimeMillis()));
	}

}
