/**
 * 
 */
package com.ericsson.ecds.bcc.prov.common.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ericsson.ecds.bcc.prov.common.event.NotificationTypeEnum;

/**
 * BmscEventTO
 * 
 */
@SuppressWarnings("serial")
public class BmscEventTO implements ProvisioningTO, Serializable {

    public static final String DEFAULT_VERSION = "1.0";
    
    private String version = "1.0";

    private NotificationTypeEnum notificationType;

    private Long embmsSessionId;

    private Long deliverySessionId;

    private Long deliveryInstanceId;

    private String description;

    private String contentURI;
    
    private String md5Value;
    
    private Long contentGroupId = null;
    
    private List<DeliveryContentTO> removedDeliveryContents;
    
    

    /**
	 * 
	 */
	public BmscEventTO() {
	}
	

	/**
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
	public BmscEventTO(String version, NotificationTypeEnum notificationType,
			Long embmsSessionId, Long deliverySessionId,
			Long deliveryInstanceId, String description, String contentURI,
			String md5Value, Long contentGroupId,
			List<DeliveryContentTO> removedDeliveryContents) {
		this.version = version;
		this.notificationType = notificationType;
		this.embmsSessionId = embmsSessionId;
		this.deliverySessionId = deliverySessionId;
		this.deliveryInstanceId = deliveryInstanceId;
		this.description = description;
		this.contentURI = contentURI;
		this.md5Value = md5Value;
		this.contentGroupId = contentGroupId;
		this.removedDeliveryContents = removedDeliveryContents;
	}



	/**
     * Gets the value of the version property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the notificationType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public NotificationTypeEnum getNotificationType() {
        return notificationType;
    }

    /**
     * Sets the value of the notificationType property.
     * 
     * @param type allowed object is {@link String }
     * 
     */
    public void setNotificationType(NotificationTypeEnum type) {
        this.notificationType = type;
    }

    /**
     * Gets the value of the embmsSessionId property.
     * 
     * @return possible object is {@link Long }
     * 
     */
    public Long getEMBMSSessionId() {
        return embmsSessionId;
    }

    /**
     * Sets the value of the embmsSessionId property.
     * 
     * @param value allowed object is {@link Long }
     * 
     */
    public void setEMBMSSessionId(Long value) {
        this.embmsSessionId = value;
    }

    /**
     * Gets the value of the deliverySessionId property.
     * 
     * @return possible object is {@link Long }
     * 
     */
    public Long getDeliverySessionId() {
        return deliverySessionId;
    }

    /**
     * Sets the value of the deliverySessionId property.
     * 
     * @param value allowed object is {@link Long }
     * 
     */
    public void setDeliverySessionId(Long value) {
        this.deliverySessionId = value;
    }

    /**
     * Gets the value of the deliveryInstanceId property.
     * 
     * @return possible object is {@link Long }
     * 
     */
    public Long getDeliveryInstanceId() {
        return deliveryInstanceId;
    }

    /**
     * Sets the value of the deliveryInstanceId property.
     * 
     * @param value allowed object is {@link Long }
     * 
     */
    public void setDeliveryInstanceId(Long value) {
        this.deliveryInstanceId = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the contentURI property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getContentURI() {
        return contentURI;
    }

    /**
     * Sets the value of the contentURI property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setContentURI(String value) {
        this.contentURI = value;
    }
    
    
    /**
     * Gets Md5 value from announcement
     * @return the md5Value
     */
    public String getMd5Value() {
        return md5Value;
    }

    /**
     * Set Md5 value from announcement
     * @param md5Value the md5Value to set
     */
    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }
    

    /**
     * Gets content group id
     * @return the contentGrougId
     */
    public Long getContentGroupId() {
        return contentGroupId;
    }

    /**
     * Sets content group id
     * @param contentGrougId the contentGrougId to set
     */
    public void setContentGroupId(Long contentGroupId) {
        this.contentGroupId = contentGroupId;
    }
    
    public List<DeliveryContentTO> getRemovedDeliveryContents() {
        if(removedDeliveryContents == null) {
            removedDeliveryContents = new ArrayList<DeliveryContentTO>();
        }
        return removedDeliveryContents;
    }

    public void setRemovedDeliveryContents(List<DeliveryContentTO> removedDeliveryContents) {
        this.removedDeliveryContents = removedDeliveryContents;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BmscEventTO [version=");
        builder.append(version);
        builder.append(", notificationType=");
        builder.append(notificationType);
        builder.append(", embmsSessionId=");
        builder.append(embmsSessionId);
        builder.append(", deliverySessionId=");
        builder.append(deliverySessionId);
        builder.append(", deliveryInstanceId=");
        builder.append(deliveryInstanceId);
        builder.append(", description=");
        builder.append(description);
        builder.append(", contentURI=");
        builder.append(contentURI);
        builder.append(", md5=");
        builder.append(md5Value);
        builder.append(", contentGroupId=");
        builder.append(contentGroupId);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((contentURI == null) ? 0 : contentURI.hashCode());
        result = prime * result + ((deliveryInstanceId == null) ? 0 : deliveryInstanceId.hashCode());
        result = prime * result + ((deliverySessionId == null) ? 0 : deliverySessionId.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((embmsSessionId == null) ? 0 : embmsSessionId.hashCode());
        result = prime * result + ((notificationType == null) ? 0 : notificationType.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        result = prime * result + ((md5Value == null) ? 0 : md5Value.hashCode());
        result = prime * result + ((contentGroupId == null) ? 0 : contentGroupId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BmscEventTO other = (BmscEventTO) obj;
        if (contentURI == null) {
            if (other.contentURI != null) {
                return false;
            }
        } else if (!contentURI.equals(other.contentURI)) {
            return false;
        }
        if (deliveryInstanceId == null) {
            if (other.deliveryInstanceId != null) {
                return false;
            }
        } else if (!deliveryInstanceId.equals(other.deliveryInstanceId)) {
            return false;
        }
        if (deliverySessionId == null) {
            if (other.deliverySessionId != null) {
                return false;
            }
        } else if (!deliverySessionId.equals(other.deliverySessionId)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (embmsSessionId == null) {
            if (other.embmsSessionId != null) {
                return false;
            }
        } else if (!embmsSessionId.equals(other.embmsSessionId)) {
            return false;
        }
        if (notificationType == null) {
            if (other.notificationType != null) {
                return false;
            }
        } else if (!notificationType.equals(other.notificationType)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        if(md5Value == null)
        {
            if(other.md5Value != null){
                return false;
            }
        } else if(!md5Value.equals(other.md5Value)){
            return false;
        }
        if(contentGroupId == null)
        {
            if(other.contentGroupId != null){
                return false;
            }
        } else if(!contentGroupId.equals(other.contentGroupId)){
            return false;
        }
        return true;
    }

}
