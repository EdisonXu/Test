package com.edison.test.beans;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

import junit.framework.Assert;

import org.junit.Test;

public class MessageNotifyerTest {

	
	@Test
	public void testNotifyMessage() {
		final Context context = EJBContainer.createEJBContainer().getContext();
		
		try {
			MessageNotifyer notifyer = (MessageNotifyer)context.lookup("java:global/message-client/MessageNotifyer");
			
			notifyer.notifyMessage("Hello");
			
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
			e.printStackTrace();
		}
	}

}
