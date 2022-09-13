/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.validation;

import com.elcom.its.config.model.dto.VdsDTO;
import com.elcom.its.utils.StringUtil;

/**
 *
 * @author Admin
 */
public class VdsValidation extends AbstractValidation {

    public String validateInsertVds(VdsDTO vdsDTO) {

        if (StringUtil.isNullOrEmpty(vdsDTO.getCameraId())) {
            getMessageDes().add("cameraId không được để trống");
        }

        if (vdsDTO.getLayoutType() == null) {
            getMessageDes().add("layoutType không được để trống");
        } else if (vdsDTO.getLayoutType() != 1 && vdsDTO.getLayoutType() != 2) {
            getMessageDes().add("layoutType chỉ nhận giá trị 1 hoặc 2");
        }

        if (StringUtil.isNullOrEmpty(vdsDTO.getProcessUnitId())) {
            getMessageDes().add("processUnitId không được để trống");
        }
        if (vdsDTO.getCapability() == null) {
            getMessageDes().add("capability không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateUpdateVds(String id, VdsDTO vdsDTO) {

        if (StringUtil.isNullOrEmpty(id)) {
            getMessageDes().add("id không được để trống");
        }

        if (StringUtil.isNullOrEmpty(vdsDTO.getCameraId())) {
            getMessageDes().add("cameraId không được để trống");
        }

        if (vdsDTO.getLayoutType() == null) {
            getMessageDes().add("layoutType không được để trống");
        } else if (vdsDTO.getLayoutType() != 1 && vdsDTO.getLayoutType() != 2) {
            getMessageDes().add("layoutType chỉ nhận giá trị 1 hoặc 2");
        }

        if (StringUtil.isNullOrEmpty(vdsDTO.getProcessUnitId())) {
            getMessageDes().add("processUnitId không được để trống");
        }

        if (vdsDTO.getCapability() == null) {
            getMessageDes().add("capability không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateUpdateVdsVideoThreads(String id, VdsDTO vdsDTO) {
        if (StringUtil.isNullOrEmpty(id)) {
            getMessageDes().add("id không được để trống");
        }
        if (vdsDTO.getDetectors() == null) {
            getMessageDes().add("detectors không được để trống");
        }
        if (vdsDTO.getRender() == null) {
            getMessageDes().add("render không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateUpdateCameraStatus(String cameraId, Integer status) {
        if (StringUtil.isNullOrEmpty(cameraId)) {
            getMessageDes().add("cameraId không được để trống");
        }
        if (status == null) {
            getMessageDes().add("status không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
