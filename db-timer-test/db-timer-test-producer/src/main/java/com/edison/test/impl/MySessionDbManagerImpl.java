package com.edison.test.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edison.test.AlarmHelper;
import com.edison.test.MySessionDAO;
import com.edison.test.entity.MySessionEntity;
import com.edison.test.ifc.MySessionDbManager;
import com.edison.test.ifc.Queries;

@Stateless
public class MySessionDbManagerImpl implements MySessionDbManager {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext(unitName = "MySessionDatabaseManager")
    private EntityManager entityManager;
	
	@Override
	public MySessionDAO create(MySessionDAO input) {
		if(input==null)
		{
			log.error("Try to perisist a null test entity");
			return null;
		}
		
		MySessionEntity te = new MySessionEntity();
		te.copy(input);
		te.setCreateTime(new Date());
		entityManager.persist(te);
		AlarmHelper.clearAlarm();
		return te.transform();
	}

	@Override
	public void update(MySessionDAO input) {
		if(input==null)
		{
			log.error("Try to update a null test entity");
			return;
		}
		MySessionEntity old = entityManager.find(MySessionEntity.class, input.getId());
		
		if(old==null)
		{
			log.error("No old entity found when trying to update");
			return;
		}
		old.copy(input);
		entityManager.merge(old);
		AlarmHelper.clearAlarm();
	}

	@Override
	public MySessionDAO remove(Long id) {
		if(id==null)
		{
			log.error("Try to update a null test entity");
			return null;
		}
		MySessionEntity old = entityManager.find(MySessionEntity.class, id);
		
		if(old==null)
		{
			log.error("No TestEntity found to remove!!!!");
			return null;
		}
		System.out.println("Remove " + old.getId()+" : "+old.getAttribute());
		//entityManager.remove(entityManager.merge(old));
		entityManager.remove(old);
		AlarmHelper.clearAlarm();
		return old.transform();
	}

	@Override
	public MySessionDAO find(MySessionDAO input) {
		AlarmHelper.clearAlarm();
		return entityManager.find(MySessionEntity.class, input.getId()).transform();
	}

	@Override
	public void lock(Long id) {
		System.out.println("lock entity: "+id);
		MySessionEntity old = entityManager.find(MySessionEntity.class, id, LockModeType.PESSIMISTIC_WRITE);
		//entityManager.lock(old, LockModeType.PESSIMISTIC_WRITE);
	}

	@Override
	public List<MySessionDAO> findSessionByDuration(Date begin, Date end) {
		String sql = Queries.READ_SESSION_BY_DURATION;
		Query query = entityManager.createNamedQuery(sql);
		query.setParameter("durationBegin", begin);
        query.setParameter("durationEnd", end);
        List<MySessionDAO> result = new ArrayList<MySessionDAO>();
        List list = query.getResultList();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
            	result.add(((MySessionEntity)list.get(i)).transform());
            }
        }
        AlarmHelper.clearAlarm();
		return result;
	}

}
