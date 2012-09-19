package com.edison.test;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Before;

public abstract class OpenEJBTestBase{

	@Before
	public void initializeOpenEJB() throws NamingException {
		Context ctxt = null;
		try {
			
			Properties p = new Properties();
			p.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
		    p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
			ctxt = new InitialContext(p);
			ctxt.bind("inject", this);
		} 
		finally {
			try {
				ctxt.close();
			} 
			catch (NamingException ignored) {
				
			}
		}
	}
}
