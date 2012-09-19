/******************************************************************************
 * Copyright Ericsson AB 2011 This program may be used and/or copied only with
 * the prior written permission of Ericsson AB or in accordance with the terms
 * and conditions stipulated in the contract agreement under which the program
 * has been supplied.
 *****************************************************************************/
package com.edison.test.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 */
@Entity
@Table(name="TEST_MESSAGE")
public class MessageEntity {

    @Id
    @SequenceGenerator(name="SEQ_TEST_MESSAGE_ID", sequenceName="SEQ_TEST_MESSAGE_ID", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TEST_MESSAGE_ID")
    private Long id;
    
    @Column(name="MESSAGE", nullable=false)
    private String message;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATE_TIME", nullable=false)
    private Date createTime;

    /**
     * @param message
     */
    public MessageEntity(String message) {
        super();
        this.message = message;
        this.createTime = new Date();
    }
    
    public MessageEntity() {
        super();
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
}
