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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "process_unit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcessUnit.findAll", query = "SELECT p FROM ProcessUnit p")})
public class ProcessUnit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "id")
    private String id;

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
    private Map<String, Object> appServices;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "code")
    private String code;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 2147483647)
    @Column(name = "event_notifiers", columnDefinition = "json")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> eventNotifiers;

    @Size(max = 2147483647)
    @Column(name = "model_profiles", columnDefinition = "json")
    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> modelProfiles;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "type")
    private String type;

    @Size(max = 255)
    @Column(name = "note")
    private String note;

    @Column(name = "servers")
    private Long servers;

    @Column(name = "status")
    private Integer status;
    
    @Column(name = "version")
    private Integer version;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> tvRenders;

    @Column(name = "spec", columnDefinition = "json")
    private String spec;
    
    @Column(name = "pu_type")
    private Integer puType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "processUnit", cascade = {CascadeType.REFRESH, CascadeType.REMOVE})
    @JsonIgnore
    private List<ProcessUnitModels> processUnitModelses;

    //@OneToMany(fetch = FetchType.EAGER, mappedBy = "processUnit")
    //private List<PuVideoThreads> videoThreads;
    
    @Transient
    private String serverName;

    public ProcessUnit() {
    }

    public ProcessUnit(String id) {
        this.id = id;
    }

    public ProcessUnit(String id, String createdBy, Date createdDate, String modifiedBy, Date modifiedDate, Map<String, Object> appServices, String code, String description, Map<String, Object> eventNotifiers, Map<String, Object> modelProfiles, String name, String type, String note, Long servers, Integer status, Map<String, Object> tvRenders, String spec) {
        this.id = id;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.modifiedBy = modifiedBy;
        this.modifiedDate = modifiedDate;
        this.appServices = appServices;
        this.code = code;
        this.description = description;
        this.eventNotifiers = eventNotifiers;
        this.modelProfiles = modelProfiles;
        this.name = name;
        this.type = type;
        this.note = note;
        this.servers = servers;
        this.status = status;
        this.tvRenders = tvRenders;
        this.spec = spec;
    }

    public String getId() {
        return id;
    }

    //public List<PuVideoThreads> getVideoThreads() {
    //    return videoThreads;
    //}

    //public void setVideoThreads(List<PuVideoThreads> videoThreads) {
    //    this.videoThreads = videoThreads;
    //}

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public Map<String, Object> getAppServices() {
        return appServices;
    }

    public void setAppServices(Map<String, Object> appServices) {
        this.appServices = appServices;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getEventNotifiers() {
        return eventNotifiers;
    }

    public void setEventNotifiers(Map<String, Object> eventNotifiers) {
        this.eventNotifiers = eventNotifiers;
    }

    public Map<String, Object> getModelProfiles() {
        return modelProfiles;
    }

    public void setModelProfiles(Map<String, Object> modelProfiles) {
        this.modelProfiles = modelProfiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getServers() {
        return servers;
    }

    public void setServers(Long servers) {
        this.servers = servers;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, Object> getTvRenders() {
        return tvRenders;
    }

    public void setTvRenders(Map<String, Object> tvRenders) {
        this.tvRenders = tvRenders;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public Integer getPuType() {
        return puType;
    }

    public void setPuType(Integer puType) {
        this.puType = puType;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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
        if (!(object instanceof ProcessUnit)) {
            return false;
        }
        ProcessUnit other = (ProcessUnit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.config.model.ProcessUnit[ id=" + id + " ]";
    }

}
