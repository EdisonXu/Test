package com.edison.test.mbean;

/**
 *
 */
public class HelloWorldManager implements HelloWorldManagerMBean {

    private String msg = "Hello world!";

    /*
     * (non-Javadoc)
     * 
     * @see com.edison.test.mbean.HelloWorldMBean#getMsg()
     */
    @Override
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg
     *            the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String printMsg() {
        return msg + " test!";
    }
}
