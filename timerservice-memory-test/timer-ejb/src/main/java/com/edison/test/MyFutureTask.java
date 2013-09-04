package com.edison.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.schduler.DelayJob;
import com.edison.test.schduler.JobSizeCounter;

public class MyFutureTask implements Callable<Boolean> {
	
	private Logger LOGGER = LoggerFactory.getLogger(MyFutureTask.class);
	private Date date;
	
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}

	public MyFutureTask(Date date) {
		super();
		this.date = date;
	}

	public MyFutureTask() {
		super();
	}

	@Override
	public Boolean call() throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		LOGGER.info("MyFutureTask target time: " + sf.format(date));
		LOGGER.info("Delt, " + (new Date().getTime() - date.getTime()));
		LOGGER.info("Execute MyFutureTask at " + sf.format(new Date()));
		
		try {
			Thread.sleep(2*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JobSizeCounter.decreaseJobCounter(DelayJob.class);
		LOGGER.info("End MyFutureTask at" + sf.format(new Date()));
		return null;
	}

}
