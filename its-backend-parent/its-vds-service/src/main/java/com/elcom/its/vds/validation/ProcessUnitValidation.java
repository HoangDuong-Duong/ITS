package com.elcom.its.vds.validation;

import com.elcom.its.vds.model.dto.ProcessUnitDTO;
import com.elcom.its.utils.StringUtil;

public class ProcessUnitValidation extends AbstractValidation {

    public String validateInsertProcessUnit(ProcessUnitDTO processUnitDto) {

        if (StringUtil.isNullOrEmpty(processUnitDto.getCode())) {
            getMessageDes().add("Mã khối xử lý không được để trống");
        }
        if (StringUtil.isNullOrEmpty(processUnitDto.getName())) {
            getMessageDes().add("Tên khối xử lý không được để trống");
        }
        if (StringUtil.isNullOrEmpty(processUnitDto.getType())) {
            getMessageDes().add("Loại khối xử lý không được để trống");
        }

        return !isValid() ? this.buildValidationMessage() : null;
    }

    public String validateAddServersToProcessUnit(String idProcessUnit, Long idServer) {

        if (StringUtil.isNullOrEmpty(idProcessUnit)) {
            getMessageDes().add("idProcessUnit không được để trống");
        }
        if (idServer == null) {
            getMessageDes().add("idServerkhông được để trống");
        }

        return !isValid() ? this.buildValidationMessage() : null;
    }
}
