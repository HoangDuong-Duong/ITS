/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class Site implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private double longitude;
    private double latitude;
    private String address;
    private Wards wardId;
    private Province provinceId;
    private District districtId;

    private Integer km;
    private Integer m;
    private Long positionM;
    private String note;
    private String code;
    private String createdBy;
    private Date createdDate;
    private double longitudeTop;
    private double latitudeTop;
    private double longitudeBottom;
    private double latitudeBottom;
    private Float size;
    private TrafficStatus trafficStatusTop;
    private TrafficStatus trafficStatusBottom;

    public Site() {
    }

    public Site(String id) {
        this.id = id;
    }

    public Site(String id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Wards getWardId() {
        return wardId;
    }

    public void setWardId(Wards wardId) {
        this.wardId = wardId;
    }

    public Province getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Province provinceId) {
        this.provinceId = provinceId;
    }

    public District getDistrictId() {
        return districtId;
    }

    public void setDistrictId(District districtId) {
        this.districtId = districtId;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Integer getM() {
        return m;
    }

    public void setM(Integer m) {
        this.m = m;
    }

    public Long getPositionM() {
        return positionM;
    }

    public void setPositionM(Long positionM) {
        this.positionM = positionM;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public double getLongitudeTop() {
        return longitudeTop;
    }

    public void setLongitudeTop(double longitudeTop) {
        this.longitudeTop = longitudeTop;
    }

    public double getLatitudeTop() {
        return latitudeTop;
    }

    public void setLatitudeTop(double latitudeTop) {
        this.latitudeTop = latitudeTop;
    }

    public double getLongitudeBottom() {
        return longitudeBottom;
    }

    public void setLongitudeBottom(double longitudeBottom) {
        this.longitudeBottom = longitudeBottom;
    }

    public double getLatitudeBottom() {
        return latitudeBottom;
    }

    public void setLatitudeBottom(double latitudeBottom) {
        this.latitudeBottom = latitudeBottom;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public TrafficStatus getTrafficStatusTop() {
        return trafficStatusTop;
    }

    @JsonIgnore
    public void setTrafficStatusTop(TrafficStatus trafficStatusTop) {
        this.trafficStatusTop = trafficStatusTop;
    }

    @JsonIgnore
    public TrafficStatus getTrafficStatusBottom() {
        return trafficStatusBottom;
    }

    @JsonIgnore
    public void setTrafficStatusBottom(TrafficStatus trafficStatusBottom) {
        this.trafficStatusBottom = trafficStatusBottom;
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
        if (!(object instanceof Site)) {
            return false;
        }
        Site other = (Site) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.itscore.data.model.Site[ id=" + id + " ]";
    }

}
