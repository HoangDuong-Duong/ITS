package com.elcom.its.tollstation.validation;

import com.elcom.its.utils.StringUtil;

public class DeviceStatus extends  AbstractValidation{

    public String create( String deviceId, String siteId, String type, String startTime, String endTime, Integer status) {

        if (StringUtil.isNullOrEmpty(deviceId)) {
            getMessageDes().add("Id thiết bị không được để trống");
        }

        if (StringUtil.isNullOrEmpty(siteId)) {
            getMessageDes().add("Vị trí không được để trống");
        }
        if (StringUtil.isNullOrEmpty(type)) {
            getMessageDes().add("Camera không được để trống");
        }
        if (StringUtil.isNullOrEmpty(startTime)) {
            getMessageDes().add("Thời gian bắt đầu không được trống");
        }
        if (StringUtil.isNullOrEmpty(endTime)) {
            getMessageDes().add("Thời gian kết thúc không thể trống");
        }
        if (StringUtil.isNullOrEmpty(endTime)) {
            getMessageDes().add("Thời gian không được để trống");
        }
        if (status==null) {
            getMessageDes().add("Trạng thái sự kiện không được để trống");
        }

        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String update( String id,  String deviceId, String siteId, String type, String startTime, String endTime, Integer status) {

        if (StringUtil.isNullOrEmpty(id)) {
            getMessageDes().add("Id không được để trống");
        }
        if (StringUtil.isNullOrEmpty(deviceId)) {
            getMessageDes().add("Id thiết bị không được để trống");
        }

        if (StringUtil.isNullOrEmpty(siteId)) {
            getMessageDes().add("Vị trí không được để trống");
        }
        if (StringUtil.isNullOrEmpty(type)) {
            getMessageDes().add("Camera không được để trống");
        }
        if (StringUtil.isNullOrEmpty(startTime)) {
            getMessageDes().add("Thời gian bắt đầu không được trống");
        }
        if (StringUtil.isNullOrEmpty(endTime)) {
            getMessageDes().add("Thời gian kết thúc không thể trống");
        }
        if (StringUtil.isNullOrEmpty(endTime)) {
            getMessageDes().add("Thời gian không được để trống");
        }
        if (status==null) {
            getMessageDes().add("Trạng thái sự kiện không được để trống");
        }

        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
