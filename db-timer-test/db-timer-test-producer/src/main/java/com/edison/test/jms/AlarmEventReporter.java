package com.edison.test.jms;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AlarmEventReporter {
	private static String destination = "java:/queue/TestEventsQueue";
    private QueueConnection queueConnection = null;
    private QueueSession queueSession = null;
    private Queue queue = null;
    private QueueSender publisher = null;
    private final Logger LOGGER = LoggerFactory.getLogger(AlarmEventReporter.class);
    
    public boolean send(String event) {
        if (event == null) {
           	LOGGER.error("Unable to handle empty event.");
            return false;
        }

        return sendMessage(event);
    }

    private boolean initQueue() {
        boolean result = false;
        InitialContext ctx = null;
        Queue queue = null;

        try {
            ctx = new InitialContext();

            QueueConnectionFactory cf = (QueueConnectionFactory) ctx.lookup("java:/ConnectionFactory");

            queue = (Queue) ctx.lookup(destination);

            try {
                if (queueConnection != null)
                    queueConnection.close();
                queueConnection = null;
            } catch (Exception ex) {
                LOGGER.warn("Closing queueConnection error.");
            }

            queueConnection = cf.createQueueConnection();

            queueSession = queueConnection.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);

            queueConnection.start();
            
            publisher = queueSession.createSender(queue);
            
            publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            result = true;
            
        } catch (Exception e) {
            LOGGER.error("Lookup ConnectionFactory error.", e);
            try {
                if (queueConnection != null)
                    queueConnection.close();
            } catch (JMSException ignored) {
            }
            queueConnection = null;
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                    LOGGER.error("Failed in closing context.", e);
                }
            }
        }
        return result;
    }

    @PreDestroy
    private void closeConnection() {
        try {
            if (queueConnection != null) {
                queueConnection.close();
            }
        } catch (JMSException jmse) {
            LOGGER.warn("Could not close connection.", jmse);
        }
    }
    
    private void finalizeConnection() {
        closeConnection();
        queueConnection = null;
    }

    private boolean reconnect() {
        finalizeConnection();
        return initQueue();
    }
    
    private boolean sendMessage(Serializable event) {

        if(queueConnection==null || queueSession==null || queue == null || publisher ==null)
        {
        	initQueue();
        }
        boolean sendResult = doSend(event);
        if (sendResult) {
            return true;
        }

        if (reconnect()) {
            return doSend(event);
        } else {
            LOGGER.error("No available message queue exists for processing the event");
        }
        return false;
    }
    
    private boolean doSend(Serializable event) {

        boolean sendResult = false;

        try {
            final ObjectMessage message = queueSession.createObjectMessage(event);
            publisher.send(message);
            sendResult = true;
            LOGGER.debug("Sending message successfully.");
        } catch (Exception ex) {
        	LOGGER.error("Failed to send message to queue", ex);
        }

        return sendResult;
    }

}
