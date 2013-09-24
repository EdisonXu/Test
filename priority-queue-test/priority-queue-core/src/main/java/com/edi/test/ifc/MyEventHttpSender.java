/**
 * 
 */
package com.edi.test.ifc;

import com.edi.test.bean.MyEventTO;


public interface MyEventHttpSender {
	/**
	 * Local NAME
	 */
	public final static String NAME = "mdfcp/provisioning/BmscEventHttpSender/local";
	
	public static final String GLOBAL_PORTABLE_NAME = 
            "java:global/mdf-cp-restful-ws/mdf-cp-restful-ws-ejb/BmscEventSenderImpl!" + 
                    "com.edi.test.ifc.BmscEventHttpSender";

    public boolean send(MyEventTO event);
}
