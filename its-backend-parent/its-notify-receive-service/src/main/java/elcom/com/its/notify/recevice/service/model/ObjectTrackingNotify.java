package elcom.com.its.notify.recevice.service.model;


import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Admin
 */
public class ObjectTrackingNotify implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    protected ObjectTrackingNotifyPK objectTrackNotifyPK;

    private String sourceId;

    private String sourceName;

    private String eventIdString;

    private String eventTypeString;

    private String eventTypeName;

    private Date startTime;

    private String identification;

    private Integer laneid;

    private Integer eventType;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Float speedOfVehicle;

    private String objectType;

    private String objectTypeName;
    private String imageUrl;

    private Float longitude;
    private Float latitude;

    private String bbox;
    private String site;
    private String alertChannel;

    private String receiver;
    private String id;
    private Date createdDate;

    public ObjectTrackingNotify() {
    }

    public ObjectTrackingNotify(ObjectTrackingNotifyPK objectTrackNotifyPK) {
        this.objectTrackNotifyPK = objectTrackNotifyPK;
    }

    public ObjectTrackingNotify(String id, Date createdDate) {
        this.objectTrackNotifyPK = new ObjectTrackingNotifyPK(id, createdDate);
    }

    @Override
    public ObjectTrackingNotify clone() throws CloneNotSupportedException{
        return (ObjectTrackingNotify) super.clone();
    }

    public Object getObjectTrackingNotifyPK() {
        return objectTrackNotifyPK;
    }

    public void setObjectTrackingNotifyPK(ObjectTrackingNotifyPK objectTrackNotifyPK) {
        this.objectTrackNotifyPK = objectTrackNotifyPK;
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

    public String getEventIdString() {
        return eventIdString;
    }

    public void setEventIdString(String eventIdString) {
        this.eventIdString = eventIdString;
    }

    public String getEventTypeString() {
        return eventTypeString;
    }

    public void setEventTypeString(String eventTypeString) {
        this.eventTypeString = eventTypeString;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public Integer getLaneid() {
        return laneid;
    }

    public void setLaneid(Integer laneid) {
        this.laneid = laneid;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Float getSpeedOfVehicle() {
        return speedOfVehicle;
    }

    public void setSpeedOfVehicle(Float speedOfVehicle) {
        this.speedOfVehicle = speedOfVehicle;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectTypeName() {
        return objectTypeName;
    }

    public void setObjectTypeName(String objectTypeName) {
        this.objectTypeName = objectTypeName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getBbox() {
        return bbox;
    }

    public void setBbox(String bbox) {
        this.bbox = bbox;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getAlertChannel() {
        return alertChannel;
    }

    public void setAlertChannel(String alertChannel) {
        this.alertChannel = alertChannel;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getId() {
        if (objectTrackNotifyPK != null) {
            return objectTrackNotifyPK.getId();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        if (objectTrackNotifyPK != null) {
            return objectTrackNotifyPK.getCreatedDate();
        }
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objectTrackNotifyPK != null ? objectTrackNotifyPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjectTrackingNotify)) {
            return false;
        }
        ObjectTrackingNotify other = (ObjectTrackingNotify) object;
        if ((this.objectTrackNotifyPK == null && other.objectTrackNotifyPK != null) || (this.objectTrackNotifyPK != null && !this.objectTrackNotifyPK.equals(other.objectTrackNotifyPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.gsan.configgsxp.model.ObjectTrackingNotify[ objectTrackNotifyPK=" + objectTrackNotifyPK + " ]";
    }

}
