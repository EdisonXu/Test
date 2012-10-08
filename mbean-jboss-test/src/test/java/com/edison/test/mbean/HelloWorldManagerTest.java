package com.edison.test.mbean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.JMX;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import junit.framework.Assert;

import org.apache.openejb.api.LocalClient;
import org.junit.Before;
import org.junit.Test;

import com.edison.test.OpenEJBTestBase;

@LocalClient
public class HelloWorldManagerTest extends OpenEJBTestBase {

	protected static MBeanServer server;
	protected static ObjectName mbeanName;
	protected static HelloWorldManager manager;
	
	@Before
	public void init() throws Exception{
		server = ManagementFactory.getPlatformMBeanServer();
		mbeanName = new ObjectName(HelloWorldManagerMBean.MBEAN_NAME);
		manager = new HelloWorldManager();
		server.registerMBean(manager, mbeanName);
	}

	@Test
	public void test() throws Exception {
		
		//Assert.assertTrue(server.isRegistered(mbeanName));
		
		MBeanInfo infos = server.getMBeanInfo(mbeanName);
		
		infos.getAttributes();
		
		infos.getOperations();
		
		Assert.assertEquals("Hello world!", server.getAttribute(mbeanName, "Msg"));
		server.setAttribute(mbeanName, new Attribute("Msg", "test"));
		Assert.assertEquals("test", server.getAttribute(mbeanName, "Msg"));
		
		server.setAttribute(mbeanName, new Attribute("Msg", "hi"));
		
		Assert.assertEquals("hi test!", server.invoke(mbeanName, "printMsg", null, null));
		
		
	}

}
