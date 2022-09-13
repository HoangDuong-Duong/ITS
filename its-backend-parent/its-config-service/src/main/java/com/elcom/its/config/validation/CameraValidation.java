package com.elcom.its.config.validation;

import com.elcom.its.config.exception.ValidationException;
import com.elcom.its.config.model.dto.CameraCreateUpdateDTO;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ducduongn
 */
public class CameraValidation extends AbstractValidation{
    private static final Logger LOGGER = LoggerFactory.getLogger(CameraValidation.class);

    public String validateCamera(CameraCreateUpdateDTO cameraDTO) throws ValidationException {
        if (StringUtil.isNullOrEmpty(cameraDTO.getName())) {
            getMessageDes().add("tên camera không được để trống");
        }
        if (StringUtil.isNullOrEmpty(cameraDTO.getCameraKey())) {
            getMessageDes().add("Định danh camera không được để trống");
        }
        if (StringUtil.isNullOrEmpty(cameraDTO.getCameraModel())) {
            getMessageDes().add("Model hãng camera không được để trống");
        }
        if (StringUtil.isNullOrEmpty(cameraDTO.getCameraType())) {
            getMessageDes().add("Loại camera không được để trống");
        }
//        if (StringUtil.isNullOrEmpty(cameraDTO.getSiteId())) {
//            getMessageDes().add("ID vị trí giám sát đặt camera không được để trống");
//        }

//        if (cameraDTO.getLongitude() == null) {
//            getMessageDes().add("Kinh độ không được để trống");
//        } else if (cameraDTO.getLongitude() > 180 || cameraDTO.getLongitude() < -180) {
//            getMessageDes().add("Kinh độ không hợp lệ");
//        }
//        if (cameraDTO.getLatitude() == null) {
//            getMessageDes().add("Vĩ độ không được để trống");
//        } else if (cameraDTO.getLatitude() > 180 || cameraDTO.getLatitude() < -180) {
//            getMessageDes().add("Vĩ độ không hợp lệ");
//        }

        //Validate thông tin luồng HLS, RTSP, stream
        if (StringUtil.isNullOrEmpty(cameraDTO.getUrls().getLiveUrl())) {
            getMessageDes().add("Link luồng live không được để trống");
        }
        if (StringUtil.isNullOrEmpty(cameraDTO.getUrls().getStreamUrl())) {
            getMessageDes().add("Link luồng rtsp không được để trống");
        }

        if (cameraDTO.getPtz() != 0 && cameraDTO.getPtz() != 1) {
            getMessageDes().add("Ptz không đúng định dạng");
        }

        return !isValid() ? this.buildValidationMessage() : null;
    }
}
