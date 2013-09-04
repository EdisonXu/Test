package com.edison.test.beans;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.NamingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.edison.test.OpenEJBTestContainer;

@ManagedBean
public class MessageNotifyerTest{
	
	@EJB
	MessageNotifyer notifyer;
	
	@Before
	public void bind()
	{
		try {
			Context ctxt = OpenEJBTestContainer.getInstance().getContext();
			if(ctxt!=null)
				ctxt.bind("inject", this);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testNotifyMessage() {
		/*Map<String, Object> map = new HashMap<String, Object>();
		map.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
		map.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
		final Context context = EJBContainer.createEJBContainer(map).getContext();*/
		
		try {
			//MessageNotifyer notifyer = (MessageNotifyer)context.lookup("java:global/message-client/MessageNotifyer");
			//context.bind("inject", this);
			notifyer.notifyMessage("Hello");
			
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.assertTrue(false);
			e.printStackTrace();
		}
	}

}
