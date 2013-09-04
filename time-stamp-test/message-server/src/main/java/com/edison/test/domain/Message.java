package com.edison.test.domain;

import java.util.Date;

public class Message {

	private String message;
	private Date createDate;
	
	public Message(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
