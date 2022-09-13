/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import com.elcom.its.config.convert.HashMapConverter;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "camera_layouts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CameraLayouts.findAll", query = "SELECT c FROM CameraLayouts c")})
public class CameraLayouts implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Column(name = "modified_by")
    private String modifiedBy;
    
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "height_img_scale")
    private int heightImgScale;
    
    @Size(max = 2147483647)
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> jsonCanvas;
    
    @Size(max = 2147483647)
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> layoutAreasJson;
    
    @Size(max = 2147483647)
    private String layoutImage;
    
    @Size(max = 2147483647)
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> metaAttributes;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "model_profile_id")
    private long modelProfileId;
    
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "origin_height")
    private int originHeight;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "origin_width")
    private int originWidth;
    
    @Size(max = 255)
    @Column(name = "scaled_fit_img")
    private String scaledFitImg;
    
    @Size(max = 255)
    @Column(name = "sized_img")
    private String sizedImg;
    
    @Column(name = "status")
    private Integer status;
    
    @Size(max = 255)
    @Column(name = "used_capture_image")
    private String usedCaptureImage;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    private int version;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "width_img_scale")
    private int widthImgScale;

    @Column(name = "layout_type")
    private int layoutType;

    @Basic(optional = false)
    @NotNull
    @Column(name = "camera_id")
    private String cameraId;

    public CameraLayouts() {
    }

    public CameraLayouts(Long id) {
        this.id = id;
    }

    public CameraLayouts(Long id, int heightImgScale, long modelProfileId, int originHeight, 
            int originWidth, int version, int widthImgScale, String cameraId) {
        this.id = id;
        this.heightImgScale = heightImgScale;
        this.modelProfileId = modelProfileId;
        this.originHeight = originHeight;
        this.originWidth = originWidth;
        this.version = version;
        this.widthImgScale = widthImgScale;
        this.cameraId = cameraId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHeightImgScale() {
        return heightImgScale;
    }

    public void setHeightImgScale(int heightImgScale) {
        this.heightImgScale = heightImgScale;
    }

    public Map<String, Object> getJsonCanvas() {
        return jsonCanvas;
    }

    public void setJsonCanvas(Map<String, Object> jsonCanvas) {
        this.jsonCanvas = jsonCanvas;
    }

    public Map<String, Object> getLayoutAreasJson() {
        return layoutAreasJson;
    }

    public void setLayoutAreasJson(Map<String, Object> layoutAreasJson) {
        this.layoutAreasJson = layoutAreasJson;
    }

    public String getLayoutImage() {
        return layoutImage;
    }

    public void setLayoutImage(String layoutImage) {
        this.layoutImage = layoutImage;
    }

    public Map<String, Object> getMetaAttributes() {
        return metaAttributes;
    }

    public void setMetaAttributes(Map<String, Object> metaAttributes) {
        this.metaAttributes = metaAttributes;
    }

    public long getModelProfileId() {
        return modelProfileId;
    }

    public void setModelProfileId(long modelProfileId) {
        this.modelProfileId = modelProfileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOriginHeight() {
        return originHeight;
    }

    public void setOriginHeight(int originHeight) {
        this.originHeight = originHeight;
    }

    public int getOriginWidth() {
        return originWidth;
    }

    public void setOriginWidth(int originWidth) {
        this.originWidth = originWidth;
    }

    public String getScaledFitImg() {
        return scaledFitImg;
    }

    public void setScaledFitImg(String scaledFitImg) {
        this.scaledFitImg = scaledFitImg;
    }

    public String getSizedImg() {
        return sizedImg;
    }

    public void setSizedImg(String sizedImg) {
        this.sizedImg = sizedImg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUsedCaptureImage() {
        return usedCaptureImage;
    }

    public void setUsedCaptureImage(String usedCaptureImage) {
        this.usedCaptureImage = usedCaptureImage;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getWidthImgScale() {
        return widthImgScale;
    }

    public void setWidthImgScale(int widthImgScale) {
        this.widthImgScale = widthImgScale;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
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
        if (!(object instanceof CameraLayouts)) {
            return false;
        }
        CameraLayouts other = (CameraLayouts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.config.model.CameraLayouts[ id=" + id + " ]";
    }

}
