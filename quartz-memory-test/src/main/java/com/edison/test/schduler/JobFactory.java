package com.edison.test.schduler;

import com.edison.test.beans.SingletonTestBean;
import com.edison.test.domain.Message;

public class JobFactory {

	public static IncreaseJob getIncreaseJob(Message msg, SingletonTestBean stb) {
		return new IncreaseJob();
	}
	
	public static DecreaseJob getDecreaseJob(Message msg, SingletonTestBean stb) {
		return new DecreaseJob();
	}
	
	public static RequestMonitorJob getRequestMonitorJob(SingletonTestBean stb) {
		return new RequestMonitorJob();
	}
	
	public static EmptyJob getEmptyJob() {
		return new EmptyJob();
	}
	
}
