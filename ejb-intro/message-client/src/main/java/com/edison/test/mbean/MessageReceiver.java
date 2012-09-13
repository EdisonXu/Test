package com.edison.test.mbean;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.beans.MessageNotifyer;

public class MessageReceiver implements MessageReceiverMBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class);
    
    private String msg;
    
    MessageNotifyer notifyer;
    
    @Override
    public String getMessage() {
        return msg;
    }

    @Override
    public void setMessage(String message) {
        this.msg = message;
    }

    @Override
    public String sendMessage() throws Exception {
        if(msg == null || msg.isEmpty())
        {
            return "Message input cannot be null";
        }
        initNotifyer();
        notifyer.notifyMessage(msg);
        return "Message " + msg + " is sent to server succesfully.";
    }

    @Override
    public String sendMessage(String message) throws Exception{
        if(message == null || message.isEmpty())
        {
            return "Message input cannot be null";
        }
        initNotifyer();
        notifyer.notifyMessage(message);
        return "Message " + message + " is sent to server succesfully.";
    }
    
    private void initNotifyer()
    {
        try {
            if(notifyer==null)
            {
                InitialContext ctx = new InitialContext();
                notifyer = (MessageNotifyer)ctx.lookup("java:global/MbeanTest/MessageNotifyer!com.test.beans.MessageNotifyer");
            }
        } catch (NamingException e) {
            LOGGER.error("Unable to find MessageNotifyer.", e);
        }
    }

    @Override
    public String deleteMessage(String message) {
        //TODO
        return null;
    }

}
