/**
 * 
 */
package com.ericsson.ecds.bcc.prov.common.ejb;

import java.util.List;

import com.ericsson.ecds.bcc.prov.common.data.BmscEventRetryTO;
import com.ericsson.ecds.bcc.prov.common.data.BmscEventTO;
import com.ericsson.ecds.bcc.prov.common.exception.ProvisioningException;

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
    
    public BmscEventRetryTO createBmscEvent(BmscEventRetryTO bmscEventRetryTO) throws ProvisioningException;

    public BmscEventRetryTO updateBmscEvent(BmscEventRetryTO bmscEventRetryTO) throws ProvisioningException;

    public BmscEventRetryTO removeBmscEvent(Long id) throws ProvisioningException;

    public List<BmscEventRetryTO> findEvents() throws ProvisioningException;
}
