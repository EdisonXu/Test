package com.edison.test.ifc;

import java.util.Date;
import java.util.List;

import com.edison.test.MySessionDAO;

public interface MySessionDbManager {

	MySessionDAO create(MySessionDAO input);
	
	void update(MySessionDAO input);
	
	MySessionDAO remove(Long id);
	
	MySessionDAO find(MySessionDAO input);
	
	void lock(Long id);
	
	List<MySessionDAO> findSessionByDuration(Date begin, Date end);
}
