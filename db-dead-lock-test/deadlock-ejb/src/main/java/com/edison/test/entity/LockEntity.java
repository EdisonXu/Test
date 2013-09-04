package com.edison.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "Lock")
public class LockEntity {

	@Id
	@Column(name = "LOCK_TYPE", nullable = false)
	// , unique = true
	@Enumerated(EnumType.STRING)
	private LockType lockType;

	@Version
	@Column(name = "LOCK_VERSION", nullable = false)
	private long lockVersion;

	public LockType getLockType() {
		return lockType;
	}

	public void setLockType(LockType lockType) {
		this.lockType = lockType;
	}

	public long getLockVersion() {
		return lockVersion;
	}

	public void setLockVersion(long lockVersion) {
		this.lockVersion = lockVersion;
	}
	
	
}
