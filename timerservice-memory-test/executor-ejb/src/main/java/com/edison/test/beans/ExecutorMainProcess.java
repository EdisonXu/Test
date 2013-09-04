package com.edison.test.beans;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class ExecutorMainProcess {

	@PostConstruct
	public void start()
	{
		
	}
}
