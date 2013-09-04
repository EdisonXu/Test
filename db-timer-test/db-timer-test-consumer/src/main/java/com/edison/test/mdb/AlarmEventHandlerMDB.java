package com.edison.test.mdb;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines a JMS queue that can be used by all MTV applications to generate
 * events towards an external OAM connector.
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/TestEventsQueue")})
public class AlarmEventHandlerMDB implements MessageListener {

	Logger LOGGER = LoggerFactory.getLogger(AlarmEventHandlerMDB.class);
	
    @Resource
    private MessageDrivenContext mdc = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessage(Message inMessage) {
        try {
            if (inMessage instanceof ObjectMessage) {
                ObjectMessage msg = (ObjectMessage) inMessage;
                Serializable s = msg.getObject();

                LOGGER.debug(":onMessage MESSAGE BEAN: Message received: " + msg.getObject());
            } else {
            	
            }
        } catch (JMSException e) {
        	LOGGER.error("",e);
            mdc.setRollbackOnly();
        } catch (Exception e) {
        	LOGGER.error("",e);
            mdc.setRollbackOnly();
        }

    }
}
