/******************************************************************************
 * Copyright Ericsson AB 2011 This program may be used and/or copied only with
 * the prior written permission of Ericsson AB or in accordance with the terms
 * and conditions stipulated in the contract agreement under which the program
 * has been supplied.
 *****************************************************************************/
package com.edison.test;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 */
public class LocalTestClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
            properties.put(Context.PROVIDER_URL, "jnp://localhost:1099");
            properties.put(Context.OBJECT_FACTORIES, "org.jboss.naming:org.jnp.interfaces");
            InitialContext ctx = new InitialContext(properties);
            RemoteTest rt = (RemoteTest)ctx.lookup(RemoteTest.JNDI_NAME);
            System.out.println("RemoteTest.getTest: " + rt.getTest());
            
            RemoteEJBInjectSample res = (RemoteEJBInjectSample)ctx.lookup(RemoteEJBInjectSample.JNDI_NAME);
            System.out.println("RemoteEJBInjectSample.getValueByInjection: " + res.getValueByInjection());
            System.out.println("RemoteEJBInjectSample.getValueByJNDILookup: " + res.getValueByJNDILookup());
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

}
