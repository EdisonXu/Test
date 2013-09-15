package com.ericsson.ecds.bcc.prov.common.dbmgr;

import java.util.List;

import com.ericsson.ecds.bcc.prov.common.data.BmscEventRetryTO;

public interface BmscEventDbManagerRemote {

    public BmscEventRetryTO createBmscEvent(BmscEventRetryTO bmscEventRetryTO);
    
    public BmscEventRetryTO updateBmscEvent(BmscEventRetryTO bmscEventRetryTO);

    public BmscEventRetryTO removeBmscEvent(Long id);

    public List<BmscEventRetryTO> findEvents();

}
