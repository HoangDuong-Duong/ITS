package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class EventManualDTO {
    private String siteId;
    private String imageUrl;
    private String videoUrl;
    private String sourceId;
    private String eventCode;
    private String objectName;
    private String directionCode;
    private String uuid;
    private String endSite;
    private String note;
    private String siteCorrect;
    private boolean manualEvent;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date endTime;
    private Integer eventStatus;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date startTime;

    public EventManualDTO() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSiteCorrect() {
        return siteCorrect;
    }

    public void setSiteCorrect(String siteCorrect) {
        this.siteCorrect = siteCorrect;
    }

    public void setSiteId(final String siteId) {
        this.siteId = siteId;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVideoUrl(final String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setSourceId(final String sourceId) {
        this.sourceId = sourceId;
    }

    public void setEventCode(final String eventCode) {
        this.eventCode = eventCode;
    }

    public void setObjectName(final String objectName) {
        this.objectName = objectName;
    }

    public void setDirectionCode(final String directionCode) {
        this.directionCode = directionCode;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public void setEndSite(final String endSite) {
        this.endSite = endSite;
    }

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }
    public void setEventStatus(final Integer eventStatus) {
        this.eventStatus = eventStatus;
    }

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    public void setStartTime(final Date startTime) {
        this.startTime = startTime;
    }

    public String getSiteId() {
        return this.siteId;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public String getSourceId() {
        return this.sourceId;
    }

    public String getEventCode() {
        return this.eventCode;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public String getDirectionCode() {
        return this.directionCode;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getEndSite() {
        return this.endSite;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public Integer getEventStatus() {
        return this.eventStatus;
    }

    public Date getStartTime() {
        return this.startTime;
    }

    public boolean isManualEvent() {
        return manualEvent;
    }

    public void setManualEvent(boolean manualEvent) {
        this.manualEvent = manualEvent;
    }

    
    
    public String toString() {
        String var10000 = this.getSiteId();
        return "EventManualDTO(siteId=" + var10000 + ", imageUrl=" + this.getImageUrl() + ", videoUrl=" + this.getVideoUrl() + ", sourceId=" + this.getSourceId() + ", eventCode=" + this.getEventCode() + ", objectName=" + this.getObjectName() + ", directionCode=" + this.getDirectionCode() + ", uuid=" + this.getUuid() + ", endSite=" + this.getEndSite() + ", endTime=" + this.getEndTime() + ", eventStatus=" + this.getEventStatus() + ", startTime=" + this.getStartTime() + ")";
    }
}
