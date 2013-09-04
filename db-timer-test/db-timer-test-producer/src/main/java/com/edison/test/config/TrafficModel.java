package com.edison.test.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficModel {

	private final Logger LOGGER = LoggerFactory.getLogger(TrafficModel.class);
	
	/**
	 * Max session
	 */
	private int maxSession = 1000;
	
	// Interval between now and session start time
	private long waitTime = 1*300*1000; // 1min
	
	private long createDataInterval = 1000;
	
	private long duration = 300000;

	public int getMaxSession() {
		return maxSession;
	}

	public void setMaxSession(int maxSession) {
		this.maxSession = maxSession;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public long getCreateDataInterval() {
		return createDataInterval;
	}

	public void setCreateDataInterval(long createDataInterval) {
		this.createDataInterval = createDataInterval;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public TrafficModel()
	{
		super();
	}
	
	
	/**
	 * 
	 * @param waitTime
	 * @param createDataInterval
	 * @param duration
	 * @throws Exception
	 */
	public TrafficModel(long waitTime, long createDataInterval, long duration) throws Exception {
		super();
		this.waitTime = waitTime;
		this.createDataInterval = createDataInterval;
		this.duration = duration;
		this.validate();
	}

	/**
	 * 
	 * @param maxSession
	 * @param waitTime
	 * @param createDataInterval
	 * @param duration
	 * @throws Exception
	 */
	public TrafficModel(int maxSession, long waitTime, long createDataInterval,
			long duration) throws Exception {
		super();
		this.maxSession = maxSession;
		this.waitTime = waitTime;
		this.createDataInterval = createDataInterval;
		this.duration = duration;
		this.validate();
	}
	
	
	/**
	 * 
	 * @param createDataInterval
	 * @param duration
	 */
	public TrafficModel(long createDataInterval, long duration) {
		super();
		this.createDataInterval = createDataInterval;
		this.duration = duration;
	}

	public void print()
	{
		System.out.println("Max session: " + this.maxSession);
		System.out.println("Create data interval: " + this.createDataInterval);
		System.out.println("Session duration: " + this.duration);
		System.out.println("Session update times: " + ConfigParameters.UPDATE_TIMES);
		System.out.println("Session update interval: " + ConfigParameters.UPDATE_INTERVAL);
		System.out.println("Current load: " + this.duration/this.createDataInterval + " per second.");
	}
	
	public void validate() throws Exception
	{
		if(ConfigParameters.UPDATE_INTERVAL * ConfigParameters.UPDATE_TIMES > this.duration)
		{
			throw new Exception("Update times * update interval cannot be bigger than session duration!!!");
		}
	}
}
