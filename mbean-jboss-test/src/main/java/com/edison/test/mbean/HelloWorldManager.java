package com.edison.test.mbean;

/**
 *
 */
public class HelloWorldManager implements HelloWorldMBean {

    /* (non-Javadoc)
     * @see com.edison.test.mbean.HelloWorldMBean#getMsg()
     */
    @Override
    public String getMsg() {
        return "Hello folks!";
    }

}
