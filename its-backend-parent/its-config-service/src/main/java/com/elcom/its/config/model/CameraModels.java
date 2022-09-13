/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
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

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "camera_models")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CameraModels.findAll", query = "SELECT c FROM CameraModels c")})
public class CameraModels implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "created_by")
    private BigInteger createdBy;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Column(name = "modified_by")
    private BigInteger modifiedBy;
    
    @Column(name = "modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    
    @Size(max = 255)
    @Column(name = "label_file")
    private String labelFile;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "model_id")
    private long modelId;
    
    @Size(max = 200)
    @Column(name = "name")
    private String name;
    
    @Size(max = 255)
    @Column(name = "net_desc_file")
    private String netDescFile;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "static_object")
    private int staticObject;
    
    @Column(name = "status")
    private Integer status;
    
    @Size(max = 100)
    @Column(name = "sub_model")
    private String subModel;
    
    @Size(max = 255)
    @Column(name = "weight_file")
    private String weightFile;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "base_model_id")
    private long baseModelId;

    public CameraModels() {
    }

    public CameraModels(Long id) {
        this.id = id;
    }

    public CameraModels(Long id, long modelId, int staticObject, long baseModelId) {
        this.id = id;
        this.modelId = modelId;
        this.staticObject = staticObject;
        this.baseModelId = baseModelId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(BigInteger createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public BigInteger getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(BigInteger modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getLabelFile() {
        return labelFile;
    }

    public void setLabelFile(String labelFile) {
        this.labelFile = labelFile;
    }

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetDescFile() {
        return netDescFile;
    }

    public void setNetDescFile(String netDescFile) {
        this.netDescFile = netDescFile;
    }

    public int getStaticObject() {
        return staticObject;
    }

    public void setStaticObject(int staticObject) {
        this.staticObject = staticObject;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSubModel() {
        return subModel;
    }

    public void setSubModel(String subModel) {
        this.subModel = subModel;
    }

    public String getWeightFile() {
        return weightFile;
    }

    public void setWeightFile(String weightFile) {
        this.weightFile = weightFile;
    }

    public long getBaseModelId() {
        return baseModelId;
    }

    public void setBaseModelId(long baseModelId) {
        this.baseModelId = baseModelId;
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
        if (!(object instanceof CameraModels)) {
            return false;
        }
        CameraModels other = (CameraModels) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.config.model.CameraModels[ id=" + id + " ]";
    }
    
}
