package com.edison.test.mbean;

/**
 *
 */
public interface HelloWorldManagerMBean {

    public String getMsg();
    
    public void setMsg(String msg);
    
    public String printMsg();
}
