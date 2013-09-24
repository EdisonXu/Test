package com.edi.test.bean;

import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class MyEventRetryTO extends MyEventTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8331632249224958871L;

	private Long id;

	private Integer retryTimes;

	private Date retryStartTime;

	private Date retryUpdateTime;

	private transient ReentrantLock lock;

	/**
	 * 
	 */
	public MyEventRetryTO() {
		super();
		this.lock = new ReentrantLock();
	}

	
	public MyEventRetryTO(MyEventTO event){
		this.lock = new ReentrantLock();
	}

	/**
	 * @param retryTimes
	 * @param retryStartTime
	 * @param retryUpdateTime
	 * @param extension
	 * @param version
	 * @param notificationType
	 * @param embmsSessionId
	 * @param deliverySessionId
	 * @param deliveryInstanceId
	 * @param description
	 * @param contentURI
	 * @param md5Value
	 * @param contentGroupId
	 * @param removedDeliveryContents
	 */
	public MyEventRetryTO(Long id, Integer retryTimes, Date retryStartTime,
			Date retryUpdateTime, Long sessionId, String version,NotificationTypeEnum notificationType, String description) {
		super(sessionId, version, notificationType, description);
		this.id = id;
		this.retryTimes = retryTimes;
		this.retryStartTime = retryStartTime;
		this.retryUpdateTime = retryUpdateTime;
		this.lock = new ReentrantLock();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the retryTimes
	 */
	public Integer getRetryTimes() {
		return retryTimes;
	}

	/**
	 * @param retryTimes
	 *            the retryTimes to set
	 */
	public void setRetryTimes(Integer retryTimes) {
		this.retryTimes = retryTimes;
	}

	/**
	 * @return the retryStartTime
	 */
	public Date getRetryStartTime() {
		return retryStartTime;
	}

	/**
	 * @param retryStartTime
	 *            the retryStartTime to set
	 */
	public void setRetryStartTime(Date retryStartTime) {
		this.retryStartTime = retryStartTime;
	}

	/**
	 * @return the retryUpdateTime
	 */
	public Date getRetryUpdateTime() {
		return retryUpdateTime;
	}

	/**
	 * @param retryUpdateTime
	 *            the retryUpdateTime to set
	 */
	public void setRetryUpdateTime(Date retryUpdateTime) {
		this.retryUpdateTime = retryUpdateTime;
	}


	public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((retryStartTime == null) ? 0 : retryStartTime.hashCode());
		result = prime * result
				+ ((retryTimes == null) ? 0 : retryTimes.hashCode());
		result = prime * result
				+ ((retryUpdateTime == null) ? 0 : retryUpdateTime.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (!super.equals(obj)){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		MyEventRetryTO other = (MyEventRetryTO) obj;
		if (id == null) {
			if (other.id != null){
				return false;
			}
		} else if (!id.equals(other.id)){
			return false;
		}
		if (retryStartTime == null) {
			if (other.retryStartTime != null){
				return false;
			}
		} else if (!retryStartTime.equals(other.retryStartTime)){
			return false;
		}
		if (retryTimes == null) {
			if (other.retryTimes != null){
				return false;
			}
		} else if (!retryTimes.equals(other.retryTimes)){
			return false;
		}
		if (retryUpdateTime == null) {
			if (other.retryUpdateTime != null){
				return false;
			}
		} else if (!retryUpdateTime.equals(other.retryUpdateTime)){
			return false;
		}
		return true;
	}

	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        
        return "MyEventRetryTO [id=" + id + ", retryTimes=" + retryTimes
                + ", retryStartTime=" + retryStartTime + ", retryUpdateTime="
                + retryUpdateTime + ", sessionId=" + getSessionId()
                + ", getVersion()=" + getVersion() + ", getNotificationType()="
                + getNotificationType() + ", getDescription()="
                + getDescription() + ", getRemovedDeliveryContents()=";
    }
	
}
