/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.model;

import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.enums.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "job_backup")
@XmlRootElement
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
@NamedQueries({
    @NamedQuery(name = "JobBackup.findAll", query = "SELECT j FROM JobBackup j")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobBackup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "job_type")
    private String jobType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "group_id")
    private String groupId;
    @Column(name = "user_ids")
    private String userIds;
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date startTime;
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date endTime;
    @Size(max = 36)
    @Column(name = "direction")
    private String direction;
    @Size(max = 36)
    @Column(name = "start_site_id")
    private String startSiteId;
    @Size(max = 36)
    @Column(name = "end_site_id")
    private String endSiteId;
    @Size(max = 36)
    @Column(name = "lane")
    private String lane;
    @Basic(optional = false)
    @NotNull
    @Column(name = "priority")
    private Priority priority;
    @Size(max = 100)
    @Column(name = "vehicle_type")
    private String vehicleType;
    @Size(max = 100)
    @Column(name = "vehicle_weight")
    private String vehicleWeight;
    @Type(type = "jsonb")
    @Column(name = "devices", columnDefinition = "jsonb")
    private List<Device> listDevices;
    @Size(max = 500)
    @Column(name = "notify_method")
    private String notifyMethod;
    @Type(type = "jsonb")
    @Column(name = "notify_organization", columnDefinition = "jsonb")
    private List<String> notifyOrganization;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private JobStatus status;
    @Type(type = "jsonb")
    @Column(name = "action_history", columnDefinition = "jsonb")
    private List<ActionHistory> actionHistory;
    @Size(max = 500)
    @Column(name = "description")
    private String description;
    @Column(name = "event_id")
    private String eventId;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "limit_speed")
    private Integer limitSpeed;
    @Column(name = "place_id")
    private String placeId;
    @Column(name = "place_name")
    private String placeName;
    @Column(name = "created_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Type(type = "jsonb")
    @Column(name = "process_result", columnDefinition = "jsonb")
    private Object processResult;

    @Column(name = "event_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date eventStartTime;

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        mapper.setDateFormat(df);
        return mapper.writeValueAsString(this);
    }

    public JobBackup() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStartSiteId() {
        return startSiteId;
    }

    public void setStartSiteId(String startSiteId) {
        this.startSiteId = startSiteId;
    }

    public String getEndSiteId() {
        return endSiteId;
    }

    public void setEndSiteId(String endSiteId) {
        this.endSiteId = endSiteId;
    }

    public String getLane() {
        return lane;
    }

    public void setLane(String lane) {
        this.lane = lane;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleWeight() {
        return vehicleWeight;
    }

    public void setVehicleWeight(String vehicleWeight) {
        this.vehicleWeight = vehicleWeight;
    }

    public String getNotifyMethod() {
        return notifyMethod;
    }

    public void setNotifyMethod(String notifyMethod) {
        this.notifyMethod = notifyMethod;
    }

    public List<String> getNotifyOrganization() {
        return notifyOrganization;
    }

    public void setNotifyOrganization(List<String> notifyOrganization) {
        this.notifyOrganization = notifyOrganization;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public List<ActionHistory> getActionHistory() {
        return actionHistory;
    }

    public void setActionHistory(List<ActionHistory> actionHistory) {
        this.actionHistory = actionHistory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Object getProcessResult() {
        return processResult;
    }

    public void setProcessResult(Object processResult) {
        this.processResult = processResult;
    }

    public Date getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(Date eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public List<Device> getListDevices() {
        return listDevices;
    }

    public void setListDevices(List<Device> listDevices) {
        this.listDevices = listDevices;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
    

    public Integer getLimitSpeed() {
        return limitSpeed;
    }

    public void setLimitSpeed(int limitSpeed) {
        this.limitSpeed = limitSpeed;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JobBackup)) {
            return false;
        }
        JobBackup other = (JobBackup) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.management.model.Job[ id=" + id + " ]";
    }

}
