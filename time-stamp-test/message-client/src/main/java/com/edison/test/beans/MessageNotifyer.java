package com.edison.test.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stateless bean who sends message received to Message-Server.
 */
@Singleton
@Startup
public class MessageNotifyer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageNotifyer.class);
    // private static String destinationName = "quque/TestQueue";

    @Resource(mappedName = "ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/queue/TestQueue")
    private Queue queue;

    private QueueConnection connection = null;
    private QueueSession session = null;

    public void notifyMessage(String message) {
        if (message != null)
            sendMessage(message);
    }

    @PostConstruct
    private void initQueue() {
        try {
            closeConnection();
            connection = ((QueueConnectionFactory) connectionFactory).createQueueConnection();
            session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            connection.start();
        } catch (Exception e) {
            LOGGER.error("Failed to init JMS queue.", e);
            closeConnection();
        }
    }

    /**
     * 
     */
    @PreDestroy
    private void closeConnection() {
        try {
            if (session != null) {
                session.close();
            }
        } catch (Exception e) {
            LOGGER.warn("Closing queue session error.", e);
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            LOGGER.warn("Closing queue connection error.", e);
        }
        session = null;
        connection = null;
    }

    private void sendMessage(String message) {
        try {
            LOGGER.debug("Send message: {}", message);
            if (connection == null || session == null) {
                initQueue();
            }
            if (connection != null && session != null) {
                final TextMessage tMessage = session.createTextMessage(message);
                QueueSender publisher = session.createSender(queue);
                publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                publisher.send(tMessage);
            } else {
                LOGGER.error("No available message queue.");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to send message to queue.", e);
        }
    }
}
