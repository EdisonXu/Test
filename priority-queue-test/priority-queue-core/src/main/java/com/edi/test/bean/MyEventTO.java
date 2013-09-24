/**
 * 
 */
package com.edi.test.bean;

import java.io.Serializable;


/**
 * BmscEventTO
 * 
 */
@SuppressWarnings("serial")
public class MyEventTO implements Serializable {

    public static final String DEFAULT_VERSION = "1.0";
    
    private String version = "1.0";

    private NotificationTypeEnum notificationType;

    private String description;
    
    private Long sessionId;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public NotificationTypeEnum getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationTypeEnum notificationType) {
        this.notificationType = notificationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public MyEventTO() {
        super();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime
                * result
                + ((notificationType == null) ? 0 : notificationType.hashCode());
        result = prime * result
                + ((sessionId == null) ? 0 : sessionId.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
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
        MyEventTO other = (MyEventTO) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (notificationType != other.notificationType)
            return false;
        if (sessionId == null) {
            if (other.sessionId != null)
                return false;
        } else if (!sessionId.equals(other.sessionId))
            return false;
        if (version == null) {
            if (other.version != null)
                return false;
        } else if (!version.equals(other.version))
            return false;
        return true;
    }

    public MyEventTO(Long sessionId, String version, NotificationTypeEnum notificationType,
            String description) {
        super();
        this.version = version;
        this.notificationType = notificationType;
        this.description = description;
        this.sessionId = sessionId;
    }

}
