package com.edison.test;

import java.io.Serializable;


public class TestDAO implements Serializable{

	private static final long serialVersionUID = -5777639843507431489L;
	private Long id;
	private String attribute;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}
