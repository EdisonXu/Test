package com.edison.test.beans;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import com.edison.test.OpenEJBTestContainer;

@ManagedBean
public class MessageHandlerTest{

	//private static EJBContainer ejbContainer;
	private static Context ctxt;
	
	@EJB
	private MessageHandler handler;
	
	@Before
	public void bind()
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
