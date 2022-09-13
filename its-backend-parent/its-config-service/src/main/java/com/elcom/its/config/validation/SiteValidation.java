package com.elcom.its.config.validation;

import com.elcom.its.config.exception.ValidationException;
import com.elcom.its.config.model.Site;
import com.elcom.its.config.model.SiteInfo;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteValidation extends AbstractValidation{
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteValidation.class);

    public String validateInsertSite(Site site) throws ValidationException {
        if (StringUtil.isNullOrEmpty(site.getName())) {
            getMessageDes().add("name không được để trống");
        }
        if (site.getKm() == null) {
            getMessageDes().add("lý trình km không được để trống");
        }
        if (site.getM() == null) {
            getMessageDes().add("lý trình m không được để trống");
        }

        if (site.getLatitude() == null) {
            getMessageDes().add("vĩ độ không được để trống");
        }
        if (site.getLongitude() == null) {
            getMessageDes().add("kinh độ không được để trống");
        }
        if (StringUtil.isNullOrEmpty(site.getAddress())) {
            getMessageDes().add("địa chỉ không được để trống");
        }
        if (site.getWardId() == null) {
            getMessageDes().add("phường xã không được để trống");
        }
        if (site.getDistrictId() == null) {
            getMessageDes().add("quận huyện không được để trống");
        }

        if (site.getProvinceId() == null) {
            getMessageDes().add("tỉnh thành phố không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validationUpdateSite(Site site) throws ValidationException{
        if (StringUtil.isNullOrEmpty(site.getName())) {
            getMessageDes().add("name không được để trống");
        }
        if (site.getKm() == null) {
            getMessageDes().add("lý trình km không được để trống");
        }
        if (site.getM() == null) {
            getMessageDes().add("lý trình m không được để trống");
        }
        if (site.getLatitude() == null) {
            getMessageDes().add("vĩ độ không được để trống");
        }
        if (site.getLongitude() == null) {
            getMessageDes().add("kinh độ không được để trống");
        }
        if (StringUtil.isNullOrEmpty(site.getAddress())) {
            getMessageDes().add("địa chỉ không được để trống");
        }
        if (site.getWardId() == null) {
            getMessageDes().add("phường xã không được để trống");
        }
        if (site.getDistrictId() == null) {
            getMessageDes().add("quận huyện không được để trống");
        }

        if (site.getProvinceId() == null) {
            getMessageDes().add("tỉnh thành phố không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
