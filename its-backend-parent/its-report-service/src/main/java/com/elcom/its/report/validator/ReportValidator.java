package com.elcom.its.report.validator;

import com.elcom.its.report.constant.Constant;
import com.elcom.its.utils.StringUtil;

import java.util.Arrays;

/**
 * @author ducduongn
 */
public class ReportValidator extends AbstractValidator {

    public String validateReportFilter(String filterTimeLevel, String filterObjectType) {
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

        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateObjectTrackingFilter(String processFromDate, String processToDate) {
        if (StringUtil.isNullOrEmpty(processFromDate)) {
            getMessageDes().add("Trường thời gian bắt đầu không được để trống");
        }
        if (StringUtil.isNullOrEmpty(processToDate)) {
            getMessageDes().add("Trường thời gian kết thúc không được để trống");
        }

        return !isValid() ? this.buildValidationMessage() : null;
    }
}
