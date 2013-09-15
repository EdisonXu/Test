/*------------------------------------------------------------------------------
 * COPYRIGHT Ericsson 2011
 * 
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *----------------------------------------------------------------------------*/
package com.ericsson.ecds.bcc.prov.common.event;

/**
 * NotificationTypeEnum
 */
public enum NotificationTypeEnum {

    /**
     * <code>UP_AND_RUNNING</code>
     */
    UP_AND_RUNNING(10, "up and running"),

    /**
     * <code>OUT_OF_SERVICE</code>
     */
    OUT_OF_SERVICE(11, "BM-SC out of service"),

    /**
     * <code>CAHCE_AT_80_PERCENT_OCCIPANCY</code>
     */
    CACHE_AT_80_PERCENT_OCCUPANCY(12, "BM-SC cache at 80% occupancy"),

    /**
     * <code>CACHE_FULL</code>
     */
    CACHE_FULL(13, "BM-SC cache full"),

    /**
     * <code>BEARER_ACTIVATED</code>
     */
    BEARER_ACTIVATED(20, "Bearer activated", 1),

    /**
     * <code>BEARER_DELETED</code>
     */
    BEARER_DELETED(21, "Bearer deleted"),

    /**
     * <code>BEARER_CHECKING_TIME_OK</code>
     */
    BEARER_CHECKING_TIME_OK(22, "Bearer checking time OK"),

    /**
     * <code>BEARER_UNEXPECTED_STOP</code>
     */
    BEARER_UNEXPECTED_STOP(29, "Unexpected stop due to internal error - bearer"),

    /**
     * <code>INSTANCE_DELETED</code>
     */
    INSTANCE_DELETED(31, "Instance deleted"),

    /**
     * <code>SESSION_UNEXPECTED_STOP</code>
     */
    SESSION_UNEXPECTED_STOP(39, "Unexpected stop - session"),

    /**
     * <code>FILE_DOWNLOADED</code>
     */
    FILE_DOWNLOADED(40, "File downloaded", 1),

    /**
     * <code>FILE_READY_FOR_DELIVERY</code>
     */
    FILE_READY_FOR_DELIVERY(41, "File ready for delivery", 1),

    /**
     * <code>FILE_DELIVERED</code>
     */
    FILE_DELIVERED(42, "File delivered", 1),

    /**
     * <code>FILE_DELETED</code>
     */
    FILE_DELETED(43, "File deleted"),

    /**
     * <code>NO_FILE_PUSHED</code>
     */
    NO_FILE_PUSHED(44, "No file pushed over Web-DAV"),

    /**
     * <code>TOO_BIG_FILE_PUSHED</code>
     */
    TOO_BIG_FILE_PUSHED(45, "Too big file pushed over Web-DAV"),

    /**
     * <code>DIRECTORY_AT_80_PERCENT_OCCUPANCY</code>
     */
    DIRECTORY_AT_80_PERCENT_OCCUPANCY(46, "Web-DAV directory at 80% occupancy"),

    /**
     * <code>DIRECTORY_FULL</code>
     */
    DIRECTORY_FULL(47, "Web-DAV directory full"),

    /**
     * <code>UNEXPECTED_CONTENT_DELETED</code>
     */
    UNEXPECTED_CONTENT_DELETED(48, "Unexpected content deleted"),
    
    /**
     * <code>FILE_DELIVERY_STARTED</code>
     */
    FILE_DELIVERY_STARTED(49, "File delivery started"),
    
    /**
     * <code>FILE_TRANSMISSION_STOPPED</code>
     */
    FILE_TRANSMISSION_FAILED(50, "File transmission stopped due to internal error", 1),
    
    /**
     * <code>FILE_PREPARATION_FAILED</code>
     */
    FILE_PREPARATION_FAILED(51, "File preparation failed"),
    
    /**
     * <code>FILE_DOWNLOAD_FAILED</code>
     */
    FILE_DOWNLOAD_FAILED(52, "File download failed"),
    
    /**
     * <code>CONTINUE_CONTENT_DELIVERY_STARTED</code>
     */
    CONTINUE_CONTENT_DELIVERY_STARTED(53, "Continuous content delivery started"),
    
    /**
     * <code>CONTINUE_CONTENT_TRANSMISSION_FAILED</code>
     */
    CONTINUE_CONTENT_TRANSMISSION_FAILED(54, "Continuous content transmission failed"),

    /**
     * <code>FILE_ENCODING</code>
     */
    FILE_ENCODING(81, "File encoding"), // internal usage only
    
    /**
     * <code>FILE_ENCODING_OK</code>
     */
    FILE_ENCODING_OK(82, "File encoding"), // internal usage only
    
    /**
     * <code>FILE_ENCODING_FAIL</code>
     */
    FILE_ENCODING_FAIL(83, "File encoding"), // internal usage only
    
    /**
     * <code>FILE_ENCODED</code>
     */
    FILE_ENCODED(84, "File encoded"), // internal usage only
    
    /**
     * <code>FILE_PROCESSING</code>
     */
    FILE_PROCESSING(85, "File processing"), // internal usage only
    FILE_ADD_FAIL(86, "Add file failed"), // internal usage only
    FILE_ADD_OK(87, "Add file OK"), // internal usage only
    BEARER_DELETED_MANUALLY(88, "Bearer deleted manually"), // internal usage only
    FILE_NOT_FOUND(89, "To be downloaded File does not exist"), // this code is not defined in NBI
    FILE_TOO_BIG(90, "To be downloaded File is too big"), // this code is not defined in NBI
    FILE_REMOVE_OK(91, "Remove file OK"),// internal usage only
    FILE_REMOVE_FAIL(92, "Remove fail failed"),// internal usage only
    DELIVERY_REMOVE_OK(93, "Remove delivery session instance OK"),// internal usage only
    DELIVERY_REMOVE_FAIL(94, "Remove delivery session instance failed"),// internal usage only
    WEBDAV_DIRECTORY_READY(95, "Web-DAV directory is ready");// internal usage only
    
    private Integer value;
    private String desciption;
    private int priority;

    private NotificationTypeEnum(Integer value, String desciption) {
        this.value = value;
        this.desciption = desciption;
        this.priority = 0;
    }
    
    private NotificationTypeEnum(Integer value, String desciption, int priority) {
        this.value = value;
        this.desciption = desciption;
        this.priority = priority;
    }

    /**
     * @return value
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     * @return desciption
     */
    public String getDesciption() {
        return this.desciption;
    }

    public int getPriority() {
        return priority;
    }
    
    public static int getMaxPriority(){
        int max = 0;
        for(NotificationTypeEnum each: NotificationTypeEnum.values())
        {
            if(each.getPriority()>max){
                max = each.getPriority();
            }
        }
        return max;
    }
}
