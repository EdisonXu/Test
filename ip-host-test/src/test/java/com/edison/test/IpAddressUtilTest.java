package com.edison.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class IpAddressUtilTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test for ifSameHost()
     */
    @Test
    public void testIfSameHost() {
        String hostname1 = "localhost";
        String hostname2 = "localhost";
        assertTrue(IpAddressUtil.ifSameHost(hostname1, hostname2));
        hostname2 = "test.com";
        assertFalse(IpAddressUtil.ifSameHost(hostname1, hostname2));
        
        String ipV4One = "127.0.0.1";
        String ipV4Two = "127.0.0.1";
        assertTrue(IpAddressUtil.ifSameHost(ipV4One, ipV4Two));
        ipV4Two = "192.168.0.1";
        assertFalse(IpAddressUtil.ifSameHost(ipV4One, ipV4Two));
        
        String ipV6One = "2001:DB8:2de:0:0:0:0:e13";
        String ipV6Two = "2001:DB8:2de::e13";
        assertTrue(IpAddressUtil.ifSameHost(ipV6One, ipV6Two));
        ipV6Two = "2001:1b70:82a9:6100::9";
        assertFalse(IpAddressUtil.ifSameHost(ipV6One, ipV6Two));
        
    }

}
