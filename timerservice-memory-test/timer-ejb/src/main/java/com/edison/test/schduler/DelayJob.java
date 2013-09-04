package com.edison.test.schduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.MyFutureTask;

public class DelayJob implements MyJob {

	private static final long serialVersionUID = -8977392852928100661L;
	private static ExecutorService executor = Executors.newFixedThreadPool(200);
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
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		LOGGER.info("DelayJob target time: " + sf.format(date));
		LOGGER.info("Delt: " + (new Date().getTime() - date.getTime()));
		LOGGER.info("Execute DelayJob at " + sf.format(new Date()));
		
		/*try {
			Thread.sleep(2*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		FutureTask<Boolean> future = new FutureTask<>(new MyFutureTask(date));
		executor.submit(future);
		JobSizeCounter.decreaseJobCounter(DelayJob.class);
		LOGGER.info("End DelayJob at" + sf.format(new Date()));
	}

}
