package com.edi.test.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.edi.test.BmscEventEntity;
import com.edi.test.BmscEventRetryTO;
import com.edi.test.ifc.BmscEventDbManager;
import com.edi.test.ifc.BmscEventDbManagerRemote;
import com.edi.test.ifc.Queries;

@Stateless
@Local(BmscEventDbManager.class)
@Remote(BmscEventDbManagerRemote.class)
public class BmscEventDbManagerImpl implements BmscEventDbManager {

    @PersistenceContext(unitName = "BmscEventDbManager")
    private EntityManager entityManager;

    @Override
    public BmscEventRetryTO createBmscEvent(BmscEventRetryTO bmscEventRetryTO) {
        if (bmscEventRetryTO == null) {
        	System.out.println("Can not create BmscEvent with null template.");
            return null;
        }
        //BmscLogger.eventInfo(BmscLogger.DB_MANAGER, "Trying to create BmscEvent");
        BmscEventEntity entity = new BmscEventEntity();
        entity.copy(bmscEventRetryTO);
        try {
            entityManager.persist(entity);
            System.out.println("Create BmscEvent '" + bmscEventRetryTO.getNotificationType().getValue() + ":" + bmscEventRetryTO.getDescription() + "' successfully, event ID " + entity.getId());
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }
        return entity.transform();
    }
    
    @Override
    public BmscEventRetryTO updateBmscEvent(BmscEventRetryTO bmscEventRetryTO) {
        if (bmscEventRetryTO == null) {
        	System.out.println("Can not update BmscEvent with null template.");
            return null;
        }
        //BmscLogger.eventInfo(BmscLogger.DB_MANAGER, "Trying to update BmscEvent(id=" + bmscEventRetryTO.getId() + ")");
        BmscEventEntity entity = new BmscEventEntity();
        entity.copy(bmscEventRetryTO);
        try {
            entityManager.merge(entity);
            System.out.println("Update BmscEvent with ID '" + bmscEventRetryTO.getId() + "' and type '" + bmscEventRetryTO.getNotificationType() + "' successfully.");
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }
        
        return entity.transform();
    }

    @Override
    public BmscEventRetryTO removeBmscEvent(Long id) {
        if (id == null) {
        	System.out.println("Can not remove BmscEvent, event ID " + id + ".");
            return null;
        }
        //BmscLogger.eventInfo(BmscLogger.DB_MANAGER, "Trying to remove BmscEvent(id = " + id + ")");

        BmscEventEntity event = null;
        
        try {
            event = this.entityManager.find(BmscEventEntity.class, id);
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }

        if (event == null) {
            return null;
        }
        
        try {
            entityManager.remove(event);
            System.out.println("Remove BmscEvent in DB successfully by object ID " + id + ", event '"+ event.getDescription() + "'");
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }
        
        return event.transform();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<BmscEventRetryTO> findEvents() {
        List<BmscEventRetryTO> list = new ArrayList<BmscEventRetryTO>();
        //BmscLogger.eventInfo(BmscLogger.DB_MANAGER, "Trying to find BmscEvents.");

        List<BmscEventEntity> resultList = new ArrayList<BmscEventEntity>();

        try {
            Query query = entityManager.createNamedQuery(Queries.FIND_BMSC_EVENT);
            resultList = query.getResultList();
        } catch (Exception e) {
        	e.printStackTrace();
            throw e;
        }

        if (resultList != null && resultList.size() > 0) {
            for (BmscEventEntity event : resultList) {
                list.add(event.transform());
            }
        }
        return list;
    }
}
