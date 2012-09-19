package com.edison.test.beans;

import java.util.List;
import java.util.concurrent.Callable;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import junit.framework.Assert;

import org.apache.openejb.api.LocalClient;
import org.junit.Test;

import com.edison.test.OpenEJBTestBase;
import com.edison.test.domain.Message;

@LocalClient
public class MessageDbManagerTest extends OpenEJBTestBase{
	
	@EJB
	MessageDbManager dbManager;
	
	@EJB
    private Caller transactionalCaller;
	
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
