/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.model;

import com.elcom.its.config.convert.HashMapConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "model_profiles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModelProfiles.findAll", query = "SELECT m FROM ModelProfiles m")})
public class ModelProfiles implements Serializable {

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
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> filterObjects;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "hier")
    private double hier;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "input_rate")
    private int inputRate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_object")
    private int maxObject;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> metaAttributes;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "min_object")
    private int minObject;
    
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "nms")
    private double nms;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "output_height")
    private int outputHeight;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "output_rate")
    private int outputRate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "output_width")
    private int outputWidth;
    
    @Column(name = "status")
    private Integer status;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "threshold")
    private double threshold;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "camera_model_id")
    private long cameraModelId;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "modelProfiles", cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonIgnore
    private List<ProcessUnitModels> processUnitModelses;

    public ModelProfiles() {
    }

    public ModelProfiles(Long id) {
        this.id = id;
    }

    public ModelProfiles(Long id, String createdBy, Date createdDate, String modifiedBy, Date modifiedDate, String description, Map<String, Object> filterObjects, double hier, int inputRate, int maxObject, Map<String, Object> metaAttributes, int minObject, String name, double nms, int outputHeight, int outputRate, int outputWidth, Integer status, double threshold, long cameraModelId) {
        this.id = id;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
        this.description = description;
        this.filterObjects = filterObjects;
        this.hier = hier;
        this.inputRate = inputRate;
        this.maxObject = maxObject;
        this.metaAttributes = metaAttributes;
        this.minObject = minObject;
        this.name = name;
        this.nms = nms;
        this.outputHeight = outputHeight;
        this.outputRate = outputRate;
        this.outputWidth = outputWidth;
        this.status = status;
        this.threshold = threshold;
        this.cameraModelId = cameraModelId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getHier() {
        return hier;
    }

    public void setHier(double hier) {
        this.hier = hier;
    }

    public int getInputRate() {
        return inputRate;
    }

    public void setInputRate(int inputRate) {
        this.inputRate = inputRate;
    }

    public int getMaxObject() {
        return maxObject;
    }

    public void setMaxObject(int maxObject) {
        this.maxObject = maxObject;
    }

    public Map<String, Object> getFilterObjects() {
        return filterObjects;
    }

    public void setFilterObjects(Map<String, Object> filterObjects) {
        this.filterObjects = filterObjects;
    }

    public Map<String, Object> getMetaAttributes() {
        return metaAttributes;
    }

    public void setMetaAttributes(Map<String, Object> metaAttributes) {
        this.metaAttributes = metaAttributes;
    }

    public int getMinObject() {
        return minObject;
    }

    public void setMinObject(int minObject) {
        this.minObject = minObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getNms() {
        return nms;
    }

    public void setNms(double nms) {
        this.nms = nms;
    }

    public int getOutputHeight() {
        return outputHeight;
    }

    public void setOutputHeight(int outputHeight) {
        this.outputHeight = outputHeight;
    }

    public int getOutputRate() {
        return outputRate;
    }

    public void setOutputRate(int outputRate) {
        this.outputRate = outputRate;
    }

    public int getOutputWidth() {
        return outputWidth;
    }

    public void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public long getCameraModelId() {
        return cameraModelId;
    }

    public void setCameraModelId(long cameraModelId) {
        this.cameraModelId = cameraModelId;
    }

    public List<ProcessUnitModels> getProcessUnitModelses() {
        return processUnitModelses;
    }

    public void setProcessUnitModelses(List<ProcessUnitModels> processUnitModelses) {
        this.processUnitModelses = processUnitModelses;
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
        if (!(object instanceof ModelProfiles)) {
            return false;
        }
        ModelProfiles other = (ModelProfiles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.config.model.ModelProfiles[ id=" + id + " ]";
    }

}
