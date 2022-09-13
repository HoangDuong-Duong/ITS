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
import javax.xml.bind.annotation.XmlRootElement;
import com.elcom.its.config.convert.HashMapConverter;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "layout_areas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LayoutAreas.findAll", query = "SELECT l FROM LayoutAreas l")})
public class LayoutAreas implements Serializable {

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
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> jsonDetailArea;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "roi_type")
    private long roiType;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "layout_id")
    private long layoutId;

    public LayoutAreas() {
    }

    public LayoutAreas(Long id) {
        this.id = id;
    }

    public LayoutAreas(Long id, long roiType, long layoutId) {
        this.id = id;
        this.roiType = roiType;
        this.layoutId = layoutId;
    }

    public Long getId() {
        return id;
    }

    public Map<String, Object> getJsonDetailArea() {
        return jsonDetailArea;
    }

    public void setJsonDetailArea(Map<String, Object> jsonDetailArea) {
        this.jsonDetailArea = jsonDetailArea;
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

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public long getRoiType() {
        return roiType;
    }

    public void setRoiType(long roiType) {
        this.roiType = roiType;
    }

    public long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(long layoutId) {
        this.layoutId = layoutId;
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
        if (!(object instanceof LayoutAreas)) {
            return false;
        }
        LayoutAreas other = (LayoutAreas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.config.model.LayoutAreas[ id=" + id + " ]";
    }

}
