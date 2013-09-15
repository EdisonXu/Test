package com.ericsson.ecds.bcc.prov.dbmgr;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.ericsson.bmsc.oam.logging.BmscLogger;
import com.ericsson.ecds.bcc.prov.common.data.BmscEventRetryTO;
import com.ericsson.ecds.bcc.prov.common.dbmgr.BmscEventDbManager;
import com.ericsson.ecds.bcc.prov.common.dbmgr.BmscEventDbManagerRemote;
import com.ericsson.ecds.bcc.prov.data.BmscEventEntity;
import com.ericsson.ecds.bcc.prov.data.Queries;
import com.ericsson.ecds.bcc.prov.util.ProvisionDBAlarmHelper;
import com.ericsson.bmsc.common.constant.ErrorCode;

@Stateless
@Local(BmscEventDbManager.class)
@Remote(BmscEventDbManagerRemote.class)
public class BmscEventDbManagerImpl implements BmscEventDbManager {

    @PersistenceContext(unitName = "BmscEventDbManager")
    private EntityManager entityManager;

    @Override
    public BmscEventRetryTO createBmscEvent(BmscEventRetryTO bmscEventRetryTO) {
        if (bmscEventRetryTO == null) {
            BmscLogger.eventWarn(BmscLogger.DB_MANAGER, "Can not create BmscEvent with null template.");
            return null;
        }
        //BmscLogger.eventInfo(BmscLogger.DB_MANAGER, "Trying to create BmscEvent");
        BmscEventEntity entity = new BmscEventEntity();
        entity.copy(bmscEventRetryTO);
        try {
            entityManager.persist(entity);
            BmscLogger.eventInfo(BmscLogger.DB_MANAGER, 
                    "Create BmscEvent '" + bmscEventRetryTO.getNotificationType().getValue() + ":" + bmscEventRetryTO.getDescription() + "' successfully, event ID " + entity.getId()); 
        } catch (Exception e) {
        	BmscLogger.eventError(BmscLogger.DB_MANAGER, ErrorCode.DATABASE_CREATE_FAILED, "BmscEvent", "with ID '" + bmscEventRetryTO.getId() + "' and type '" + bmscEventRetryTO.getNotificationType() + "'", e.getMessage());
            //BmscLogger.eventError(BmscLogger.DB_MANAGER, "Failed to create BmscEvent because of DB error.");
            BmscLogger.eventDebug(BmscLogger.DB_MANAGER, "BmscEventDbManagerImpl:createBmscEvent", e);
            // raise alarm
            ProvisionDBAlarmHelper.raiseAlarm(e);
            throw e;
        }

        // clear alarm
        ProvisionDBAlarmHelper.clearAlarm();

        return entity.transform();
    }
    
    @Override
    public BmscEventRetryTO updateBmscEvent(BmscEventRetryTO bmscEventRetryTO) {
        if (bmscEventRetryTO == null) {
            BmscLogger.eventWarn(BmscLogger.DB_MANAGER, "Can not update BmscEvent with null template.");
            return null;
        }
        //BmscLogger.eventInfo(BmscLogger.DB_MANAGER, "Trying to update BmscEvent(id=" + bmscEventRetryTO.getId() + ")");
        BmscEventEntity entity = new BmscEventEntity();
        entity.copy(bmscEventRetryTO);
        try {
            entityManager.merge(entity);
            BmscLogger.eventInfo(BmscLogger.DB_MANAGER, "Update BmscEvent with ID '" + bmscEventRetryTO.getId() + "' and type '" + bmscEventRetryTO.getNotificationType() + "' successfully.");
        } catch (Exception e) {
        	BmscLogger.eventError(BmscLogger.DB_MANAGER, ErrorCode.DATABASE_UPDATE_FAILED, "BmscEvent", "with ID '" + bmscEventRetryTO.getId() + "' and type '" + bmscEventRetryTO.getNotificationType() + "'", e.getMessage());
            //BmscLogger.eventError(BmscLogger.DB_MANAGER, "Failed to update BmscEvent(id=" + bmscEventRetryTO.getId() + ") because of DB error");
            BmscLogger.eventDebug(BmscLogger.DB_MANAGER, "BmscEventDbManagerImpl:upedateBmscEvent", e);
            // raise alarm
            ProvisionDBAlarmHelper.raiseAlarm(e);
            throw e;
        }

        // clear alarm
        ProvisionDBAlarmHelper.clearAlarm();
        
        return entity.transform();
    }

    @Override
    public BmscEventRetryTO removeBmscEvent(Long id) {
        if (id == null) {
            BmscLogger.eventWarn(BmscLogger.DB_MANAGER, "Can not remove BmscEvent, event ID " + id + ".");
            return null;
        }
        //BmscLogger.eventInfo(BmscLogger.DB_MANAGER, "Trying to remove BmscEvent(id = " + id + ")");

        BmscEventEntity event = null;
        
        try {
            event = this.entityManager.find(BmscEventEntity.class, id);
        } catch (Exception e) {
        	BmscLogger.eventError(BmscLogger.DB_MANAGER, ErrorCode.DATABASE_SEARCH_FAILED, "BmscEvent", "with ID '" + id + "'", e.getMessage());
            //BmscLogger.eventError(BmscLogger.DB_MANAGER, "Failed to remove BmscEvent(id=" + id + ") because of DB error");
            BmscLogger.eventDebug(BmscLogger.DB_MANAGER, "BmscEventDbManagerImpl:removeBmscEvent", e);
            // raise alarm
            ProvisionDBAlarmHelper.raiseAlarm(e);
            throw e;
        }

        // clear alarm
        ProvisionDBAlarmHelper.clearAlarm();
        
        if (event == null) {
            return null;
        }
        
        try {
            entityManager.remove(event);
            BmscLogger.eventInfo(BmscLogger.DB_MANAGER, "Remove BmscEvent in DB successfully by object ID " + id + ", event '"+ event.getDescription() + "'");
        } catch (Exception e) {
        	BmscLogger.eventError(BmscLogger.DB_MANAGER, ErrorCode.DATABASE_DELETE_FAILED, "BmscEvent", "with ID '" + id + "'", e.getMessage());
            //BmscLogger.eventError(BmscLogger.DB_MANAGER, "Failed to remove BmscEvent(id=" + id + ") because of DB error");
            BmscLogger.eventDebug(BmscLogger.DB_MANAGER, "BmscEventDbManagerImpl:removeBmscEvent", e);
            // raise alarm
            ProvisionDBAlarmHelper.raiseAlarm(e);
            throw e;
        }

        // clear alarm
        ProvisionDBAlarmHelper.clearAlarm();
        
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
        	BmscLogger.eventError(BmscLogger.DB_MANAGER, ErrorCode.DATABASE_SEARCH_FAILED, "BmscEvent", "", e.getMessage());
            //BmscLogger.eventError(BmscLogger.DB_MANAGER, "Failed to find BmscEvent because of DB error");
            BmscLogger.eventDebug(BmscLogger.DB_MANAGER, "BmscEventDbManagerImpl:findEvents", e);
            // raise alarm
            ProvisionDBAlarmHelper.raiseAlarm(e);
            throw e;
        }

        // clear alarm
        ProvisionDBAlarmHelper.clearAlarm();

        if (resultList != null && resultList.size() > 0) {
            for (BmscEventEntity event : resultList) {
                list.add(event.transform());
            }
        }
        return list;
    }
}
