package com.elcom.its.management.dto;

import com.elcom.its.management.enums.EventStatus;
import com.elcom.its.management.enums.EventType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EventDTO {
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private String startTime;
    private String sourceId;
    private String sourceName;
    private SiteInfo site;
    private LanesRoute lane;
    private String videoUrl;
    private String imageUrl;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private String endTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private String imageTime;
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
    private EventStatus eventStatus;
    private Boolean reportStatus;

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

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date createDate;

    private String modifiedBy;

    private String modifiedAction;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    @Temporal(TemporalType.TIMESTAMP)
    private String modifiedDate;
    private Integer isNewest;
    private Integer isDelete;
    private String eventIdString;
    private String parentId;
    private String directionCode;
    private String userName;
    private String key;
}
