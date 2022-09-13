package com.elcom.its.config.validation;

import com.elcom.its.config.exception.ValidationException;
import com.elcom.its.config.model.dto.ObjectTrackingCreateUpdateDto;
import com.elcom.its.config.model.dto.ObjectTrackingProcessDTO;
import com.elcom.its.utils.StringUtil;

/**
 * @author ducduongn
 */
public class ObjectTrackingValidation extends AbstractValidation {

    public String validateObjectTracking(ObjectTrackingCreateUpdateDto createUpdateDto) throws ValidationException {
        //if (StringUtil.isNullOrEmpty(createUpdateDto.getModel())) {
        //    getMessageDes().add("Model không được để trống");
        //}
        //if (StringUtil.isNullOrEmpty(createUpdateDto.getAlertChannel())) {
        //    getMessageDes().add("Kênh thông báo không được để trống");
        //}
        if (StringUtil.isNullOrEmpty(createUpdateDto.getAlertReceiver())) {
            getMessageDes().add("Người nhận không được để trống");
        }
        if (StringUtil.isNullOrEmpty(createUpdateDto.getIndentification())) {
            getMessageDes().add("Biển số không được để trống");
        }
        if (StringUtil.isNullOrEmpty(createUpdateDto.getVehicleType())) {
            getMessageDes().add("Loại phương tiện không được để trống");
        }

        return !isValid() ? this.buildValidationMessage() : null;
    }
    
    public String validateObjectTrackingProcessed(ObjectTrackingProcessDTO createUpdateDto) throws ValidationException {
        if (StringUtil.isNullOrEmpty(createUpdateDto.getProcessDate())) {
            getMessageDes().add("Thời gian xử lý không được để trống");
        }
        if (StringUtil.isNullOrEmpty(createUpdateDto.getProcessNote())) {
            getMessageDes().add("Ghi chú xử lý không được để trống");
        }
        
        if (createUpdateDto.getProcessStatus() == null) {
            getMessageDes().add("Trạng thái xử lý không được để trống");
        }

        return !isValid() ? this.buildValidationMessage() : null;
    }
}
