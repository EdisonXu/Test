package com.edison.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.edison.test.entity.SessionEntity;

public class SessionInfor {

	private List<SessionEntity> list;

	private volatile Lock lock = new ReentrantLock();
	
	public Lock getLock() {
		return lock;
	}
	
	public List<SessionEntity> getList() {
		if(list==null || list.size()==0)
		{
			list = new ArrayList<SessionEntity>();
		}
		return list;
	}

	public void setList(List<SessionEntity> list) {
		this.list = list;
	}
	
	public void remove(SessionEntity entity)
	{
		if(list.contains(entity))
		{
			list.remove(entity);
			System.out.println("Remove entity [" + entity.getId() + "]");
		}else
		{
			System.out.println("No entity [" + entity.getId() + "] found!!!");
		}
	}
}
