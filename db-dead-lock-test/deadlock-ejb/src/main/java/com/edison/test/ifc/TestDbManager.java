package com.edison.test.ifc;

import com.edison.test.entity.TestEntity;

public interface TestDbManager {

	void create(TestEntity entity);
	
	void update(TestEntity entity);
	
	void remove(TestEntity entity);
	
	TestEntity find(TestEntity entity);
}
