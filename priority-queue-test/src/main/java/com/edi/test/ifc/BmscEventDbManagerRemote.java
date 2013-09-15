package com.edi.test.ifc;

import java.util.List;

import com.edi.test.BmscEventRetryTO;

public interface BmscEventDbManagerRemote {

    public BmscEventRetryTO createBmscEvent(BmscEventRetryTO bmscEventRetryTO);
    
    public BmscEventRetryTO updateBmscEvent(BmscEventRetryTO bmscEventRetryTO);

    public BmscEventRetryTO removeBmscEvent(Long id);

    public List<BmscEventRetryTO> findEvents();

}
