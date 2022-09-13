/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model;

import com.elcom.its.vds.convert.HashMapConverter;
import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.PrePersist;
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
@Table(name = "servers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servers.findAll", query = "SELECT s FROM Servers s")})
public class Servers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Size(max = 36)
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    @Size(max = 36)
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
    @Column(name = "gpu")
    private int gpu;
    
    @Column(name = "hardware_attributes", columnDefinition = "json")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> hardwareAttributes;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Size(max = 200)
    @Column(name = "name")
    private String name;

    @Column(name = "os_attributes", columnDefinition = "json")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> osAttributes;

    @Column(name = "server_status")
    private String serverStatus;
    
    @Column(name = "software_attributes", columnDefinition = "json")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> softwareAttributes;
    
    @Column(name = "status")
    private Integer status;

    public Servers() {
    }

    public Servers(Long id) {
        this.id = id;
    }

    public Servers(Long id, int gpu, String ipAddress, int port) {
        this.id = id;
        this.gpu = gpu;
        this.ipAddress = ipAddress;
    }

    @PrePersist
    void preInsert() {
        if (this.getCreatedDate() == null) {
            this.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        }
        if (this.getStatus() == null) {
            this.setStatus(1);
        }
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

    public int getGpu() {
        return gpu;
    }

    public void setGpu(int gpu) {
        this.gpu = gpu;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    public Map<String, Object> getHardwareAttributes() {
        return hardwareAttributes;
    }

    public void setHardwareAttributes(Map<String, Object> hardwareAttributes) {
        this.hardwareAttributes = hardwareAttributes;
    }

    public Map<String, Object> getOsAttributes() {
        return osAttributes;
    }

    public void setOsAttributes(Map<String, Object> osAttributes) {
        this.osAttributes = osAttributes;
    }

    public Map<String, Object> getSoftwareAttributes() {
        return softwareAttributes;
    }

    public void setSoftwareAttributes(Map<String, Object> softwareAttributes) {
        this.softwareAttributes = softwareAttributes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        if (!(object instanceof Servers)) {
            return false;
        }
        Servers other = (Servers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.config.model.Servers[ id=" + id + " ]";
    }
}
