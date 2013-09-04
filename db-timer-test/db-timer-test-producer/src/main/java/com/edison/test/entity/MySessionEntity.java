package com.edison.test.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.edison.test.MySessionDAO;
import com.edison.test.ifc.Queries;

@Entity
@Table(name="MySession")
@NamedQueries({
	@NamedQuery(name = Queries.READ_SESSION_BY_DURATION, 
			query = "SELECT se FROM MySessionEntity AS se WHERE se.stopTime >= :durationBegin and se.startTime <= :durationEnd")
})
public class MySessionEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6850585015062152134L;

	@Id
	@SequenceGenerator(name="SEQ_TEST_ENTITY_ID", sequenceName="SEQ_TEST_ENTITY_ID", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TEST_ENTITY_ID")
	private Long id = null;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime = null;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date stopTime = null;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime = null;
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private String attribute = null;
	
	private boolean started = false;
	
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	@Column
	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	public MySessionDAO transform()
	{
		MySessionDAO td = new MySessionDAO(startTime, stopTime);
		td.setId(this.id);
		td.setAttribute(this.attribute);
		td.setStarted(this.started);
		return td;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attribute == null) ? 0 : attribute.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + (started ? 1231 : 1237);
		result = prime * result
				+ ((stopTime == null) ? 0 : stopTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MySessionEntity other = (MySessionEntity) obj;
		if (attribute == null) {
			if (other.attribute != null)
				return false;
		} else if (!attribute.equals(other.attribute))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (started != other.started)
			return false;
		if (stopTime == null) {
			if (other.stopTime != null)
				return false;
		} else if (!stopTime.equals(other.stopTime))
			return false;
		return true;
	}

	public void copy(MySessionDAO sessionDAO)
	{
		if(sessionDAO==null)
			return;
		this.id = sessionDAO.getId();
		this.startTime = sessionDAO.getStartTime();
		this.stopTime = sessionDAO.getStopTime();
		this.attribute = sessionDAO.getAttribute();
		this.started = sessionDAO.isStarted();
	}
}
