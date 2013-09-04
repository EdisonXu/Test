package com.edison.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SessionCache {

	INSTANCE;
	
	public static SessionCache getInstance()
	{
		return INSTANCE;
	}
	
	private volatile Map<Long, SessionInfor> map = new ConcurrentHashMap<Long, SessionInfor>();
	
	public Map<Long, SessionInfor> getMap()
	{
		return map;
	}
	
	
	public synchronized void remove(Long id)
	{
		SessionInfor infor = map.get(id);
		
		if(infor == null)
		{
			System.out.println("No SessionInfor found!");
			return;
		}
		this.map.remove(id);
	}
}
