package com.edison.test;

import java.util.Properties;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;

public class OpenEJBTestContainer{

	//protected static Context ctxt = null;
	
	private static EJBContainer instance = null;
	
	private OpenEJBTestContainer(){}
	
	public static EJBContainer getInstance()
	{
		if(instance == null)
		{
			System.out.println("Create new container!");
			Properties p = new Properties();
			p.put("java:/TestManagerDS","new://Resource?type=DataSource");
			p.put("java:/TestManagerDS.hibernate.hbm2ddl.auto","update");
			p.put("java:/TestManagerDS.hibernate.dialect","org.hibernate.dialect.Oracle10gDialect");
			p.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
		    p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
			instance = EJBContainer.createEJBContainer(p);
		}
		return instance;
	}
	
/*	public static void initializeOpenEJB() throws NamingException {
		
		try {
			Properties p = new Properties();
			p.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
		    p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.client.LocalInitialContextFactory");
			ctxt = new InitialContext(p);
		    //ctxt = EJBContainer.createEJBContainer().getContext();
		} 
		catch (Exception ignored) {
			
		}
	}*/
	
/*	@Before
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
	
/*	
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
	}*/
}
