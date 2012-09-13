package com.edison.test.beans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.domain.Message;

/**
 * Session Bean implementation class TestService
 */
@Stateless
@LocalBean
public class MessageHandler {

    /**
     * Logger for TestService.
     */
    private final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);
    
    @EJB
    MessageDbManager dbManager;
    
    /**
     * Default constructor.
     */
    public MessageHandler() {

    }

    /**
     * Handle message get from MDB and persist into db.
     * @param message
     */
    public void handleMessage(String message) {
        LOGGER.debug("Handle message: {}", message);
        Message msg = new Message(message);
        dbManager.create(msg);
        LOGGER.debug("Message {} persists successfully.", message);
    }
}
