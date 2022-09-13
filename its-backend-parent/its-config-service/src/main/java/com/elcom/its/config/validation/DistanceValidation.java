package com.elcom.its.config.validation;

import com.elcom.its.config.exception.ValidationException;
import com.elcom.its.config.model.Distance;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistanceValidation extends AbstractValidation{
    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceValidation.class);

    public String validateInsertDistance(Distance distance) throws ValidationException {
        if (StringUtil.isNullOrEmpty(distance.getName())) {
            getMessageDes().add("tên quãng đường không được để trống");
        }
        if (StringUtil.isNullOrEmpty(distance.getSiteStart())) {
            getMessageDes().add("vị trí bắt đầu không được để trống");
        }
        if (StringUtil.isNullOrEmpty(distance.getSiteEnd())) {
            getMessageDes().add("vị trí kết thúc không được để trống");
        }if (StringUtil.isNullOrEmpty(distance.getDirectionCode())) {
            getMessageDes().add("direction code không được để trống");
        }if (StringUtil.isNullOrEmpty(distance.getDirectionString())) {
            getMessageDes().add("direction string không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validationUpdateDistance(Distance distance) throws ValidationException{
        if (StringUtil.isNullOrEmpty(distance.getName())) {
            getMessageDes().add("tên quãng đường không được để trống");
        }
        if (StringUtil.isNullOrEmpty(distance.getSiteStart())) {
            getMessageDes().add("vị trí bắt đầu không được để trống");
        }
        if (StringUtil.isNullOrEmpty(distance.getSiteEnd())) {
            getMessageDes().add("vị trí kết thúc không được để trống");
        }if (StringUtil.isNullOrEmpty(distance.getDirectionCode())) {
            getMessageDes().add("direction code không được để trống");
        }if (StringUtil.isNullOrEmpty(distance.getDirectionString())) {
            getMessageDes().add("direction string không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
