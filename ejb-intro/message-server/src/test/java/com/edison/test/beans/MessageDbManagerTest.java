package com.edison.test.beans;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.naming.Context;
import javax.naming.NamingException;

import junit.framework.Assert;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import com.edison.test.OpenEJBTestContainer;
import com.edison.test.domain.Message;

@ManagedBean
public class MessageDbManagerTest{
	
	private static Context ctxt;
	
	@EJB
	MessageDbManager dbManager;
	
	@EJB
    private Caller transactionalCaller;
	
	@Before
	public void bind()
	{
		try {
			ctxt = OpenEJBTestContainer.getInstance().getContext();
			if(ctxt!=null)
				ctxt.bind("inject", this);
			System.out.println("!");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*protected void setUp() throws Exception {
        final Properties p = new Properties();
        p.put("java:/TestManagerDS", "new://Resource?type=DataSource");
        p.put("java:/TestManagerDS.hibernate.hbm2ddl.auto", "update");
        p.put("java:/TestManagerDS.hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        p.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");

        EJBContainer.createEJBContainer(p).getContext().bind("inject", this);
    }*/
	
/*	@Before
	public void init() throws NamingException {
		//
	}*/
	
	public void doWork() throws Exception {
		List<Message> list = dbManager.getAllMessages();
		Assert.assertEquals("List.size()",0,  list.size());
		
		dbManager.create(new Message("test"));
		dbManager.create(new Message("test2"));
		dbManager.create(new Message("test3"));
		
		list = dbManager.getAllMessages();
		
		Assert.assertEquals("List.size()",3,  list.size());
		
	}
	
	 @Test
	 public void testWithTransaction() throws Exception {
	        transactionalCaller.call(new Callable() {
	            public Object call() throws Exception {
	                doWork();
	                return null;
	            }
	        });
	    }
	
	public static interface Caller {
        public <V> V call(Callable<V> callable) throws Exception;
    }
	
	@Stateless
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public static class TransactionBean implements Caller {

        public <V> V call(Callable<V> callable) throws Exception {
            return callable.call();
        }
    }

}
