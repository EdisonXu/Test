package com.edison.test.schduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class EmptyJob implements MyJob {

	private Logger LOGGER = LoggerFactory.getLogger(EmptyJob.class);
	
	@Override
	public void execute() {
		LOGGER.info("I'm ghost~~~");
	}

}
