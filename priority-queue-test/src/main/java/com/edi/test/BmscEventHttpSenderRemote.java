package com.ericsson.ecds.bcc.prov.common.ejb;

import com.ericsson.ecds.bcc.prov.common.data.BmscEventTO;

public interface BmscEventHttpSenderRemote {

	/**
	 * Remote JNDI NAME
	 */
	public static final String JNDI_NAME = "mdfcp/provisioning/BmscEventHttpSender/remote";
	
	public static final String GLOBAL_PORTABLE_NAME = 
	        "java:global/mdf-cp-restful-ws/mdf-cp-restful-ws-ejb/BmscEventSenderImpl!" + 
	                "com.ericsson.ecds.bcc.prov.common.ejb.BmscEventHttpSenderRemote";

	public boolean send(BmscEventTO event);

}
