package com.elcom.its.config.validation;

import com.elcom.its.config.model.dto.CameraLayoutDTO;
import com.elcom.its.utils.StringUtil;

public class CameraLayoutValidation extends AbstractValidation {

    public String validateInsertCameraLayout(CameraLayoutDTO cameraLayoutDto) {

        if (StringUtil.isNullOrEmpty(cameraLayoutDto.getCameraId())) {
            getMessageDes().add("Camera không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
