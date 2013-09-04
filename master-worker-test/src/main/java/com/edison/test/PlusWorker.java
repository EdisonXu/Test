package com.edison.test;

public class PlusWorker extends Worker{

	public Object handle(Object input)
	{
		Integer i = (Integer)input;
		try {
			Thread.sleep(MainTest.getCostOfTask());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i*i*i;
	}
}
