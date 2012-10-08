package com.edison.test.beans;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.edison.test.OpenEJBTestContainer;

@ManagedBean
public class MessageServerMdbTest{

	@Resource
	private ConnectionFactory connectionFactory;
	
	@Resource(mappedName = "queue/TestQueue")
	private Queue queue;
	
	private static Context ctxt;
	
	@Before
	public void init()
	{
		try {
			ctxt = OpenEJBTestContainer.getInstance().getContext();
			if(ctxt!=null)
				ctxt.bind("inject", this);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOnMessage() {
		try {
			//EJBContainer.createEJBContainer().getContext().bind("inject", this);
			
			final QueueConnection connection = ((QueueConnectionFactory) connectionFactory).createQueueConnection();
			
			connection.start();
			
			final QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			
			
            QueueSender publisher = session.createSender(queue);
            publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            this.sendMessage("Hello", publisher, session);
            
            Assert.assertTrue(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.assertFalse(true);
			e.printStackTrace();
		}
	}
	
	private void sendMessage(String message, QueueSender sender, Session session) throws JMSException {
		final TextMessage tMessage = session.createTextMessage(message);
		sender.send(tMessage);
	}

}
