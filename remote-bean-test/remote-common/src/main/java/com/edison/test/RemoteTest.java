package com.edison.test;


public interface RemoteTest {
    
    public static final String NAME = "Test/RemoteTest";
    
    public static final String JNDI_NAME = "Test/RemoteTest/remote";
    
    /**
     * Return "Test"
     * @return
     */
    public String getTest();
    
    /**
     * Local interface for session bean
     */
    public static interface Local extends RemoteTest{}
    
    /**
     * Remote interface for session bean
     */
    public static interface Remote extends RemoteTest{}

}
