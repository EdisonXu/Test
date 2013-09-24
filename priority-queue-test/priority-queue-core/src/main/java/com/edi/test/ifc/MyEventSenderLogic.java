/**
 * 
 */
package com.edi.test.ifc;

import java.util.List;

import com.edi.test.bean.MyEventRetryTO;
import com.edi.test.bean.MyEventTO;

/**
 * myEventSender
 * 
 */
public interface MyEventSenderLogic{

	public final static String GLOBAL_JNDI_NAME = "java:global/mdf-cp-core/prov-business-logic-ejb/MyEventSenderLogicImpl!" +
            "com.ericsson.ecds.bcc.prov.common.ejb.MyEventSenderLogic";

    /** Used to receive event from internal logic */
    public void send(MyEventTO event);
    
    public MyEventRetryTO createMyEvent(MyEventRetryTO myEventRetryTO) throws Exception;

    public MyEventRetryTO updateMyEvent(MyEventRetryTO myEventRetryTO) throws Exception;

    public MyEventRetryTO removeMyEvent(Long id) throws Exception;

    public List<MyEventRetryTO> findEvents() throws Exception;
}
