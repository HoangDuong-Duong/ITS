/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.model.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import elcom.com.its.notify.recevice.service.enums.DataStatus;
import elcom.com.its.notify.recevice.service.enums.DeviceStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Camera {

    private String id;
    private String name;
    private String cameraKey;
    private String cameraModel;
    private String cameraType;
    private Sites siteId;
    private float longitude;
    private float latitude;
    private Object setupAttributes;
    private Object urls;
    private String note;
    private DataStatus status;
    private DeviceStatus cameraStatus;
    private String createdBy;
    private Date createdDate;
    private String modifiedBy;
    private Date modifiedDate;
    private String imageUrl;
    private List<Object> aiEngineJson;
    private String liveImagesUrl;
    private String vmsId;
    private Integer ptz;
    private String directionCode;
    private String directionString;
    private List<Object> activeLayouts;
    private List<Object> processUnitUse;
    private Object ptzInfo;

    public Camera() {
    }

    public Camera(String id) {
        this.id = id;
    }

    public Camera(String id, String name, String cameraKey, String cameraModel, Sites siteId,
                  float longitude, float latitude, Object setupAttributes, Object urls,
                  DataStatus status, String createdBy, String directionCode, String directionString) {
        this.id = id;
        this.name = name;
        this.cameraKey = cameraKey;
        this.cameraModel = cameraModel;
        this.siteId = siteId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.setupAttributes = setupAttributes;
        this.urls = urls;
        this.status = status;
        this.createdBy = createdBy;
        this.directionCode = directionCode;
        this.directionString = directionString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCameraKey() {
        return cameraKey;
    }

    public void setCameraKey(String cameraKey) {
        this.cameraKey = cameraKey;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public Sites getSiteId() {
        return siteId;
    }

    public void setSiteId(Sites siteId) {
        this.siteId = siteId;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public Object getSetupAttributes() {
        return setupAttributes;
    }

    public void setSetupAttributes(Object setupAttributes) {
        this.setupAttributes = setupAttributes;
    }

    public Object getUrls() {
        return urls;
    }

    public void setUrls(Object urls) {
        this.urls = urls;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public DataStatus getStatus() {
        return status;
    }

    public void setStatus(DataStatus status) {
        this.status = status;
    }

    public DeviceStatus getCameraStatus() {
        return cameraStatus;
    }

    public void setCameraStatus(DeviceStatus cameraStatus) {
        this.cameraStatus = cameraStatus;
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

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Object> getAiEngineJson() {
        return aiEngineJson;
    }

    public void setAiEngineJson(List<Object> aiEngineJson) {
        this.aiEngineJson = aiEngineJson;
    }

    public String getLiveImagesUrl() {
        return liveImagesUrl;
    }

    public void setLiveImagesUrl(String liveImagesUrl) {
        this.liveImagesUrl = liveImagesUrl;
    }

    public String getVmsId() {
        return vmsId;
    }

    public void setVmsId(String vmsId) {
        this.vmsId = vmsId;
    }

    public Integer getPtz() {
        return ptz;
    }

    public void setPtz(Integer ptz) {
        this.ptz = ptz;
    }

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public String getDirectionString() {
        return directionString;
    }

    public void setDirectionString(String directionString) {
        this.directionString = directionString;
    }

    public List<Object> getActiveLayouts() {
        return activeLayouts;
    }

    public void setActiveLayouts(List<Object> activeLayouts) {
        this.activeLayouts = activeLayouts;
    }

    public List<Object> getProcessUnitUse() {
        return processUnitUse;
    }

    public void setProcessUnitUse(List<Object> processUnitUse) {
        this.processUnitUse = processUnitUse;
    }

    public Object getPtzInfo() {
        return ptzInfo;
    }

    public void setPtzInfo(Object ptzInfo) {
        this.ptzInfo = ptzInfo;
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
        if (!(object instanceof Camera)) {
            return false;
        }
        Camera other = (Camera) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.itscore.data.model.Camera[ id=" + id + " ]";
    }
}
