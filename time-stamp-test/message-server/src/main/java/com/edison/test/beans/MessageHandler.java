package com.edison.test.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.domain.Message;

/**
 * Session Bean implementation class TestService
 */
@Startup
@Singleton
@LocalBean
public class MessageHandler {

    /**
     * Logger for TestService.
     */
    private final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);
    
    @EJB
    MessageDbManager dbManager;
    
    @Resource
    private TimerService timerService;
    
    /**
     * Default constructor.
     */
    public MessageHandler() {

    }
    
    @PostConstruct
    public void test()
    {
    	handleMessage("Test");
    }

    /**
     * Handle message get from MDB and persist into db.
     * @param message
     * @throws ParseException 
     */
    public void handleMessage(String message){
        LOGGER.debug("Handle message: {}", message);
        Message msg = new Message(message);
        try {
			Date target = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2013-04-23 10:02:44.65");
			msg.setCreateDate(new Date(target.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        MessageEntity entity = dbManager.create(msg);
        LOGGER.info("Create Date: " + entity.getCreateTime());
        LOGGER.info("In TimeStamp: "  + entity.getCreateTime().getTime());
        LOGGER.debug("Message {} persists successfully.", message);
        
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerConfig.setInfo(entity);
        timerService.createSingleActionTimer(2000l, timerConfig);
    }
    
    @Timeout
    public void handleTimeout(Timer timer)
    {
    	MessageEntity old= (MessageEntity)timer.getInfo();
    	MessageEntity entity = dbManager.findById(old.getId());
    	LOGGER.info("Date is: " + entity.getCreateTime());
    	LOGGER.info("In TimeStamp: " + entity.getCreateTime().getTime());
    }
}
