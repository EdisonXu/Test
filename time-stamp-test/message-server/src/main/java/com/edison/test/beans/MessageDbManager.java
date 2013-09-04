package com.edison.test.beans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
        MessageEntity newEntity = new MessageEntity(newValue.getMessage(), newValue.getCreateDate());
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
    
    public MessageEntity create(Message message)
    {
    	if(message == null)
    	{
    		return null;
    	}
    	
    	MessageEntity entity = new MessageEntity(message.getMessage(), message.getCreateDate());
        manager.persist(entity);
        return entity;
    }
    
    public List<Message> getAllMessages() {
    	List<Message> result = new ArrayList<>();
    	Query q = manager.createQuery("select e from MessageEntity e");

    	@SuppressWarnings("unchecked")
		List<MessageEntity> list = q.getResultList();
    	for(MessageEntity e:list)
    	{
    		result.add(new Message(e.getMessage()));
    	}
    	return result;
    	
    }

}
