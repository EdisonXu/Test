package com.edison.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Master {

	// Work queue
	protected Queue<Object> workQueue = new ConcurrentLinkedQueue<Object>();
	
	// Worker queue
	protected Map<String, Thread> threadMap = new HashMap<>();
	
	// Result set of worker
	protected Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();
	
	public boolean isComplete()
	{
//		for(Map.Entry<String, Thread> entry:threadMap.entrySet())
//		{
//			if(entry.getValue().getState()!=Thread.State.TERMINATED)
//				return false;
//		}
		for(Thread thread:threadMap.values())
		{
			if(thread.getState()!=Thread.State.TERMINATED)
				return false;
		}
		return true;
	}
	
	public Master(Worker worker, int countWorker)
	{
		worker.setWorkQueue(workQueue);
		worker.setResultMap(resultMap);
		for(int i=0;i<countWorker;i++)
		{
			threadMap.put(Integer.toString(i), new Thread(worker, Integer.toString(i)));
		}
	}
	
	public void submit(Object job)
	{
		workQueue.add(job);
	}

	public Queue<Object> getWorkQueue() {
		return workQueue;
	}

	public void setWorkQueue(Queue<Object> workQueue) {
		this.workQueue = workQueue;
	}

	public Map<String, Thread> getThreadMap() {
		return threadMap;
	}

	public void setThreadMap(Map<String, Thread> threadMap) {
		this.threadMap = threadMap;
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}
	
	public void execute()
	{
		for(Thread thread:threadMap.values())
		{
			thread.start();
		}
	}
}
