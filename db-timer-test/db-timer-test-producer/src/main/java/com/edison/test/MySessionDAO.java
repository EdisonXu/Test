package com.edison.test;

import java.io.Serializable;
import java.util.Date;


public class MySessionDAO implements Serializable{

	private static final long serialVersionUID = -5777639843507431489L;
	private Long id;
	private Date startTime;
	private Date stopTime;
	private String attribute;
	private int updateTimes=1;
	private boolean started = false;
	
	public int getUpdateTimes() {
		return updateTimes;
	}

	public void setUpdateTimes(int updateTimes) {
		this.updateTimes = updateTimes;
	}

	public MySessionDAO(Date startTime, Date stopTime)
	{
		this.startTime = startTime;
		this.stopTime = stopTime;
	}
	
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
}
