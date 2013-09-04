package com.edison.test.schduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayJob implements MyJob, Runnable{

	private static final long serialVersionUID = -8977392852928100661L;

	private Date date;
	private Logger LOGGER = LoggerFactory.getLogger(DelayJob.class);
	
	public DelayJob(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public void execute() {
		
	}

	@Override
	public void run() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		LOGGER.info("DelayJob target time: " + sf.format(date));
		LOGGER.info("Delt: " + (new Date().getTime() - date.getTime()));
		LOGGER.info("Execute DelayJob at " + sf.format(new Date()));
		
		try {
			Thread.sleep(2*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JobSizeCounter.decreaseJobCounter(DelayJob.class);
		LOGGER.info("End DelayJob at" + sf.format(new Date()));
		
	}

}
