package com.edison.test;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.edison.test.mbean.HelloWorldManager;
import com.edison.test.mbean.HelloWorldManagerMBean;

public abstract class OpenEJBTestBase{

	static Context ctxt = null;
	
	@BeforeClass
	public static void initializeOpenEJB() throws NamingException {
		
		try {
			Properties p = new Properties();
			p.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
			// p.put("openejb.user.mbeans.list", HelloWorldManagerMBean.MBEAN_NAME);
		    p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
		    //p.put("openejb.deployments.classpath.include", "file:///C:/Edison/WorkShop/github/Test/mbean-jboss-test/target/classes/.*");
			ctxt = new InitialContext(p);
		} 
		catch (NamingException ignored) {
			
		}
	}
	
	/*@Before
	public void bind()
	{
		try {
			if(ctxt!=null)
				ctxt.bind("inject", this);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
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
