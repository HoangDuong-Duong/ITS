package com.elcom.its.management.validation;

import com.elcom.its.management.dto.CommonProperty;
import com.elcom.its.management.model.Job;
import com.elcom.its.utils.StringUtil;

public class PropertyValidation extends AbstractValidation{

    public String validateProperty(CommonProperty commonProperty) {
        if(StringUtil.isNullOrEmpty(commonProperty.getName())){
            getMessageDes().add("Tên không được để trống");
        }
        if(StringUtil.isNullOrEmpty(commonProperty.getPosition())){
            getMessageDes().add("Lý trình không được để trống");
        }
        if(commonProperty.getM()==null || commonProperty.getM() >1000 || commonProperty.getM() <0){
            getMessageDes().add("Lý trình m nằm trong khoảng từ 0 đến 999");
        }
        if(commonProperty.getKm()==null || commonProperty.getKm() > 280  || commonProperty.getKm() <200){
            getMessageDes().add("Lý trình Km nằm trong khoảng từ 200 đến 280");
        }
        if(commonProperty.getType()==null ){
            getMessageDes().add("Loại thiết bị không tồn tại");
        }
        if(commonProperty.getStatus()==null ){
            getMessageDes().add("Trạng thái thiết bị không tồn tại");
        }
        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validatePropertyHistory(String startTime, String endTime, String propertyId) {
        if(StringUtil.isNullOrEmpty(startTime)){
            getMessageDes().add("Thời gian bắt đầu không thể trống");
        }
        if(StringUtil.isNullOrEmpty(endTime)){
            getMessageDes().add("Thời gian kết thúc không thể trống");
        }
        if(StringUtil.isNullOrEmpty(propertyId)){
            getMessageDes().add(" Id tài sản không thể trống");
        }
        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateGetPropertyHistory(String property, Integer page, Integer size ) {
        if(StringUtil.isNullOrEmpty(property)){
            getMessageDes().add("Id tài sản không thể trống");
        }
        if(page ==null){
            getMessageDes().add("page không thể trống");
        }
        if(size == null || size <=0){
            getMessageDes().add(" size không thể trống");
        }
        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
