package com.edison.test;

import java.util.Map;
import java.util.Queue;

public class Worker implements Runnable{

	protected Queue<Object> workQueue;
	
	protected Map<String, Object> resultMap;
	
	public Queue<Object> getWorkQueue() {
		return workQueue;
	}



	public void setWorkQueue(Queue<Object> workQueue) {
		this.workQueue = workQueue;
	}



	public Map<String, Object> getResultMap() {
		return resultMap;
	}



	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public Object handle(Object input)
	{
		return input;
	}
	
	@Override
	public void run() {
		while(true)
		{
			Object input = workQueue.poll();
			if(input==null) break;
			Object re = handle(input);
			resultMap.put(Integer.toString(input.hashCode()), re);
		}
	}

	
}
