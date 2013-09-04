package com.edison.test.impl;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import com.edison.test.MySessionDAO;
import com.edison.test.config.ConfigParameters;
import com.edison.test.ifc.MyScheduler;
import com.edison.test.ifc.MySessionDbManager;
import com.edison.test.schduler.SessionStopJob;
import com.edison.test.schduler.SessionUpdateJob;


@Singleton
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class MySessionManager {

	private Object fetchingTimeLock = new Object();
	private Date currentFetchTime = null;
    private Date nextFetchTime = null;
    private final long OFFSET_DURATION = 300;
    private Map<Long, MySessionDAO> startedSessions = new ConcurrentHashMap<>();
    private Logger LOGGER = LoggerFactory.getLogger(MySessionManager.class);
    
	@EJB
	MySessionDbManager dbManager;
	
	@EJB
	MyScheduler scheduler;
	
	public void startSession(MySessionDAO session)
	{
		LOGGER.info("Start session: [" + session.getId() + "].");
		session.setStarted(true);
		startedSessions.put(session.getId(), session);
		SessionStopJob ssj = new SessionStopJob(session);
		scheduler.schedule(session.getStopTime(), ssj);
		
		SessionUpdateJob suj = new SessionUpdateJob(session);
		scheduler.schedule(new Date(System.currentTimeMillis() + ConfigParameters.UPDATE_INTERVAL), suj);
		
	}
	
	public void updateSession(MySessionDAO session)
	{
		LOGGER.info("Update session: [" + session.getId() + "].");
		int updateTimes = session.getUpdateTimes();
		if(updateTimes <=ConfigParameters.UPDATE_TIMES)
		{
			updateTimes++;
			session.setUpdateTimes(updateTimes);
			session.setAttribute(String.valueOf(updateTimes));
			
			SessionUpdateJob suj = new SessionUpdateJob(session);
			scheduler.schedule(new Date(System.currentTimeMillis() + ConfigParameters.UPDATE_INTERVAL), suj);

			dbManager.update(session);
		}
		
	}
	
	public void stopSession(MySessionDAO session)
	{
		this.startedSessions.remove(session.getId());
		dbManager.remove(session.getId());
	}

	public Date getCurrentFetchTime() {
		//first time when system startup
        synchronized (fetchingTimeLock) {
            if (nextFetchTime == null) {
                Date now = new Date();
                currentFetchTime = new Date(now.getTime() - OFFSET_DURATION);
                nextFetchTime = new Date(now.getTime() + ConfigParameters.FETCH_DURATION);
            } else {
                currentFetchTime = nextFetchTime;
                nextFetchTime = new Date(currentFetchTime.getTime() + ConfigParameters.FETCH_DURATION);
            }

            return currentFetchTime;
        }
	}

	public Date getNextFetchTime() {
		synchronized (fetchingTimeLock) {
            //first time when system startup
            if (nextFetchTime == null) {
                Date now = new Date();
                currentFetchTime = new Date(now.getTime() - OFFSET_DURATION);
                nextFetchTime = new Date(now.getTime() +ConfigParameters.FETCH_DURATION);
            }
            return nextFetchTime;
        }
	}

	public Map<Long, MySessionDAO> getStartedSessions() {
		return startedSessions;
	}
	
}
