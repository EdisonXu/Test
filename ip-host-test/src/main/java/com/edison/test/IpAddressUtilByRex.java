package com.edison.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class IpAddressUtilByRex {

    public static String VALID_HOST_REGEX = "^(?=.{1,255}$)[0-9A-Za-z](?:(?:[0-9A-Za-z]|\\b-)"
            + "{0,61}[0-9A-Za-z])?(?:\\.[0-9A-Za-z](?:(?:[0-9A-Za-z]|\\b-){0,61}[0-9A-Za-z])?)*\\.?$";

    public static String VALID_IPV6_REGEX = "^(((?=(?>.*?::)(?!.*::)))(::)?([0-9A-F]{1,4}::?)"
            + "{0,5}|([0-9A-F]{1,4}:){6})(\\2([0-9A-F]{1,4}(::?|$)){0,2}|((25[0-5]|(2[0-4]|1\\d|[1-9])?\\d)"
            + "(\\.|$)){4}|[0-9A-F]{1,4}:[0-9A-F]{1,4})(?<![^:]:|\\.)\\z";

    public static String VALID_IPV4_REGEX = "\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}\\b";
    
    public static boolean isSameHostName(String hostA, String hostB)
    {
        if(isHostName(hostA) && isHostName(hostB))
        {
            if(hostA.equalsIgnoreCase(hostB))
            {
                return true;
            }else
            {
                // try resolving with DNS
                try {
                    InetAddress addressA = InetAddress.getByName(hostA);
                    InetAddress addressB = InetAddress.getByName(hostB);
                    if(addressA.equals(addressB))
                    {
                        return true;
                    }else
                    {
                        return false;
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    public static boolean isHostName(String input)
    {
        Pattern hostPattern = Pattern.compile(VALID_HOST_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher hostMatcher = hostPattern.matcher(input);
        if(hostMatcher.find())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isIpV4Address(String ipAddress)
    {
        Pattern hostPattern = Pattern.compile(VALID_IPV4_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher hostMatcher = hostPattern.matcher(ipAddress);
        if(hostMatcher.find())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public static boolean isIpV6Address(String ipAddress)
    {
        Pattern hostPattern = Pattern.compile(VALID_IPV6_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher hostMatcher = hostPattern.matcher(ipAddress);
        if(hostMatcher.find())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
}
