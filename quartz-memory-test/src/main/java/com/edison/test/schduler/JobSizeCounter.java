package com.edison.test.schduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JobSizeCounter {

	private volatile static Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
	
	
	public synchronized static void increaseJobCounter(Object job)
	{
		Integer counter = map.get(job.getClass().getName());
		if(counter == null)
		{
			map.put(job.getClass().getName(), new Integer(0));
		}else
		{
			counter++;
			map.put(job.getClass().getName(), counter);
		}
		System.out.println("Current size of " + job + " is: "+counter);
	}
	
	public synchronized static void decreaseJobCounter(Object job)
	{
		Integer counter = map.get(job.getClass().getName());
		if(counter == null || counter ==0)
		{
			System.out.println("Count is 0 for job "+job);
		}else
		{
			counter--;
			map.put(job.getClass().getName(), counter);
		}
		System.out.println("Current size of " + job + " is: "+counter);
	}
}
