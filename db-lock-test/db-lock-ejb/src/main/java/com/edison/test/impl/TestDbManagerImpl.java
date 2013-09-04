package com.edison.test.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.TestDAO;
import com.edison.test.entity.TestEntity;
import com.edison.test.ifc.TestDbManager;

@Stateless
public class TestDbManagerImpl implements TestDbManager {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext(unitName = "TestDbManager")
    private EntityManager entityManager;
	
	@Override
	public TestDAO create(TestDAO input) {
		if(input==null)
		{
			log.error("Try to perisist a null test entity");
			return null;
		}
		
		TestEntity te = new TestEntity();
		te.setAttribute(input.getAttribute());
		
		entityManager.persist(te);
		return te.transform();
	}

	@Override
	public void update(TestDAO input) {
		if(input==null)
		{
			log.error("Try to update a null test entity");
			return;
		}
		TestEntity old = entityManager.find(TestEntity.class, input.getId());
		
		if(old==null)
		{
			log.error("No old entity found when trying to update");
			return;
		}
		old.setAttribute(input.getAttribute());
		entityManager.merge(old);
		
	}

	@Override
	public TestDAO remove(Long id) {
		if(id==null)
		{
			log.error("Try to update a null test entity");
			return null;
		}
		TestEntity old = entityManager.find(TestEntity.class, id);
		
		if(old==null)
		{
			log.error("No TestEntity found to remove!!!!");
			return null;
		}
		System.out.println("Remove " + old.getId()+" : "+old.getAttribute());
		//entityManager.remove(entityManager.merge(old));
		entityManager.remove(old);
		return old.transform();
	}

	@Override
	public TestDAO find(TestDAO input) {
		return entityManager.find(TestEntity.class, input.getId()).transform();
	}

	@Override
	public void lock(Long id) {
		System.out.println("lock entity: "+id);
		TestEntity old = entityManager.find(TestEntity.class, id, LockModeType.PESSIMISTIC_WRITE);
		//entityManager.lock(old, LockModeType.PESSIMISTIC_WRITE);
	}

}
