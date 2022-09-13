/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.uploadservice.validation;

import com.elcom.its.uploadservice.config.UploadConfig;
import com.elcom.its.uploadservice.exception.ValidationException;
import com.elcom.its.uploadservice.model.dto.UploadDTO;
import com.elcom.its.uploadservice.utils.StringUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Admin
 */
public class UploadValidation extends AbstractValidation {

    public void validate(String requestPath, String service, MultipartFile[] files, UploadDTO dto) throws ValidationException {
        if (StringUtil.isNullOrEmpty(requestPath)) {
            getMessageDes().add("Path request không được trống");
        }
        //Service
        if (StringUtil.isNullOrEmpty(service) || !UploadConfig.UPLOAD_SERVICE_LIST.contains(service)) {
            getMessageDes().add("Service request không tồn tại");
        }
        //Path
        List<String> pathList = UploadConfig.UPLOAD_SERVICE_PATH_MAP.get(service);
        if (pathList == null || pathList.isEmpty() || !pathList.contains(requestPath)) {
            getMessageDes().add("Request path không tồn tại");
        }
        //File list
        if (files == null || files.length == 0) {
            getMessageDes().add("File không được trống");
        }
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                //Content type
                String contentType = file.getContentType();
                System.out.println("UploadValidation - validate - content type : " + contentType);
                if (dto == null || (dto.getAccept() != null && !dto.getAccept().equals("*") && !dto.getAccept().contains(contentType))) {
                    getMessageDes().add("Kiểu file upload không hợp lệ");
                } else {
                    //Check by mime magic
                    if (UploadConfig.CHECK_CONTENT_BY_MAGIC) {
                        try {
                            InputStream initialStream = file.getInputStream();
                            byte[] buffer = new byte[initialStream.available()];
                            initialStream.read(buffer);
                            if (buffer == null || buffer.length == 0) {
                                getMessageDes().add("Kiểu file upload " + contentType + " không hợp lệ do không lấy được thông tin file");
                            } else {
                                MagicMatch match = Magic.getMagicMatch(buffer, false);
                                if (match != null) {
                                    contentType = match.getMimeType();
                                    System.out.println("UploadValidation - validate by Mime Magic - content type : " + contentType);
                                    if (dto == null || (dto.getAccept() != null && !dto.getAccept().equals("*") && !dto.getAccept().contains(contentType))) {
                                        getMessageDes().add("Kiểu file upload " + contentType + " không hợp lệ");
                                    }
                                }
                            }
                        } catch (IOException | MagicParseException | MagicMatchNotFoundException | MagicException ex) {
                            Logger.getLogger(UploadValidation.class.getName()).log(Level.SEVERE, null, ex);
                            ex.printStackTrace();
                        }
                    }
                }
                //File size
                if (dto == null || dto.getMaxSize() == null || dto.getMaxSize().compareTo(file.getSize()) < 0) {
                    getMessageDes().add("File upload dung lượng tối đa " + (dto != null ? dto.getMaxSize() / 1024 + " KB" : " chưa định nghĩa"));
                }
            }
        }

        if (!isValid()) {
            throw new ValidationException(buildValidationMessage());
        }
    }
}
