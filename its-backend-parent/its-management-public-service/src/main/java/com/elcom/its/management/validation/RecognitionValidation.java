/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.validation;

import com.elcom.its.utils.StringUtil;

import java.util.List;

/**
 *
 * @author Admin
 */
public class RecognitionValidation extends AbstractValidation {

    public String validateRecognitionByCriteria(String fromDate, String toDate, Integer page, Integer size, String filterObjectType) {

        if (fromDate == null) {
            getMessageDes().add("fromDate không được để trống");
        }

        if (toDate == null) {
            getMessageDes().add("toDate không được để trống");
        }

        if (page == null) {
            getMessageDes().add("page không được để trống");
        }

        if (size == null || size ==0) {
            getMessageDes().add("size không được để trống");
        }

        if (!StringUtil.isNullOrEmpty(filterObjectType) && !"cam".equals(filterObjectType)
                && !"site".equals(filterObjectType)) {
            getMessageDes().add("filterObjectType chỉ nhận 1 trong 2 giá trị 'cam' hoặc 'site'");
        }

        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateRecognitionHistory(String fromDate, String toDate, Integer page, Integer size, String filterObjectType,String plate) {

        if (fromDate == null) {
            getMessageDes().add("fromDate không được để trống");
        }

        if (toDate == null) {
            getMessageDes().add("toDate không được để trống");
        }

        if (page == null) {
            getMessageDes().add("page không được để trống");
        }

        if (size == null || size ==0) {
            getMessageDes().add("size không được để trống");
        }

        if (StringUtil.isNullOrEmpty(plate)) {
            getMessageDes().add("plate không được để trống");
        }

        if (!StringUtil.isNullOrEmpty(filterObjectType) && !"cam".equals(filterObjectType)
                && !"site".equals(filterObjectType)) {
            getMessageDes().add("filterObjectType chỉ nhận 1 trong 2 giá trị 'cam' hoặc 'site'");
        }

        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateVehicleRoute(String fromDate, String toDate, String plate) {
        if (StringUtil.isNullOrEmpty(plate)) {
            getMessageDes().add("plate không được để trống");
        }
        if (fromDate == null) {
            getMessageDes().add("fromDate không được để trống");
        }

        if (toDate == null) {
            getMessageDes().add("toDate không được để trống");
        }
        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateCheckCameras(List<String> cameraIds) {
        if (cameraIds == null || cameraIds.isEmpty()) {
            return "Tham số truyền lên thiếu";
        }
        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateUpdateInfo(String brand, String color, String objectType, String reason, String imageUrl,String plate) {
        if (StringUtil.isNullOrEmpty(plate)) {
            getMessageDes().add( "Tham số truyền lên thiếu plate");
        }
        if (StringUtil.isNullOrEmpty(imageUrl)) {
            getMessageDes().add( "Tham số truyền lên thiếu imageUrl");
        }
        if (StringUtil.isNullOrEmpty(objectType)) {
            getMessageDes().add( "Tham số truyền lên thiếu objectType");
        }
        if (StringUtil.isNullOrEmpty(brand) ){
            getMessageDes().add( "Tham số truyền lên thiếu brand");
        }
//        if (StringUtil.isNullOrEmpty(color)) {
//            getMessageDes().add( "Tham số truyền lên thiếu color");
//        }

        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }

}
