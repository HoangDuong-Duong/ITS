package elcom.com.its.notify.recevice.service.model.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class Sites implements Serializable {
    private String id;
    private String name;
    private float longitude;
    private float latitude;
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
    private Float longitudeTop;
    private Float latitudeTop;
    private Float longitudeBottom;
    private Float latitudeBottom;
    private Float size;

    public Sites() {
    }

    public Sites(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Sites(String id, float longitude, float latitude) {
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

    public String getAddress() {
        return address;
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

    public Float getLongitudeTop() {
        return longitudeTop;
    }

    public void setLongitudeTop(Float longitudeTop) {
        this.longitudeTop = longitudeTop;
    }

    public Float getLatitudeTop() {
        return latitudeTop;
    }

    public void setLatitudeTop(Float latitudeTop) {
        this.latitudeTop = latitudeTop;
    }

    public Float getLongitudeBottom() {
        return longitudeBottom;
    }

    public void setLongitudeBottom(Float longitudeBottom) {
        this.longitudeBottom = longitudeBottom;
    }

    public Float getLatitudeBottom() {
        return latitudeBottom;
    }

    public void setLatitudeBottom(Float latitudeBottom) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sites)) {
            return false;
        }
        Sites other = (Sites) object;
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
