package com.elcom.its.vds.validation;

import com.elcom.its.vds.model.dto.CameraLayoutDTO;
import com.elcom.its.utils.StringUtil;

public class CameraLayoutValidation extends AbstractValidation {

    public String validateInsertCameraLayout(CameraLayoutDTO cameraLayoutDto) {

        if (StringUtil.isNullOrEmpty(cameraLayoutDto.getCameraId())) {
            getMessageDes().add("Camera không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
