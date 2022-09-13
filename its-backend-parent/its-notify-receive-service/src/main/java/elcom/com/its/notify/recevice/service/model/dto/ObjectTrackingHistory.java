package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

public class ObjectTrackingHistory {
    private static final long serialVersionUID = 1L;

    private ObjectTrackingDTO objectTrackingId;

    private String id;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date startTime;

    private String sourceId;
    private SiteInfo site;
    private Float speed;
    private String imageUrl;
    private Short isNewest;
    private String note;
    private String createBy;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createDate;
    private Short processStatus;
    private String alertChannel;
    private Object alertReceiver;
    private String sourceName;
    private String queue;
    private String notifyTrigger;

    public ObjectTrackingHistory() {
    }

    public ObjectTrackingDTO getObjectTrackingId() {
        return objectTrackingId;
    }

    public void setObjectTrackingId(ObjectTrackingDTO objectTrackingId) {
        this.objectTrackingId = objectTrackingId;
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

    public String getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public SiteInfo getSite() {
        return this.site;
    }

    public void setSite(SiteInfo site) {
        this.site = site;
    }

    public Float getSpeed() {
        return this.speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Short getIsNewest() {
        return this.isNewest;
    }

    public void setIsNewest(Short isNewest) {
        this.isNewest = isNewest;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public Short getProcessStatus() {
        return this.processStatus;
    }

    public void setProcessStatus(Short processStatus) {
        this.processStatus = processStatus;
    }

    public String getAlertChannel() {
        return this.alertChannel;
    }

    public void setAlertChannel(String alertChannel) {
        this.alertChannel = alertChannel;
    }

    public Object getAlertReceiver() {
        return this.alertReceiver;
    }

    public void setAlertReceiver(Object alertReceiver) {
        this.alertReceiver = alertReceiver;
    }

    public String getSourceName() {
        return this.sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getQueue() {
        return this.queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getNotifyTrigger() {
        return this.notifyTrigger;
    }

    public void setNotifyTrigger(String notifyTrigger) {
        this.notifyTrigger = notifyTrigger;
    }


    public boolean equals(Object object) {
        if (!(object instanceof ObjectTrackingHistory)) {
            return false;
        } else {
            ObjectTrackingHistory other = (ObjectTrackingHistory)object;
            return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
        }
    }

    public String toString() {
        return "com.elcom.itscore.data.model.ObjectTrackingHistory[ objectTrackingHistoryPK=" + this.id + " ]";
    }
}
