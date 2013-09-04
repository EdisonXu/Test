package com.edison.test.schduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.MySessionDAO;
import com.edison.test.ifc.MyJob;
import com.edison.test.impl.MySessionManager;
import com.edison.test.util.MyEJBHelper;

public class SessionUpdateJob extends MyJob{

	private static final long serialVersionUID = -1227994985778172171L;
	private Logger LOGGER = LoggerFactory.getLogger(SessionUpdateJob.class);
	
	public SessionUpdateJob(MySessionDAO session) {
		super();
		this.session = session;
	}

	@Override
	public void execute() {
		MySessionManager sessionManager = MyEJBHelper.getMySessionManager();
		if(sessionManager==null)
		{
			LOGGER.error("SessionManager is null when executing Update job");
			return;
		}
		sessionManager.updateSession(session);
	}
	
	
}
