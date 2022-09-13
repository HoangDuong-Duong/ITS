package com.elcom.its.tollstation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventFile implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private Date startTime;
    private String eventId;
    private String fileType;
    private String fileName;
    private String fileUrl;
    private Integer size;
    private Date uploadTime;
    private String createBy;
    private Date createDate;

    public EventFile() {
    }

    public EventFile(String id, Date startTime, String eventId, String fileType, String fileName, String fileUrl, Integer size) {
        this.id = id;
        this.startTime = startTime;
        this.eventId = eventId;
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.size = size;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Date getUploadTime() {
        return this.uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public boolean equals(Object object) {
        if (!(object instanceof EventFile)) {
            return false;
        } else {
            EventFile other = (EventFile) object;
            return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
        }
    }

    public String toString() {
        return "com.elcom.itsmanagement.model.EventFile[ id=" + this.id + " ]";
    }
}
