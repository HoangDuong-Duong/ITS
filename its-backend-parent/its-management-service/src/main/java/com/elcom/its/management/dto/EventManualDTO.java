package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventManualDTO {
    private String siteId;
    private Long startKm;
    private Long startM;
    private Integer startProvinceId;
    private Integer startDistrictId;
    private Integer startWardId;
    private String startAddress;
    private String imageUrl;
    private String videoUrl;
    private String sourceId;
    private String eventCode;
    private String objectName;
    private String directionCode;
    private String uuid;
    private String endSite;
    private Long endKm;
    private Long endM;
    private Integer endProvinceId;
    private Integer endDistrictId;
    private Integer endWardId;
    private String endAddress;
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
    private String user;
    private String listSite;

    public String getListSite() {
        return listSite;
    }

    public void setListSite(String listSite) {
        this.listSite = listSite;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

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

    public Long getStartKm() {
        return startKm;
    }

    public void setStartKm(Long startKm) {
        this.startKm = startKm;
    }

    public Long getStartM() {
        return startM;
    }

    public void setStartM(Long startM) {
        this.startM = startM;
    }

    public Integer getStartProvinceId() {
        return startProvinceId;
    }

    public void setStartProvinceId(Integer startProvinceId) {
        this.startProvinceId = startProvinceId;
    }

    public Integer getStartDistrictId() {
        return startDistrictId;
    }

    public void setStartDistrictId(Integer startDistrictId) {
        this.startDistrictId = startDistrictId;
    }

    public Integer getStartWardId() {
        return startWardId;
    }

    public void setStartWardId(Integer startWardId) {
        this.startWardId = startWardId;
    }

    public Long getEndKm() {
        return endKm;
    }

    public void setEndKm(Long endKm) {
        this.endKm = endKm;
    }

    public Long getEndM() {
        return endM;
    }

    public void setEndM(Long endM) {
        this.endM = endM;
    }

    public Integer getEndProvinceId() {
        return endProvinceId;
    }

    public void setEndProvinceId(Integer endProvinceId) {
        this.endProvinceId = endProvinceId;
    }

    public Integer getEndDistrictId() {
        return endDistrictId;
    }

    public void setEndDistrictId(Integer endDistrictId) {
        this.endDistrictId = endDistrictId;
    }

    public Integer getEndWardId() {
        return endWardId;
    }

    public void setEndWardId(Integer endWardId) {
        this.endWardId = endWardId;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String toString() {
        String var10000 = this.getSiteId();
        return "EventManualDTO(siteId=" + var10000 + ", imageUrl=" + this.getImageUrl() + ", videoUrl=" + this.getVideoUrl() + ", sourceId=" + this.getSourceId() + ", eventCode=" + this.getEventCode() + ", objectName=" + this.getObjectName() + ", directionCode=" + this.getDirectionCode() + ", uuid=" + this.getUuid() + ", endSite=" + this.getEndSite() + ", endTime=" + this.getEndTime() + ", eventStatus=" + this.getEventStatus() + ", startTime=" + this.getStartTime() + ")";
    }
}
