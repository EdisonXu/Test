package com.edison.test;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class OpenEJBTestBase{

	protected static Context ctxt = null;
	
	@BeforeClass
	public static void initializeOpenEJB() throws NamingException {
		
		try {
			Properties p = new Properties();
			p.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
		    p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
			ctxt = new InitialContext(p);
		    //ctxt = EJBContainer.createEJBContainer().getContext();
		} 
		catch (Exception ignored) {
			
		}
	}
	
	@Before
	public void bind()
	{
		try {
			if(ctxt!=null)
				ctxt.bind("inject", this);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@AfterClass
	public static void destroy() throws Exception {
		try {
			if(ctxt!=null)
			{
				ctxt.close();
			}
		} catch(Exception ignore)
		{
			
		}
	}
}
