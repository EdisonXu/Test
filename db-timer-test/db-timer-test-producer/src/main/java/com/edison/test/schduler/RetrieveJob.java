package com.edison.test.schduler;

import java.util.Date;
import java.util.List;

import javax.ejb.Asynchronous;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.MySessionDAO;
import com.edison.test.ifc.MyJob;
import com.edison.test.ifc.MyScheduler;
import com.edison.test.ifc.MySessionDbManager;
import com.edison.test.impl.MySessionManager;
import com.edison.test.util.MyEJBHelper;

public class RetrieveJob extends MyJob {

	private static final long serialVersionUID = -4992255749972180152L;

	private MySessionManager sessionManager;
	
	final Logger LOGGER = LoggerFactory.getLogger(RetrieveJob.class);
	
	public RetrieveJob(MySessionManager sessionManager) {
		super();
		this.sessionManager = sessionManager;
	}

	@Override
	public void execute() {
        scheduleSessionStart();
        scheduleNextRetrieve();
	}
	
	@Asynchronous
	private void scheduleSessionStart()
	{
		Date start = sessionManager.getCurrentFetchTime();
        Date end = sessionManager.getNextFetchTime();
		MySessionDbManager dbManager = MyEJBHelper.getMySessionDbManager();
		if(dbManager==null)
		{
			LOGGER.error("Dbmanager is null!");
			return;
		}
		
		List<MySessionDAO> sessions = dbManager.findSessionByDuration(start, end);
		for(MySessionDAO each: sessions)
		{
			if(sessionManager.getStartedSessions().containsKey(each.getId()) || each.isStarted())
			{
				continue;
			}
			System.out.println("Retrieve session " + each.getId() + " started: " + each.isStarted());
			MyScheduler scheduler = MyEJBHelper.getScheduler();
			if(scheduler!=null)
			{
				SessionStartJob ssj = new SessionStartJob(sessionManager, each);
				scheduler.schedule(each.getStartTime(), ssj);
				sessionManager.getStartedSessions().put(each.getId(), each);
			}else
			{
				LOGGER.error("scheduler cannot be null!");
			}
		}
		
	}
	
	private void scheduleNextRetrieve()
	{
		Date timeToFetch = sessionManager.getNextFetchTime();
		RetrieveJob rj = new RetrieveJob(sessionManager);
		MyScheduler scheduler = MyEJBHelper.getScheduler();
		if(scheduler!=null)
		{
			scheduler.schedule(timeToFetch, rj);
		}else
		{
			LOGGER.error("scheduler cannot be null!");
		}
	}

}
