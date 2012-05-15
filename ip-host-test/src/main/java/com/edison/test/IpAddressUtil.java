package com.edison.test;

import sun.net.util.IPAddressUtil;

/**
 *
 */
public class IpAddressUtil {
    /**
     *  This method is to compare two host name
     *  to check if they are the same host without DNS resolving.
     *  If the host name is IP address string, e.g., '2001:1b70:82a9:6100::9'
     *  the comparison supports to compare different format of same IP address.
     *
     *  If the host name is just an host name instead of IP address,
     *  it just compare the String by using
     *  String.equals(...)
     *
     * @param hostA
     * @param hostB
     * @return
     */
    public static boolean ifSameHost(String hostA, String hostB) {

        boolean same = false;
        
        if(hostA==null && hostB==null)
        {
            return true;
        }

        byte[] addr1 = parseIpAddress(hostA);

        byte[] addr2 = parseIpAddress(hostB);

        //If this IP address string, compare the IP address
        if (addr1 != null && addr2 != null)
        {
            same = ifSameIpAddress(addr1, addr2);
        }


        //If this is host mame, just compare the host name string
        if (!same && hostA!=null)
            same = hostA.equals(hostB);

        return same;
    }

    /**
     * This function parse the IP address string as a byte array
     * If the IP address is IPv4 address, the byte array length is 4
     * If the IP address is IPv6 address, the byte array length is 16
     * If the host is not an IP address string, will return null;
     * @param ipAddress
     * @return if this is valid IPv4 address or IPv6 address, return byte array for the address
     * otherwise return null.
     * So, this method can be used to check if the ipAddress string is valid or not
     */
    public static byte[] parseIpAddress(String ipAddress)
    {
        if (ipAddress == null)
            return null;
        boolean ipv6Expected = false;
        if (ipAddress.charAt(0) == '[') {
            // This is supposed to be an IPv6 literal
            if (ipAddress.length() > 2 && ipAddress.charAt(ipAddress.length()-1) == ']') {
                ipAddress = ipAddress.substring(1, ipAddress.length() -1);
                ipv6Expected = true;
            } else {
                // This was supposed to be a IPv6 address, but it's not!
                //throw new UnknownHostException(host + ": invalid IPv6 address");
                return null;
            }
        }

        // if host is an IP address, we won't do further lookup
        if (Character.digit(ipAddress.charAt(0), 16) != -1
                || (ipAddress.charAt(0) == ':')) {
            byte[] addressBytes = null;
//            int numericZone = -1;
//            String ifname = null;

            // see if it is IPv4 address
            addressBytes = IPAddressUtil.textToNumericFormatV4(ipAddress);
            if (addressBytes == null) {
                // see if it is IPv6 address
                // Check if a numeric or string zone id is present
//                int pos;
//                if ((pos=ipAddress.indexOf ("%")) != -1) {
//                    numericZone = checkNumericZone (ipAddress);
//                    if (numericZone == -1) { /* remainder of string must be an ifname */
////                        ifname = ipAddress.substring (pos+1);
//                    }
//                }
                addressBytes = IPAddressUtil.textToNumericFormatV6(ipAddress);
            } else if (ipv6Expected) {
                // Means an IPv4 litteral between brackets!
                //throw new UnknownHostException("["+host+"]");
                return null;
            }
            return addressBytes;

        } else if (ipv6Expected) {
            // We were expecting an IPv6 Litteral, but got something else
            //throw new UnknownHostException("["+host+"]");
            return null;
        }

        return null;
    }

    private static int getInteger(byte[] addr)
    {
        int address = 0;
        if (addr != null)
        {
            if (addr.length == 4)
            {
                address = addr[3] & 0xFF;
                address |= ((addr[2] << 8) & 0xFF00);
                address |= ((addr[1] << 16) & 0xFF0000);
                address |= ((addr[0] << 24) & 0xFF000000);
            }
        }
        return address;
    }

    /**
     * Compare two IP address byte array, the length must be 4 or 16.
     * @param address1
     * @param address2
     * @return
     */
    private static boolean ifSameIpAddress(byte[] address1, byte[] address2)
    {
        if (address1 == null && address2 == null)
            return true;

        if (address1 != null && address2 != null
                && address1.length == address2.length)
        {
            //this IPv4 address
            if (address1.length == 4)
            {
                int a = getInteger(address1);
                int b = getInteger(address2);
                return a == b;
            }
            else
            {
                for (int i = 0; i < 16; i++) {
                    if (address1[i] != address2[i])
                        return false;
                }
                return true;
            }
        }

        return false;

    }

    /**
     * check if the literal address string has %nn appended
     * returns -1 if not, or the numeric value otherwise.
     *
     * %nn may also be a string that represents the displayName of
     * a currently available NetworkInterface.
     */
    private static int checkNumericZone (String s)
    {
        int percent = s.indexOf ('%');
        int slen = s.length();
        int digit, zone=0;
        if (percent == -1) {
            return -1;
        }
        for (int i=percent+1; i<slen; i++) {
            char c = s.charAt(i);
            if (c == ']') {
                if (i == percent+1) {
                    return -1;
                }
                break;
            }
            if ((digit = Character.digit (c, 10)) < 0) {
                return -1;
            }
            zone = (zone * 10) + digit;
        }
        return zone;
    }
    
    public static boolean isEmptyString(String input)
    {
        if(input == null || input.length()==0)
        {
            return true;
        }
        return false;
    }
}
