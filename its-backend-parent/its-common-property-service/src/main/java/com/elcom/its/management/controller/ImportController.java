package com.elcom.its.management.controller;

import com.elcom.its.management.dto.ABACResponseDTO;
import com.elcom.its.management.dto.AuthorizationResponseDTO;
import com.elcom.its.management.dto.EventExport;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.service.impl.ImportExcel;
import com.elcom.its.management.validation.EventValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.DateUtil;
import com.elcom.its.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/v1.0")
public class ImportController extends BaseController {

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private ImportExcel importExcel;


    @RequestMapping(value = "/property/**", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("type") Integer type,
                                             @RequestHeader Map<String, String> headerMap, HttpServletRequest request) throws IOException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            return new ResponseEntity("Bạn chưa đăng nhập", HttpStatus.UNAUTHORIZED);
        } else {
//            EventExport eventExport = new EventExport();
//            ObjectMapper mapper = new ObjectMapper();
//            String msg = mapper.writeValueAsString(file);
//            rabbitMQClient.callWorkerService(queueExport, msg);

//            importExcel(file);
            importExcel.importExcel(file,dto.getUuid(), type);
            //Check RBAC quyền xử lý vi phạm

        }
        return  new ResponseEntity("oke", HttpStatus.OK);
    }

}
