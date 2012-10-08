package com.edison.test.beans;

import java.io.Serializable;

public class TestJob implements Serializable{

	private int n;

	public TestJob(int n) {
		super();
		this.n = n;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
	
}
