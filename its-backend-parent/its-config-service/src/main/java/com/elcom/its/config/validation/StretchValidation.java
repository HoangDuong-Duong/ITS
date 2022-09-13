package com.elcom.its.config.validation;

import com.elcom.its.config.exception.ValidationException;
import com.elcom.its.config.model.Stretch;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StretchValidation extends AbstractValidation {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceValidation.class);

    public String validateInsertStretch(Stretch stretch) throws ValidationException {
        if (StringUtil.isNullOrEmpty(stretch.getName())) {
            getMessageDes().add("tên quãng đường không được để trống");
        }
        if (StringUtil.isNullOrEmpty(stretch.getSiteStart())) {
            getMessageDes().add("vị trí bắt đầu không được để trống");
        }
        if (StringUtil.isNullOrEmpty(stretch.getSiteEnd())) {
            getMessageDes().add("vị trí kết thúc không được để trống");
        }
        if (StringUtil.isNullOrEmpty(stretch.getDirectionCode())) {
            getMessageDes().add("direction code không được để trống");
        }
        if (StringUtil.isNullOrEmpty(stretch.getDirectionString())) {
            getMessageDes().add("direction string không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validationUpdateStretch(Stretch stretch) throws ValidationException {
        if (StringUtil.isNullOrEmpty(stretch.getName())) {
            getMessageDes().add("tên quãng đường không được để trống");
        }
        if (StringUtil.isNullOrEmpty(stretch.getSiteStart())) {
            getMessageDes().add("vị trí bắt đầu không được để trống");
        }
        if (StringUtil.isNullOrEmpty(stretch.getSiteEnd())) {
            getMessageDes().add("vị trí kết thúc không được để trống");
        }
        if (StringUtil.isNullOrEmpty(stretch.getDirectionCode())) {
            getMessageDes().add("direction code không được để trống");
        }
        if (StringUtil.isNullOrEmpty(stretch.getDirectionString())) {
            getMessageDes().add("direction string không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
