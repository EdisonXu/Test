package com.edison.test.beans;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message-Driven Bean implementation class for: TestMdb
 * 
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/TestQueue") })
public class MessageServerMdb implements MessageListener {

    /**
     * Logger for TestMdb
     */
    private final Logger LOGGER = LoggerFactory.getLogger(MessageServerMdb.class);

    @EJB
    MessageHandler tService;

    /**
     * Default constructor.
     */
    public MessageServerMdb() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        if(!(message instanceof TextMessage))
        {
            LOGGER.error("Invalid message received from queue.");
            return;
        }
        
        try {
            String msg = ((TextMessage) message).getText();
            if (msg == null) {
                LOGGER.error("No message found!");
                return;
            }
            LOGGER.info("Message received : {}", msg);
            tService.handleMessage(msg);
        } catch (JMSException e) {
            LOGGER.error("Error happen when handling message in queue.", e);
        }
    }

}
