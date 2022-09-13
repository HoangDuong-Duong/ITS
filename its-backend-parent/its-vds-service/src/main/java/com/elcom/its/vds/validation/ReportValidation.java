package com.elcom.its.vds.validation;

import com.elcom.its.utils.StringUtil;
import com.elcom.its.vds.constant.Constant;

import java.util.Arrays;

public class ReportValidation extends AbstractValidation{
    public String validateInsertProcessUnit(String filterTimeLevel,
                                            String filterObjectType,
                                            Integer isAdminBackEnd) {

        if (StringUtil.isNullOrEmpty(filterTimeLevel)) {
            getMessageDes().add("Trường lọc theo thời gian không được để trống");
        } else if (!Arrays.asList(Constant.FILTER_TIME_LEVEL).contains(filterTimeLevel)) {
            getMessageDes().add("Trường lọc theo thời gian không hợp lệ, phải có giá trị sau: hour/ day/ week/ month/ year");
        }

        if (StringUtil.isNullOrEmpty(filterObjectType)) {
            getMessageDes().add("Trường lọc theo cam/site  không được để trống");
        } else if (!Arrays.asList(Constant.FILTER_OBJECT_TYPE).contains(filterObjectType)) {
            getMessageDes().add("Trường lọc theo cam/site không hợp lệ, phải có giá trị sau: cam/ site");
        }

        if (isAdminBackEnd == null) {
            getMessageDes().add("Trường admin không được để trống");
        }

        return !isValid() ? this.buildValidationMessage() : null;
    }
}
