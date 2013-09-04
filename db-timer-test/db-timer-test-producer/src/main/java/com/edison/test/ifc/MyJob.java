package com.edison.test.ifc;

import java.io.Serializable;
import java.util.Date;

import com.edison.test.MySessionDAO;

public abstract class MyJob implements Serializable{

	protected Date targetDate;
	protected MySessionDAO session;
	
	public abstract void execute();
	
	public void setTargetDate(Date date)
	{
		this.targetDate = date;
	}
	
	public Date getTargetDate()
	{
		return targetDate;
	}

	public MySessionDAO getSession() {
		return session;
	}

	public void setSession(MySessionDAO session) {
		this.session = session;
	}
	
}
