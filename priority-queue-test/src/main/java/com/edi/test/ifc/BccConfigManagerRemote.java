package com.edi.test.ifc;

public interface BccConfigManagerRemote {

	public String GLOBAL_JNDI_NAME = "";
	
	public int getRetryConntectionTimes();
	
	public int getRetryIntervalTime();
	
	public String getEmbmEventUrl();
	
	public String getBmscName();
	
	public String getKeyStorePath();
	public String getKeyStorePass();
	public String getTrustStorePath();
	public String getTrustStorePass();
}
