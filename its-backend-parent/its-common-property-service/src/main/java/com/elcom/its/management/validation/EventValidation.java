package com.elcom.its.management.validation;

import com.elcom.its.utils.StringUtil;

public class EventValidation  extends  AbstractValidation{

    public String updateViolation(String id, String startTime, String sourceId, String eventCode, String plate, String laneId, String imageUrl, Integer eventStatus) {

        if (StringUtil.isNullOrEmpty(startTime)) {
            getMessageDes().add("Thời gian không được để trống");
        }

        if (StringUtil.isNullOrEmpty(id)) {
            getMessageDes().add("id không được để trống");
        }
        if (StringUtil.isNullOrEmpty(sourceId)) {
            getMessageDes().add("Camera không được để trống");
        }
        if (StringUtil.isNullOrEmpty(eventCode)) {
            getMessageDes().add("Mã sự kiện không được trống");
        }
        if (StringUtil.isNullOrEmpty(plate)) {
            getMessageDes().add("Biển số không thể trống");
        }
        if (StringUtil.isNullOrEmpty(laneId)) {
            getMessageDes().add("Thời gian không được để trống");
        }
        if (StringUtil.isNullOrEmpty(imageUrl)) {
            getMessageDes().add("Camera không được để trống");
        }
        if (eventStatus==null) {
            getMessageDes().add("Trạng thái sự kiện không được để trống");
        }

        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String updateSecurity(String id, String startTime, String sourceId, String eventCode, String laneId, String imageUrl, Integer eventStatus) {

        if (StringUtil.isNullOrEmpty(id)) {
            getMessageDes().add("Id không được để trống");
        }
        if (StringUtil.isNullOrEmpty(startTime)) {
            getMessageDes().add("Thời gian không được để trống");
        }
        if (StringUtil.isNullOrEmpty(sourceId)) {
            getMessageDes().add("Camera không được để trống");
        }
        if (StringUtil.isNullOrEmpty(eventCode)) {
            getMessageDes().add("Mã sự kiện không được trống");
        }

        if (StringUtil.isNullOrEmpty(laneId)) {
            getMessageDes().add("Thời gian không được để trống");
        }
        if (StringUtil.isNullOrEmpty(imageUrl)) {
            getMessageDes().add("Đường dẫn hình ảnh không được để trống");
        }
        if (eventStatus==null) {
            getMessageDes().add("Trạng thái sự kiện không được để trống");
        }
        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateManual(String id,String startTime, Integer eventStatus) {
//        if (StringUtil.isNullOrEmpty(id)) {
//            getMessageDes().add("id không được để trống");
//        }
        if (StringUtil.isNullOrEmpty(startTime)) {
            getMessageDes().add("Thời gian không được để trống");
        }
//        if (eventStatus==null) {
//            getMessageDes().add("Trạng thái sự kiện không được để trống");
//        }

        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateManual(String startTime, String eventCode, String siteId) {
        if (StringUtil.isNullOrEmpty(startTime)) {
            getMessageDes().add("Thời gian không được để trống");
        }
        if (StringUtil.isNullOrEmpty(siteId)) {
            getMessageDes().add("Vị trí không được để trống");
        }
        if (StringUtil.isNullOrEmpty(eventCode)) {
            getMessageDes().add("Mã sự kiện không được để trống");
        }

        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
