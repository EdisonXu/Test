package com.edison.test;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class MainTest {

	private static int costOfTask = 0;
	
	public static int getCostOfTask() {
		return costOfTask;
	}

	public static void setCostOfTask(int costOfTask) {
		MainTest.costOfTask = costOfTask;
	}

	public static void testMaster()
	{
		Date begin = new Date();
		Master m = new Master (new PlusWorker(), 5);
		for(int i=0;i<100;i++)
		{
			m.submit(i);
		}
		
		m.execute();
		int re=0;
		Map<String, Object> resultMap = m.getResultMap();
		while(resultMap.size()>0 || !m.isComplete())
		{
			Set<String> keys = resultMap.keySet();
			String key = null;
			for(String k:keys)
			{
				key = k;
				break;
			}
			Integer i = null;
			if(key!=null)
				i = (Integer)resultMap.get(key);
			if(i!=null)
				re+=i;
			if(key!=null)
				resultMap.remove(key);
		}
		Date end = new Date();
		System.out.println("Master-worker result: " + re);
		System.out.println("Cost:" + (end.getTime()-begin.getTime()));
	}
	
	public static void testNormal()
	{
		Date begin = new Date();
		int re = 0;
		for(int i =0;i<100;i++)
		{
			re += i*i*i;
			try {
				Thread.sleep(MainTest.getCostOfTask());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Date end = new Date();
		System.out.println("Normal result: "+ re);
		System.out.println("Cost:" + (end.getTime()-begin.getTime()));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//case 1: cost of task is 0
		MainTest.setCostOfTask(0);
		testMaster();
		testNormal();
		//case 2: cost of task is 100
		MainTest.setCostOfTask(100);
		testMaster();
		testNormal();
	}

}
