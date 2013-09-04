package com.edison.test.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.entity.TestEntity;
import com.edison.test.ifc.TestDbManager;

@Stateless
public class TestDbManagerImpl implements TestDbManager {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext(unitName = "TestDatabaseManager")
    private EntityManager entityManager;
	
	@Override
	public void create(TestEntity entity) {
		if(entity==null)
		{
			log.error("Try to perisist a null test entity");
			return;
		}
		
		entityManager.persist(entity);
	}

	@Override
	public void update(TestEntity entity) {
		if(entity==null)
		{
			log.error("Try to update a null test entity");
			return;
		}
		TestEntity old = entityManager.find(TestEntity.class, entity);
		
		if(old==null)
		{
			log.error("No old entity found when trying to update");
			return;
		}
		
		entityManager.merge(entity);
		
	}

	@Override
	public void remove(TestEntity entity) {
		if(entity==null)
		{
			log.error("Try to update a null test entity");
			return;
		}
		
		entityManager.remove(entity);
	}

	@Override
	public TestEntity find(TestEntity entity) {
		return entityManager.find(TestEntity.class, entity);
	}

}
