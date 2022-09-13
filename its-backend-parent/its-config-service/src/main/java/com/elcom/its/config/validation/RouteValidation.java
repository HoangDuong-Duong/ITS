package com.elcom.its.config.validation;

import com.elcom.its.config.exception.ValidationException;
import com.elcom.its.config.model.Route;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RouteValidation extends AbstractValidation {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteValidation.class);

    public String validateInsertRoute(Route route) throws ValidationException {
        if (StringUtil.isNullOrEmpty(route.getName())) {
            getMessageDes().add("tên quãng đường không được để trống");
        }
        if (StringUtil.isNullOrEmpty(route.getSiteStart())) {
            getMessageDes().add("vị trí bắt đầu không được để trống");
        }
        if (StringUtil.isNullOrEmpty(route.getSiteEnd())) {
            getMessageDes().add("vị trí kết thúc không được để trống");
        }
        if (StringUtil.isNullOrEmpty(route.getLine())) {
            getMessageDes().add("Lý trình không được để trống");
        }
        if (route.getNumberLanes() == null) {
            getMessageDes().add("số làn không được để trống");
        } else if (route.getNumberLanes() <= 0) {
            getMessageDes().add("Số làn đường phải dương");
        }
        if (route.getEmergencyStop() == null) {
            getMessageDes().add("Dừng làn khẩn cấp không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validationUpdateRoute(Route route) throws ValidationException {
        if (StringUtil.isNullOrEmpty(route.getName())) {
            getMessageDes().add("tên quãng đường không được để trống");
        }
        if (StringUtil.isNullOrEmpty(route.getSiteStart())) {
            getMessageDes().add("vị trí bắt đầu không được để trống");
        }
        if (StringUtil.isNullOrEmpty(route.getSiteEnd())) {
            getMessageDes().add("vị trí kết thúc không được để trống");
        }
        if (StringUtil.isNullOrEmpty(route.getLine())) {
            getMessageDes().add("Lý trình không được để trống");
        }
        if (route.getNumberLanes() == null) {
            getMessageDes().add("số làn không được để trống");
        } else if (route.getNumberLanes() <= 0) {
            getMessageDes().add("Số làn đường phải dương");
        }
        if (route.getEmergencyStop() == null) {
            getMessageDes().add("Dừng làn khẩn cấp không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
