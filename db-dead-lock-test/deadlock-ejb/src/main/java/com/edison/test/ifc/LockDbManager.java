package com.edison.test.ifc;

import com.edison.test.entity.LockType;

public interface LockDbManager {

	void createLock();
	void lock(LockType type);
}
