package com.edison.test.beans;

import javax.ejb.EJB;
import javax.naming.NamingException;

import org.apache.openejb.api.LocalClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.edison.test.OpenEJBTestBase;

@LocalClient
public class MessageNotifyerTest extends OpenEJBTestBase{
	
	@EJB
	MessageNotifyer notifyer;
	
	/*@Before
	public void bind()
	{
		try {
			//Context ctxt = OpenEJBContainer.getInstance().getContext();
			if(ctxt!=null)
				ctxt.bind("inject", this);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
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
