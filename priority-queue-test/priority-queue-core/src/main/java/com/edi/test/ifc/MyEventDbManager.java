package com.edi.test.ifc;

import java.util.List;

import com.edi.test.bean.MyEventRetryTO;


public interface MyEventDbManager{

    public MyEventRetryTO persistMyEvent(MyEventRetryTO myEventRetryTO);
    
    public MyEventRetryTO updateMyEvent(MyEventRetryTO myEventRetryTO);

    public MyEventRetryTO removeMyEvent(Long id);

    public List<MyEventRetryTO> findEvents();
}
