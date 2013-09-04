package com.edison.test;

import com.edison.test.entity.SessionEntity;

public class AddSessionThread implements Runnable{

	@Override
	public void run() {
		SessionEntity se = new SessionEntity();
		se.setId(1);
		se.setData("s");
		for(int i=0;i<5000;i++)
		{
			SessionInfor si = new SessionInfor();
			si.getList().add(se);
			si.getLock().lock();
			System.out.println("Lock!!");
			System.out.println("Put value: ["+i+"].");
			SessionCache.getInstance().getMap().put(Long.valueOf(i), si);
			System.out.println("Map size: " + SessionCache.getInstance().getMap().size());
			
			RemoveSessionThread removeThread = new RemoveSessionThread();
			removeThread.setId(i);
			removeThread.setEntity(se);
			new Thread(removeThread).start();
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(SessionCache.getInstance().getMap().get(Long.valueOf(i))!=null)
			{
				System.out.println("Found entity [" + i + "]");
			}else
			{
				System.out.println("Opps~ Entity [" + i + "] is removed already!");
			}
			si.getLock().unlock();
			System.out.println("Unlock!!");
		}
	}
	
	public static void main(String[] args) {
		AddSessionThread at = new AddSessionThread();
		new Thread(at).start();
	}
}
