package com.edison.test.schduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayJob implements BccJob {

	private JobDetail jd;
	private Logger LOGGER = LoggerFactory.getLogger(DelayJob.class);
	
	public DelayJob() {
		super();
	}
	
	public DelayJob(Date date) {
		super();
		JobDataMap map = new JobDataMap();
		map.put("Date", date);
		jd = new JobDetail("DelayJob", DecreaseJob.class);
		jd.setJobDataMap(map);
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		JobDataMap map = context.getMergedJobDataMap();
		Date date = (Date)map.get("Date");
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

	@Override
	public JobDetail getJobDetail() {
		return jd;
	}

	@Override
	public void setJobDetail(JobDetail jd) {
		this.jd = jd;
	}

}
