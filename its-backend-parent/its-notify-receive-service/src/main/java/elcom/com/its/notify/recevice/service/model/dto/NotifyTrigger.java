package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Admin
 */
public class NotifyTrigger implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String userId;
    private String deviceId;
    private String siteIds;
    private String eventType;
    private Date fromDate;
    private Date toDate;
    private Integer status;
    private Date createdDate;
    private Integer typeMessage;
    private String notifyParameter;
    private String deviceType;
    private String systemType;
    private String detailType;



    public NotifyTrigger() {
    }

    public NotifyTrigger(String id) {
        this.id = id;
    }

    public NotifyTrigger(String id, String userId, String deviceId, String siteIds, String eventType, String detailType, Date fromDate, Date toDate, Integer status, Date createdDate, Integer typeMessage, String notifyParameter, String deviceType, String systemType) {
        this.id = id;
        this.userId = userId;
        this.deviceId = deviceId;
        this.siteIds = siteIds;
        this.eventType = eventType;
        this.detailType = detailType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.createdDate = createdDate;
        this.typeMessage = typeMessage;
        this.notifyParameter = notifyParameter;
        this.deviceType = deviceType;
        this.systemType = systemType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDetailType() {
        return deviceType;
    }

    public void setDetailType(String detailType) {this.detailType = detailType;}

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(Integer typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getNotifyParameter() {
        return notifyParameter;
    }

    public void setNotifyParameter(String notifyParameter) {
        this.notifyParameter = notifyParameter;
    }

    public String getSiteIds() {
        return siteIds;
    }

    public void setSiteIds(String siteIds) {
        this.siteIds = siteIds;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
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
        if (!(object instanceof NotifyTrigger)) {
            return false;
        }
        NotifyTrigger other = (NotifyTrigger) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.notify.data.model.NotifyTrigger[ id=" + id + " ]";
    }

}
