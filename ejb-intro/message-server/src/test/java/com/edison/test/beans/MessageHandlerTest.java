package com.edison.test.beans;

import javax.ejb.EJB;

import junit.framework.Assert;

import org.apache.openejb.api.LocalClient;
import org.junit.Test;

import com.edison.test.OpenEJBTestBase;

@LocalClient
public class MessageHandlerTest extends OpenEJBTestBase{

	//private static EJBContainer ejbContainer;
	
	@EJB
	private MessageHandler handler;
	
	/*@BeforeClass
	public static void start() {
		ejbContainer = EJBContainer.createEJBContainer();
	}*/
	
	/*@Before
	public void lookupABean() throws NamingException{
		Object obj = ejbContainer.getContext().lookup("java:global/message-server/MessageHandler");
		
		Assert.assertTrue(obj instanceof MessageHandler);
		
		handler = (MessageHandler)obj;
	}*/
	
	/*@AfterClass
	public static void stopContainer()
	{
		if(ejbContainer !=null)
		{
			ejbContainer.close();
		}
	}*/
	
	@Test
	public void testHandleMessage() {
		try {
			handler.handleMessage("Hello");
			
			Assert.assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}
	}

}
