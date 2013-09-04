package com.edison.test.schduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.MySessionDAO;
import com.edison.test.ifc.MyJob;
import com.edison.test.impl.MySessionManager;

public class SessionStartJob extends MyJob {

	private static final long serialVersionUID = 5030563570672042742L;
	private MySessionManager sessionManager;
	private Logger LOGGER = LoggerFactory.getLogger(SessionStartJob.class);

	public SessionStartJob(MySessionManager sessionManager, MySessionDAO session) {
		super();
		this.sessionManager = sessionManager;
		this.session = session;
	}


	@Override
	public void execute() {
		sessionManager.startSession(session);
	}

}
