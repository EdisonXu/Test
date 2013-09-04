package com.edison.test.schduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.MySessionDAO;
import com.edison.test.ifc.MyJob;
import com.edison.test.impl.MySessionManager;
import com.edison.test.util.MyEJBHelper;

public class SessionStopJob extends MyJob {

	private static final long serialVersionUID = 6797145642868555785L;
	private Logger LOGGER = LoggerFactory.getLogger(SessionStopJob.class);
	
	public SessionStopJob(MySessionDAO session) {
		super();
		this.session = session;
	}

	@Override
	public void execute() {
		MySessionManager sessionManager = MyEJBHelper.getMySessionManager();
		if(sessionManager==null)
		{
			LOGGER.error("SessionManager is null when executing stop job");
			return;
		}
		sessionManager.stopSession(session);
	}

}
