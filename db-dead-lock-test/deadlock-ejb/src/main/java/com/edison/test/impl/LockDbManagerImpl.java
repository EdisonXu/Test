package com.edison.test.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import com.edison.test.entity.LockEntity;
import com.edison.test.entity.LockType;
import com.edison.test.ifc.LockDbManager;

@Stateless
public class LockDbManagerImpl implements LockDbManager {

	@PersistenceContext(unitName = "LockDatabaseManager")
    private EntityManager entityManager;
	
	
	@Override
	public void createLock() {
		LockEntity lock = new LockEntity();
		lock.setLockType(LockType.LOCK);
		lock.setLockVersion(0);
		
		entityManager.persist(lock);
	}

	@Override
	public void lock(LockType type) {
		entityManager.find(LockEntity.class, type, LockModeType.PESSIMISTIC_WRITE);
	}

}
