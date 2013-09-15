package com.edi.test;

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

import com.edi.test.ifc.Queries;

@Entity
@Table(name = "T_BMSC_EVENT")
@NamedQueries({ @NamedQuery(name = Queries.FIND_BMSC_EVENT, query = "SELECT con FROM BmscEventEntity con order by RETRY_START_TIME, id") })
public class BmscEventEntity {

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

    @Column(name = "EMBMS_SESSION_ID")
    private Long embmsSessionId;

    @Column(name = "DELIVERY_SESSION_ID")
    private Long deliverySessionId;

    @Column(name = "DELIVERY_INSTANCE_ID")
    private Long deliveryInstanceId;

    @Column(name = "CONTENT_GROUP_ID")
    private Long contentGroupId;

    @Column(name = "CONTENT_URI")
    private String contentUri;

    @Column(name = "MD5_VALUE")
    private String md5value;

    @Column(name = "EXTENSION")
    private String extension;

    @Column(name = "DESCRIPTION")
    private String description;

    public BmscEventEntity() {
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
    public BmscEventEntity(Long id, NotificationTypeEnum notificationType,
            Integer retryTimes, Date retryStartTime, Date retryUpdateTime,
            Long embmsSessionId, Long deliverySessionId,
            Long deliveryInstanceId, Long contentGroupId, String contentUri,
            String md5value, String extension, String description) {
        this.id = id;
        this.notificationType = notificationType;
        this.retryTimes = retryTimes;
        this.retryStartTime = retryStartTime;
        this.retryUpdateTime = retryUpdateTime;
        this.embmsSessionId = embmsSessionId;
        this.deliverySessionId = deliverySessionId;
        this.deliveryInstanceId = deliveryInstanceId;
        this.contentGroupId = contentGroupId;
        this.contentUri = contentUri;
        this.md5value = md5value;
        this.extension = extension;
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
     * @return the embmsSessionId
     */
    public Long getEmbmsSessionId() {
        return embmsSessionId;
    }

    /**
     * @param embmsSessionId
     *            the embmsSessionId to set
     */
    public void setEmbmsSessionId(Long embmsSessionId) {
        this.embmsSessionId = embmsSessionId;
    }

    /**
     * @return the deliverySessionId
     */
    public Long getDeliverySessionId() {
        return deliverySessionId;
    }

    /**
     * @param deliverySessionId
     *            the deliverySessionId to set
     */
    public void setDeliverySessionId(Long deliverySessionId) {
        this.deliverySessionId = deliverySessionId;
    }

    /**
     * @return the deliveryInstanceId
     */
    public Long getDeliveryInstanceId() {
        return deliveryInstanceId;
    }

    /**
     * @param deliveryInstanceId
     *            the deliveryInstanceId to set
     */
    public void setDeliveryInstanceId(Long deliveryInstanceId) {
        this.deliveryInstanceId = deliveryInstanceId;
    }

    /**
     * @return the contentGroupId
     */
    public Long getContentGroupId() {
        return contentGroupId;
    }

    /**
     * @param contentGroupId
     *            the contentGroupId to set
     */
    public void setContentGroupId(Long contentGroupId) {
        this.contentGroupId = contentGroupId;
    }

    /**
     * @return the contentUri
     */
    public String getContentUri() {
        return contentUri;
    }

    /**
     * @param contentUri
     *            the contentUri to set
     */
    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    /**
     * @return the md5value
     */
    public String getMd5value() {
        return md5value;
    }

    /**
     * @param md5value
     *            the md5value to set
     */
    public void setMd5value(String md5value) {
        this.md5value = md5value;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension
     *            the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
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

    public BmscEventRetryTO transform() {
        BmscEventRetryTO bmscEventRetryTO = new BmscEventRetryTO(id,
                retryTimes, retryStartTime, retryUpdateTime, extension, "",
                notificationType, embmsSessionId, deliverySessionId,
                deliveryInstanceId, description, contentUri, md5value,
                contentGroupId);
        return bmscEventRetryTO;
    }

    public void copy(BmscEventRetryTO to) {
        if (to == null) {
            return;
        }
        this.id = to.getId();
        this.retryTimes = to.getRetryTimes();
        this.retryStartTime = to.getRetryStartTime();
        this.retryUpdateTime = to.getRetryUpdateTime();
        this.extension = to.getExtension();
        this.notificationType = to.getNotificationType();
        this.embmsSessionId = to.getEMBMSSessionId();
        this.deliverySessionId = to.getDeliverySessionId();
        this.deliveryInstanceId = to.getDeliveryInstanceId();
        this.description = to.getDescription();
        this.contentUri = to.getContentURI();
        this.md5value = to.getMd5Value();
        this.contentGroupId = to.getContentGroupId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BmscEventEntity [id=");
        builder.append(id);
        builder.append(", notificationType=");
        builder.append(notificationType);
        builder.append(", retryTimes=");
        builder.append(retryTimes);
        builder.append(", retryStartTime=");
        builder.append(retryStartTime);
        builder.append(", retryUpdateTime=");
        builder.append(retryUpdateTime);
        builder.append(", embmsSessionId=");
        builder.append(embmsSessionId);
        builder.append(", deliverySessionId=");
        builder.append(deliverySessionId);
        builder.append(", deliveryInstanceId=");
        builder.append(deliveryInstanceId);
        builder.append(", contentGroupId=");
        builder.append(contentGroupId);
        builder.append(", contentUri=");
        builder.append(contentUri);
        builder.append(", md5value=");
        builder.append(md5value);
        builder.append(", extension=");
        builder.append(extension);
        builder.append(", description=");
        builder.append(description);
        builder.append("]");
        return builder.toString();
    }

}
