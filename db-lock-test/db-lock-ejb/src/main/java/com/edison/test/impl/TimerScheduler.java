package com.edison.test.impl;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.edison.test.ifc.MyJob;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TimerScheduler {

	@Resource
    private TimerService timerService;
	
	public void schedule(Long time, MyJob job)
	{
		TimerConfig tc1 = new TimerConfig();
		tc1.setInfo(job);
		timerService.createSingleActionTimer(time, tc1);
	}
	
	@Timeout
	public void timeout(Timer timer)
	{
		MyJob job = (MyJob)timer.getInfo();
		job.execute();
	}
}
