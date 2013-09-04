package com.edison.test.ifc;

import com.edison.test.TestDAO;
import com.edison.test.entity.TestEntity;

public interface TestDbManager {

	TestDAO create(TestDAO input);
	
	void update(TestDAO input);
	
	TestDAO remove(Long id);
	
	TestDAO find(TestDAO input);
	
	void lock(Long id);
}
