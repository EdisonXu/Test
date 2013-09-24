package com.edi.test.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.edi.test.bean.MyEventRetryTO;
import com.edi.test.ifc.MyEventDbManager;
import com.edi.test.ifc.Queries;
import com.edi.test.persist.MyEventEntity;

@Stateless
public class MyEventDbManagerImpl implements MyEventDbManager {

    @PersistenceContext(unitName = "MyEventDbManager")
    private EntityManager entityManager;

    @Override
    public MyEventRetryTO persistMyEvent(MyEventRetryTO myEventRetryTO) {
        if (myEventRetryTO == null) {
        	System.out.println("Can not create MyEvent with null template.");
            return null;
        }
        MyEventEntity entity = new MyEventEntity();
        entity.copy(myEventRetryTO);
        try {
            entityManager.persist(entity);
            System.out.println("Create MyEvent '" + myEventRetryTO.getNotificationType().getValue() + ":" + myEventRetryTO.getDescription() + "' successfully, event ID " + entity.getId());
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }
        return entity.transform();
    }
    
    @Override
    public MyEventRetryTO updateMyEvent(MyEventRetryTO myEventRetryTO) {
        if (myEventRetryTO == null) {
        	System.out.println("Can not update MyEvent with null template.");
            return null;
        }
        MyEventEntity entity = new MyEventEntity();
        entity.copy(myEventRetryTO);
        try {
            entityManager.merge(entity);
            System.out.println("Update MyEvent with ID '" + myEventRetryTO.getId() + "' and type '" + myEventRetryTO.getNotificationType() + "' successfully.");
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }
        
        return entity.transform();
    }

    @Override
    public MyEventRetryTO removeMyEvent(Long id) {
        if (id == null) {
        	System.out.println("Can not remove MyEvent, event ID " + id + ".");
            return null;
        }

        MyEventEntity event = null;
        
        try {
            event = this.entityManager.find(MyEventEntity.class, id);
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }

        if (event == null) {
            return null;
        }
        
        try {
            entityManager.remove(event);
            System.out.println("Remove MyEvent in DB successfully by object ID " + id + ", event '"+ event.getDescription() + "'");
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }
        
        return event.transform();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MyEventRetryTO> findEvents() {
        List<MyEventRetryTO> list = new ArrayList<MyEventRetryTO>();

        List<MyEventEntity> resultList = new ArrayList<MyEventEntity>();

        try {
            Query query = entityManager.createNamedQuery(Queries.FIND_MY_EVENT);
            resultList = query.getResultList();
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }

        if (resultList != null && resultList.size() > 0) {
            for (MyEventEntity event : resultList) {
                list.add(event.transform());
            }
        }
        return list;
    }
}
