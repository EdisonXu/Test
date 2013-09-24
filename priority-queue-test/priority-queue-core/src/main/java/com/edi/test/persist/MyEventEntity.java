package com.edi.test.persist;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.edi.test.bean.MyEventRetryTO;
import com.edi.test.bean.NotificationTypeEnum;
import com.edi.test.ifc.Queries;

@Entity
@Table(name = "T_BMSC_EVENT")
@NamedQueries({ @NamedQuery(name = Queries.FIND_MY_EVENT, query = "SELECT con FROM MyEventEntity con order by RETRY_START_TIME, id") })
public class MyEventEntity {

    @Id
    @SequenceGenerator(name = "SEQ_BCC_EVENT_ID", sequenceName = "SEQ_BCC_EVENT_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BCC_EVENT_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NOTIFICATION_TYPE", nullable = false)
    private NotificationTypeEnum notificationType;

    @Column(name = "RETRY_TIMES", nullable = false)
    private Integer retryTimes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RETRY_START_TIME", nullable = false)
    private Date retryStartTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RETRY_UPDATE_TIME", nullable = false)
    private Date retryUpdateTime;

    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "SESSION_ID")
    private Long sessionId;

    public MyEventEntity() {
    }

    /**
     * @param id
     * @param notificationType
     * @param retryTimes
     * @param retryStartTime
     * @param retryUpdateTime
     * @param embmsSessionId
     * @param deliverySessionId
     * @param deliveryInstanceId
     * @param contentGroupId
     * @param contentUri
     * @param md5value
     * @param extension
     * @param description
     */
    public MyEventEntity(Long id, NotificationTypeEnum notificationType,
            Integer retryTimes, Date retryStartTime, Date retryUpdateTime, Long sessionId, String description) {
        this.id = id;
        this.notificationType = notificationType;
        this.retryTimes = retryTimes;
        this.retryStartTime = retryStartTime;
        this.retryUpdateTime = retryUpdateTime;
        this.sessionId = sessionId;
        this.description = description;
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
     * @return the notificationType
     */
    public NotificationTypeEnum getNotificationType() {
        return notificationType;
    }

    /**
     * @param notificationType
     *            the notificationType to set
     */
    public void setNotificationType(NotificationTypeEnum notificationType) {
        this.notificationType = notificationType;
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

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public MyEventRetryTO transform() {
        MyEventRetryTO bmscEventRetryTO = new MyEventRetryTO(id,
                retryTimes, retryStartTime, retryUpdateTime, sessionId, "", notificationType, description);
        return bmscEventRetryTO;
    }

    public void copy(MyEventRetryTO to) {
        if (to == null) {
            return;
        }
        this.id = to.getId();
        this.retryTimes = to.getRetryTimes();
        this.retryStartTime = to.getRetryStartTime();
        this.retryUpdateTime = to.getRetryUpdateTime();
        this.sessionId = to.getSessionId();
        this.notificationType = to.getNotificationType();
        this.description = to.getDescription();
    }

    @Override
    public String toString() {
        return "MyEventEntity [id=" + id + ", notificationType="
                + notificationType + ", retryTimes=" + retryTimes
                + ", retryStartTime=" + retryStartTime + ", retryUpdateTime="
                + retryUpdateTime + ", description=" + description
                + ", sessionId=" + sessionId + "]";
    }


}
