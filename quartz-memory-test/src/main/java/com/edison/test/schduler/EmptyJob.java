package com.edison.test.schduler;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyJob implements BccJob {

	private Logger LOGGER = LoggerFactory.getLogger(EmptyJob.class);
	
	private JobDetail jd;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		LOGGER.info("I'm ghost~~~");
	}
	
	public EmptyJob() {
		super();
		jd = new JobDetail("EmptyJob", EmptyJob.class);
	}

	@Override
	public JobDetail getJobDetail() {
		return jd;
	}

	
	@Override
	public void setJobDetail(JobDetail jd) {
		this.jd =  jd;
	}
}
