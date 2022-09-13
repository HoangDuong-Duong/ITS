package com.elcom.its.vds.validation;

import com.elcom.its.vds.model.dto.PuVideoThreadsDTO;
import com.elcom.its.utils.StringUtil;

public class PuVideoThreadsValidation extends AbstractValidation {

    public String validateInsertPuVideoThreads(PuVideoThreadsDTO puVideoThreadsDto) {

        if (StringUtil.isNullOrEmpty(puVideoThreadsDto.getCameraId())) {
            getMessageDes().add("Camera Id không được để trống");
        }
        if (puVideoThreadsDto.getLayoutId() == 0 || puVideoThreadsDto.getLayoutId() == null) {
            getMessageDes().add("Layout Id không được để trống");
        }
        if (StringUtil.isNullOrEmpty(puVideoThreadsDto.getIdProcessUnit())) {
            getMessageDes().add("Process Id không được để trống");
        }
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
