/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model;

import com.elcom.its.vds.convert.HashMapConverter;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
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

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "vds")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vds.findAll", query = "SELECT v FROM Vds v")})
public class Vds implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "id")
    private String id;
    
    @Size(max = 255)
    @Column(name = "vds_key")
    private String vdsKey;
    
    @Size(max = 255)
    @Column(name = "vds_name")
    private String vdsName;
    
    @Size(max = 36)
    @Column(name = "camera_id")
    private String cameraId;
    
    @Size(max = 36)
    @Column(name = "process_unit_id")
    private String processUnitId;
    
    @Column(name = "layout_id")
    private Long layoutId;
    
    @Size(max = 255)
    @Column(name = "camera_name")
    private String cameraName;
    
    @Size(max = 36)
    @Column(name = "site_id")
    private String siteId;
    
    @Size(max = 255)
    @Column(name = "site_name")
    private String siteName;
    
    @Size(max = 255)
    @Column(name = "camera_type")
    private String cameraType;
    
    @Column(name = "camera_status")
    private Integer cameraStatus;
    
    @Column(name = "layout_type")
    private Integer layoutType;
    
    @Size(max = 255)
    @Column(name = "layout_type_name")
    private String layoutTypeName;
    
    @Size(max = 255)
    @Column(name = "process_unit_name")
    private String processUnitName;
    
    @Column(name = "status")
    private Integer status;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Size(max = 36)
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    
    @Size(max = 36)
    @Column(name = "modified_by")
    private String modifiedBy;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> capability;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> detectors;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> render;
    
    @Size(max = 36)
    @Column(name = "direction_code")
    private String directionCode;
    
    @Column(name = "render_vds")
    private Integer renderVds;

    public Vds() {
    }

    public Vds(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVdsKey() {
        return vdsKey;
    }

    public void setVdsKey(String vdsKey) {
        this.vdsKey = vdsKey;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getProcessUnitId() {
        return processUnitId;
    }

    public void setProcessUnitId(String processUnitId) {
        this.processUnitId = processUnitId;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public Integer getCameraStatus() {
        return cameraStatus;
    }

    public void setCameraStatus(Integer cameraStatus) {
        this.cameraStatus = cameraStatus;
    }

    public Integer getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(Integer layoutType) {
        this.layoutType = layoutType;
    }

    public String getLayoutTypeName() {
        return layoutTypeName;
    }

    public void setLayoutTypeName(String layoutTypeName) {
        this.layoutTypeName = layoutTypeName;
    }

    public String getProcessUnitName() {
        return processUnitName;
    }

    public void setProcessUnitName(String processUnitName) {
        this.processUnitName = processUnitName;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getVdsName() {
        return vdsName;
    }

    public void setVdsName(String vdsName) {
        this.vdsName = vdsName;
    }

    public Map<String, Object> getCapability() {
        return capability;
    }

    public void setCapability(Map<String, Object> capability) {
        this.capability = capability;
    }
    
    public Map<String, Object> getDetectors() {
        return detectors;
    }

    public void setDetectors(Map<String, Object> detectors) {
        this.detectors = detectors;
    }

    public Map<String, Object> getRender() {
        return render;
    }

    public void setRender(Map<String, Object> render) {
        this.render = render;
    }

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public Integer getRenderVds() {
        return renderVds;
    }

    public void setRenderVds(Integer renderVds) {
        this.renderVds = renderVds;
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
        if (!(object instanceof Vds)) {
            return false;
        }
        Vds other = (Vds) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.vds.model.Vds[ id=" + id + " ]";
    }

}
