/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model;

import com.elcom.its.vds.convert.HashMapConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "pu_video_threads")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PuVideoThreads.findAll", query = "SELECT p FROM PuVideoThreads p")})
public class PuVideoThreads implements Serializable {

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
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "auto_adjust")
    private int autoAdjust;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cache_length")
    private int cacheLength;
    
    @Size(max = 255)
    @Column(name = "cache_url")
    private String cacheUrl;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> capability;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> detectors;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> render;
    
    @Column(name = "status")
    private Integer status;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "camera_id")
    private String cameraId;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "layout_id")
    private Long layoutId;

    @ManyToOne
    @JoinColumn(name = "process_unit_id", nullable = false)
    @JsonIgnore
    private ProcessUnit processUnit;

    public PuVideoThreads() {
    }

    public PuVideoThreads(Long id) {
        this.id = id;
    }

    public PuVideoThreads(Long id, int autoAdjust, int cacheLength, String cameraId, Long layoutId) {
        this.id = id;
        this.autoAdjust = autoAdjust;
        this.cacheLength = cacheLength;
        this.cameraId = cameraId;
        this.layoutId = layoutId;
    }

    public ProcessUnit getProcessUnit() {
        return processUnit;
    }

    public void setProcessUnit(ProcessUnit processUnit) {
        this.processUnit = processUnit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getAutoAdjust() {
        return autoAdjust;
    }

    public void setAutoAdjust(int autoAdjust) {
        this.autoAdjust = autoAdjust;
    }

    public int getCacheLength() {
        return cacheLength;
    }

    public void setCacheLength(int cacheLength) {
        this.cacheLength = cacheLength;
    }

    public String getCacheUrl() {
        return cacheUrl;
    }

    public void setCacheUrl(String cacheUrl) {
        this.cacheUrl = cacheUrl;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }
//
//    public String getProcessUnitId() {
//        return processUnitId;
//    }
//
//    public void setProcessUnitId(String processUnitId) {
//        this.processUnitId = processUnitId;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PuVideoThreads)) {
            return false;
        }
        PuVideoThreads other = (PuVideoThreads) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.config.model.PuVideoThreads[ id=" + id + " ]";
    }

}
