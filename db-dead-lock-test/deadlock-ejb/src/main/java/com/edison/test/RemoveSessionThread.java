package com.edison.test;

import java.util.Date;

import com.edison.test.entity.SessionEntity;

public class RemoveSessionThread implements Runnable{

	private long id;
	private SessionEntity entity;
	
	public SessionEntity getEntity() {
		return entity;
	}

	public void setEntity(SessionEntity entity) {
		this.entity = entity;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void run() {
		SessionInfor infor = SessionCache.getInstance().getMap().get(id);
		try {
			if(infor==null)
			{
				System.out.println("No infor found to remove [" + id + "]");
				return;
			}
			infor.getLock().lock();
			System.out.println("Lock to remove");
			infor.remove(entity);
			SessionCache.getInstance().remove(Long.valueOf(id));
			System.out.println(new Date() + " Remove entity ["+id + "] from cache");
		} finally {
			System.out.println("Unlock to remove");
			if(infor!=null)
				infor.getLock().unlock();
		}
		
	}

}
