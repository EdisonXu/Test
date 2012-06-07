package com.edison.test;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 */
@Stateless(name = RemoteEJBInjectSample.NAME, mappedName = RemoteEJBInjectSampleBean.JNDI_NAME)
@Local(RemoteEJBInjectSample.Local.class)
@Remote(RemoteEJBInjectSample.Remote.class)
public class RemoteEJBInjectSampleBean implements RemoteEJBInjectSample {

    @EJB
    RemoteTest.Local test;

    @Override
    public String getValueByInjection() {
        return test.getTest();
    }

    @Override
    public String getValueByJNDILookup() {
        try {
            InitialContext ctx = new InitialContext();
            RemoteTest rTest = (RemoteTest)ctx.lookup(RemoteTest.JNDI_NAME);
            return rTest.getTest();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
}
