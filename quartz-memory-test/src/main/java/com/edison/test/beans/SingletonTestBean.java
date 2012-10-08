package com.edison.test.beans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.domain.Message;

@Singleton
@LocalBean
public class SingletonTestBean {

	private Logger LOGGER = LoggerFactory.getLogger(SingletonTestBean.class);
	
	private Map<Long, Message> map = new ConcurrentHashMap();

	private AtomicLong counter = new AtomicLong(0);
	
	public Map<Long, Message> getMap() {
		return map;
	}
	
	public AtomicLong getCounter() {
		return counter;
	}

	public void setCounter(AtomicLong counter) {
		this.counter = counter;
	}



	public void decrease(Message msg) {
		LOGGER.info("Remove message [{}].", msg.getId());

		if(!map.containsKey(msg.getId()))
		{
			LOGGER.error("No message [{}] exist!", msg.getId());
			return;
		}
		map.remove(msg.getId());
		
		LOGGER.info("After decreasing map size :[{}]", map.size());
	}
	
	public void increase(Message msg) {
		LOGGER.info("Add message [{}].", msg.getId());
		
		/*if(i==50)
		{
			this.map.clear();
			counter.set(0);
			i = 0;
			LOGGER.info("Reach maximum, reset!");
		}*/
		this.map.put(msg.getId(), msg);
		LOGGER.info("After increasing map size :[{}]", map.size());
	}
	
	
}
