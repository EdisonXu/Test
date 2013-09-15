/**
 * 
 */
package com.edi.test.ifc;

import java.util.List;

import com.edi.test.BmscEventRetryTO;
import com.edi.test.BmscEventTO;

/**
 * BmscEventSenderRemote
 * 
 */
public interface BmscEventSenderRemote {
    /**
     * JNDI_NAME
     */
    public final static String JNDI_NAME = "mdfcp/provisioning/BmscEventSender/remote";
	
	public final static String GLOBAL_JNDI_NAME = "java:global/mdf-cp-core/prov-business-logic-ejb/BmscEventSenderLogicImpl!" +
			"com.ericsson.ecds.bcc.prov.common.ejb.BmscEventSenderRemote";

    /** Used to receive event from internal logic */
    public void send(BmscEventTO event);
    
    public BmscEventRetryTO createBmscEvent(BmscEventRetryTO bmscEventRetryTO) throws Exception;

    public BmscEventRetryTO updateBmscEvent(BmscEventRetryTO bmscEventRetryTO) throws Exception;

    public BmscEventRetryTO removeBmscEvent(Long id) throws Exception;

    public List<BmscEventRetryTO> findEvents() throws Exception;
}
