package com.edi.test.ifc;

public interface MyConfig {

	public String GLOBAL_JNDI_NAME = "";
	
	public int getRetryConntectionTimes();
	
	public int getRetryIntervalTime();
	
	public String getEmbmEventUrl();
	
	public String getBmscName();
	
	public String getKeyStorePath();
	public String getKeyStorePass();
	public String getTrustStorePath();
	public String getTrustStorePass();
	
	int RETRY_CONNECTION_TIMES = 3;
	int RETRY_INTERVAL_TIME_IN_SEC = 10;
	String CONSUMER_URL = "http://127.0.0.1/myevents/";
	String CONSUMER_NAME = "me";
	String KEYSTORE_PATH = "";
	String KEYSTORE_PASS = "";
	String TRUSTSTORE_PATH = "";
	String TRUSTSTORE_PASS = "";
}
