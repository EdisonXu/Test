package com.edison.test;


public interface RemoteEJBInjectSample {

    public static final String NAME = "Test/RemoteEJBInjectSample";
    public static final String JNDI_NAME = "Test/RemoteEJBInjectSample/remote";
    
    public static interface Local extends RemoteEJBInjectSample{}
    public static interface Remote extends RemoteEJBInjectSample{}
    
    public String getValueByInjection();
    
    public String getValueByJNDILookup();
}
