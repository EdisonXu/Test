package com.edison.test.beans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.edison.test.domain.Message;

/**
 * Session Bean implementation class TestDbManager
 */
@Stateless
@LocalBean
public class MessageDbManager {

    @PersistenceContext(unitName = "TestDbManager")
    private EntityManager manager;

    public MessageEntity findById(Long id) {
        if (id == null)
            return null;
        return manager.find(MessageEntity.class, id);
    }

    public void update(Message newValue) {
        if (newValue == null)
            return;
        MessageEntity newEntity = new MessageEntity(newValue.getMessage());
        manager.merge(newEntity);
    }

    public void deleteById(Long id) {
        if (id == null)
            return;
        MessageEntity te = manager.find(MessageEntity.class, id);
        if (te == null)
            return;
        manager.remove(te);
    }
    
    public void create(Message message)
    {
    	if(message == null)
    	{
    		return;
    	}
    	
    	MessageEntity entity = new MessageEntity(message.getMessage());
        manager.persist(entity);
    }

}
