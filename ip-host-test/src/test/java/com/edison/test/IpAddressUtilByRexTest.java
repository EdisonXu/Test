package com.edison.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class IpAddressUtilByRexTest {

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

    @Test
    public void testIsSameHostName() {
        // nba.hupu.com and www.hoopchina.com point to the same web
        // they change the URL of the website but still support old one
        String hostNameA = "nba.hupu.com";
        String hostNameB = "www.hoopchina.com";
        assertTrue(IpAddressUtilByRex.isSameHostName(hostNameA, hostNameB));
        hostNameA = "fsdf&f*";
        assertFalse(IpAddressUtilByRex.isSameHostName(hostNameA, hostNameB));
    }
    
    @Test
    public void testIsIpV4Address() {
        String ipAddress = "127.0.0.1";
        assertTrue(IpAddressUtilByRex.isIpV4Address(ipAddress));
        ipAddress = "1.1.1";
        assertFalse(IpAddressUtilByRex.isIpV4Address(ipAddress));
    }
    
    @Test
    public void testIsIp64Address() {
        String ipAddress = "2001:DB8:2de:0:0:0:0:e13";
        assertTrue(IpAddressUtilByRex.isIpV6Address(ipAddress));
        
        ipAddress = "2001:DB8:2de::e13";
        assertTrue(IpAddressUtilByRex.isIpV6Address(ipAddress));
        
        ipAddress = "::01";
        assertTrue(IpAddressUtilByRex.isIpV6Address(ipAddress));
        
        ipAddress = "2001:DB8:2de:::e13";
        assertFalse(IpAddressUtilByRex.isIpV6Address(ipAddress));
    }

}
