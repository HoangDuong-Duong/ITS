package elcom.com.its.notify.recevice.service.model.dto;

public class SiteInfoConfig {
    public String siteId;
    public String siteName;
    public String siteAddress;
    public double longitude;
    public double latitude;
    public Long provinceId;
    public String provinceName;
    public Long districtId;
    public String districtName;
    public Long wardId;
    public String wardName;
    public Long positionM;
    public Integer km;
    public Integer m;
    public String code;
    public String note;

    public static SiteInfoConfig.SiteInfoConfigBuilder builder() {
        return new SiteInfoConfig.SiteInfoConfigBuilder();
    }

    public String getSiteId() {
        return this.siteId;
    }

    public String getSiteName() {
        return this.siteName;
    }

    public String getSiteAddress() {
        return this.siteAddress;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public Long getProvinceId() {
        return this.provinceId;
    }

    public String getProvinceName() {
        return this.provinceName;
    }

    public Long getDistrictId() {
        return this.districtId;
    }

    public String getDistrictName() {
        return this.districtName;
    }

    public Long getWardId() {
        return this.wardId;
    }

    public String getWardName() {
        return this.wardName;
    }

    public Long getPositionM() {
        return this.positionM;
    }

    public Integer getKm() {
        return this.km;
    }

    public Integer getM() {
        return this.m;
    }

    public String getCode() {
        return this.code;
    }

    public String getNote() {
        return this.note;
    }

    public void setSiteId(final String siteId) {
        this.siteId = siteId;
    }

    public void setSiteName(final String siteName) {
        this.siteName = siteName;
    }

    public void setSiteAddress(final String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public void setProvinceId(final Long provinceId) {
        this.provinceId = provinceId;
    }

    public void setProvinceName(final String provinceName) {
        this.provinceName = provinceName;
    }

    public void setDistrictId(final Long districtId) {
        this.districtId = districtId;
    }

    public void setDistrictName(final String districtName) {
        this.districtName = districtName;
    }

    public void setWardId(final Long wardId) {
        this.wardId = wardId;
    }

    public void setWardName(final String wardName) {
        this.wardName = wardName;
    }

    public void setPositionM(final Long positionM) {
        this.positionM = positionM;
    }

    public void setKm(final Integer km) {
        this.km = km;
    }

    public void setM(final Integer m) {
        this.m = m;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setNote(final String note) {
        this.note = note;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SiteInfoConfig)) {
            return false;
        } else {
            SiteInfoConfig other = (SiteInfoConfig)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (Double.compare(this.getLongitude(), other.getLongitude()) != 0) {
                return false;
            } else if (Double.compare(this.getLatitude(), other.getLatitude()) != 0) {
                return false;
            } else {
                label184: {
                    Object this$provinceId = this.getProvinceId();
                    Object other$provinceId = other.getProvinceId();
                    if (this$provinceId == null) {
                        if (other$provinceId == null) {
                            break label184;
                        }
                    } else if (this$provinceId.equals(other$provinceId)) {
                        break label184;
                    }

                    return false;
                }

                Object this$districtId = this.getDistrictId();
                Object other$districtId = other.getDistrictId();
                if (this$districtId == null) {
                    if (other$districtId != null) {
                        return false;
                    }
                } else if (!this$districtId.equals(other$districtId)) {
                    return false;
                }

                label170: {
                    Object this$wardId = this.getWardId();
                    Object other$wardId = other.getWardId();
                    if (this$wardId == null) {
                        if (other$wardId == null) {
                            break label170;
                        }
                    } else if (this$wardId.equals(other$wardId)) {
                        break label170;
                    }

                    return false;
                }

                label163: {
                    Object this$positionM = this.getPositionM();
                    Object other$positionM = other.getPositionM();
                    if (this$positionM == null) {
                        if (other$positionM == null) {
                            break label163;
                        }
                    } else if (this$positionM.equals(other$positionM)) {
                        break label163;
                    }

                    return false;
                }

                Object this$km = this.getKm();
                Object other$km = other.getKm();
                if (this$km == null) {
                    if (other$km != null) {
                        return false;
                    }
                } else if (!this$km.equals(other$km)) {
                    return false;
                }

                Object this$m = this.getM();
                Object other$m = other.getM();
                if (this$m == null) {
                    if (other$m != null) {
                        return false;
                    }
                } else if (!this$m.equals(other$m)) {
                    return false;
                }

                label142: {
                    Object this$siteId = this.getSiteId();
                    Object other$siteId = other.getSiteId();
                    if (this$siteId == null) {
                        if (other$siteId == null) {
                            break label142;
                        }
                    } else if (this$siteId.equals(other$siteId)) {
                        break label142;
                    }

                    return false;
                }

                label135: {
                    Object this$siteName = this.getSiteName();
                    Object other$siteName = other.getSiteName();
                    if (this$siteName == null) {
                        if (other$siteName == null) {
                            break label135;
                        }
                    } else if (this$siteName.equals(other$siteName)) {
                        break label135;
                    }

                    return false;
                }

                Object this$siteAddress = this.getSiteAddress();
                Object other$siteAddress = other.getSiteAddress();
                if (this$siteAddress == null) {
                    if (other$siteAddress != null) {
                        return false;
                    }
                } else if (!this$siteAddress.equals(other$siteAddress)) {
                    return false;
                }

                label121: {
                    Object this$provinceName = this.getProvinceName();
                    Object other$provinceName = other.getProvinceName();
                    if (this$provinceName == null) {
                        if (other$provinceName == null) {
                            break label121;
                        }
                    } else if (this$provinceName.equals(other$provinceName)) {
                        break label121;
                    }

                    return false;
                }

                Object this$districtName = this.getDistrictName();
                Object other$districtName = other.getDistrictName();
                if (this$districtName == null) {
                    if (other$districtName != null) {
                        return false;
                    }
                } else if (!this$districtName.equals(other$districtName)) {
                    return false;
                }

                label107: {
                    Object this$wardName = this.getWardName();
                    Object other$wardName = other.getWardName();
                    if (this$wardName == null) {
                        if (other$wardName == null) {
                            break label107;
                        }
                    } else if (this$wardName.equals(other$wardName)) {
                        break label107;
                    }

                    return false;
                }

                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                Object this$note = this.getNote();
                Object other$note = other.getNote();
                if (this$note == null) {
                    if (other$note != null) {
                        return false;
                    }
                } else if (!this$note.equals(other$note)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SiteInfoConfig;
    }

    public int hashCode() {
//        int PRIME = true;
        int result = 1;
        long $longitude = Double.doubleToLongBits(this.getLongitude());
        result = result * 59 + (int)($longitude >>> 32 ^ $longitude);
        long $latitude = Double.doubleToLongBits(this.getLatitude());
        result = result * 59 + (int)($latitude >>> 32 ^ $latitude);
        Object $provinceId = this.getProvinceId();
        result = result * 59 + ($provinceId == null ? 43 : $provinceId.hashCode());
        Object $districtId = this.getDistrictId();
        result = result * 59 + ($districtId == null ? 43 : $districtId.hashCode());
        Object $wardId = this.getWardId();
        result = result * 59 + ($wardId == null ? 43 : $wardId.hashCode());
        Object $positionM = this.getPositionM();
        result = result * 59 + ($positionM == null ? 43 : $positionM.hashCode());
        Object $km = this.getKm();
        result = result * 59 + ($km == null ? 43 : $km.hashCode());
        Object $m = this.getM();
        result = result * 59 + ($m == null ? 43 : $m.hashCode());
        Object $siteId = this.getSiteId();
        result = result * 59 + ($siteId == null ? 43 : $siteId.hashCode());
        Object $siteName = this.getSiteName();
        result = result * 59 + ($siteName == null ? 43 : $siteName.hashCode());
        Object $siteAddress = this.getSiteAddress();
        result = result * 59 + ($siteAddress == null ? 43 : $siteAddress.hashCode());
        Object $provinceName = this.getProvinceName();
        result = result * 59 + ($provinceName == null ? 43 : $provinceName.hashCode());
        Object $districtName = this.getDistrictName();
        result = result * 59 + ($districtName == null ? 43 : $districtName.hashCode());
        Object $wardName = this.getWardName();
        result = result * 59 + ($wardName == null ? 43 : $wardName.hashCode());
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $note = this.getNote();
        result = result * 59 + ($note == null ? 43 : $note.hashCode());
        return result;
    }

    public SiteInfoConfig() {
    }

    public SiteInfoConfig(final String siteId, final String siteName, final String siteAddress, final double longitude, final double latitude, final Long provinceId, final String provinceName, final Long districtId, final String districtName, final Long wardId, final String wardName, final Long positionM, final Integer km, final Integer m, final String code, final String note) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.siteAddress = siteAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.districtId = districtId;
        this.districtName = districtName;
        this.wardId = wardId;
        this.wardName = wardName;
        this.positionM = positionM;
        this.km = km;
        this.m = m;
        this.code = code;
        this.note = note;
    }

    public String toString() {
        String var10000 = this.getSiteId();
        return "SiteInfoConfig(siteId=" + var10000 + ", siteName=" + this.getSiteName() + ", siteAddress=" + this.getSiteAddress() + ", longitude=" + this.getLongitude() + ", latitude=" + this.getLatitude() + ", provinceId=" + this.getProvinceId() + ", provinceName=" + this.getProvinceName() + ", districtId=" + this.getDistrictId() + ", districtName=" + this.getDistrictName() + ", wardId=" + this.getWardId() + ", wardName=" + this.getWardName() + ", positionM=" + this.getPositionM() + ", km=" + this.getKm() + ", m=" + this.getM() + ", code=" + this.getCode() + ", note=" + this.getNote() + ")";
    }

    public static class SiteInfoConfigBuilder {
        private String siteId;
        private String siteName;
        private String siteAddress;
        private double longitude;
        private double latitude;
        private Long provinceId;
        private String provinceName;
        private Long districtId;
        private String districtName;
        private Long wardId;
        private String wardName;
        private Long positionM;
        private Integer km;
        private Integer m;
        private String code;
        private String note;

        SiteInfoConfigBuilder() {
        }

        public SiteInfoConfig.SiteInfoConfigBuilder siteId(final String siteId) {
            this.siteId = siteId;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder siteName(final String siteName) {
            this.siteName = siteName;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder siteAddress(final String siteAddress) {
            this.siteAddress = siteAddress;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder longitude(final double longitude) {
            this.longitude = longitude;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder latitude(final double latitude) {
            this.latitude = latitude;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder provinceId(final Long provinceId) {
            this.provinceId = provinceId;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder provinceName(final String provinceName) {
            this.provinceName = provinceName;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder districtId(final Long districtId) {
            this.districtId = districtId;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder districtName(final String districtName) {
            this.districtName = districtName;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder wardId(final Long wardId) {
            this.wardId = wardId;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder wardName(final String wardName) {
            this.wardName = wardName;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder positionM(final Long positionM) {
            this.positionM = positionM;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder km(final Integer km) {
            this.km = km;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder m(final Integer m) {
            this.m = m;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder code(final String code) {
            this.code = code;
            return this;
        }

        public SiteInfoConfig.SiteInfoConfigBuilder note(final String note) {
            this.note = note;
            return this;
        }

        public SiteInfoConfig build() {
            return new SiteInfoConfig(this.siteId, this.siteName, this.siteAddress, this.longitude, this.latitude, this.provinceId, this.provinceName, this.districtId, this.districtName, this.wardId, this.wardName, this.positionM, this.km, this.m, this.code, this.note);
        }

        public String toString() {
            return "SiteInfoConfig.SiteInfoConfigBuilder(siteId=" + this.siteId + ", siteName=" + this.siteName + ", siteAddress=" + this.siteAddress + ", longitude=" + this.longitude + ", latitude=" + this.latitude + ", provinceId=" + this.provinceId + ", provinceName=" + this.provinceName + ", districtId=" + this.districtId + ", districtName=" + this.districtName + ", wardId=" + this.wardId + ", wardName=" + this.wardName + ", positionM=" + this.positionM + ", km=" + this.km + ", m=" + this.m + ", code=" + this.code + ", note=" + this.note + ")";
        }
    }
}
