Mbean Creation Test

Prerequisite:

Create mbean implementing an interface named with ***Mbean, and deploy this mbean under Jboss.

Purpose:

Confirm whether it's OK when deploy the Mbean without jboss-service.xml

Conclusion:

If there's no jboss-service.xml, JBoss will not load this MBean automatically, which means we have to use
==============================================================================================================
server.registerMBean(new HelloWorldManager, new ObjectName("com.edison.test.mbean:service=HelloWorldManager"));
==============================================================================================================
to register this MBean manually.

