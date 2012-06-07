package com.edison.test;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Singleton;

/**
 *
 */
@Singleton(name = RemoteTest.NAME, mappedName=RemoteTest.JNDI_NAME)
@Local(RemoteTest.Local.class)
@Remote(RemoteTest.Remote.class)
public class RemoteTestBean implements RemoteTest {

    /* (non-Javadoc)
     * @see com.edison.test.RemoteTest#getTest()
     */
    @Override
    public String getTest() {
        return "Test";
    }

}
