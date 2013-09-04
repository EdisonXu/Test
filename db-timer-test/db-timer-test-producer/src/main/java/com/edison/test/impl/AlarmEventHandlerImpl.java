package com.edison.test.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.edison.test.ifc.AlarmEventHandlerRemote;
import com.edison.test.jms.AlarmEventReporter;

@Stateless
public class AlarmEventHandlerImpl implements AlarmEventHandlerRemote {

	@EJB
	AlarmEventReporter reporter;
	
	@Override
	public void clearAlarm(String message) {
		reporter.send(message);
	}

}
