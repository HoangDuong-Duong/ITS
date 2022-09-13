package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.enums.EventStatus;
import com.elcom.its.vds.enums.EventType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO {
    private static final long serialVersionUID = 1L;
    private String id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    private String sourceId;
    private String sourceName;
    private SiteInfo site;
    private LanesRoute lane;
    private String videoUrl;
    private String imageUrl;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date imageTime;
    private Float longitude;
    private Float latitude;
    private String plate;
    private EventType event;
    private String eventCode;
    private String eventName;
    private String objectType;
    private String objectCreate;
    private Float speedOfVehicle;
    private SiteInfo endSite;
    private String createBy;
    private String listSite;
    private String nameStatus;
    private Boolean reportStatus;


    private EventStatus eventStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    private String modifiedBy;

    private String modifiedAction;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    private Integer isNewest;
    private Integer isDelete;
    private String eventIdString;
    private String parentId;
    private String directionCode;
    private String userName;
    private String key;

    private String note;
    private SiteInfo siteCorrect;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SiteInfo getSiteCorrect() {
        return siteCorrect;
    }

    public void setSiteCorrect(SiteInfo siteCorrect) {
        this.siteCorrect = siteCorrect;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Boolean getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Boolean reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public SiteInfo getSite() {
        return site;
    }

    public void setSite(SiteInfo site) {
        this.site = site;
    }

    public LanesRoute getLane() {
        return lane;
    }

    public void setLane(LanesRoute lane) {
        this.lane = lane;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getImageTime() {
        return imageTime;
    }

    public void setImageTime(Date imageTime) {
        this.imageTime = imageTime;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectCreate() {
        return objectCreate;
    }

    public void setObjectCreate(String objectCreate) {
        this.objectCreate = objectCreate;
    }

    public Float getSpeedOfVehicle() {
        return speedOfVehicle;
    }

    public void setSpeedOfVehicle(Float speedOfVehicle) {
        this.speedOfVehicle = speedOfVehicle;
    }

    public SiteInfo getEndSite() {
        return endSite;
    }

    public void setEndSite(SiteInfo endSite) {
        this.endSite = endSite;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getListSite() {
        return listSite;
    }

    public void setListSite(String listSite) {
        this.listSite = listSite;
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedAction() {
        return modifiedAction;
    }

    public void setModifiedAction(String modifiedAction) {
        this.modifiedAction = modifiedAction;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getIsNewest() {
        return isNewest;
    }

    public void setIsNewest(Integer isNewest) {
        this.isNewest = isNewest;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getEventIdString() {
        return eventIdString;
    }

    public void setEventIdString(String eventIdString) {
        this.eventIdString = eventIdString;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
