/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.uploadservice.controller;

import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.uploadservice.config.UploadConfig;
import com.elcom.its.uploadservice.constant.Constant;
import com.elcom.its.uploadservice.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.uploadservice.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.uploadservice.model.dto.ResponseMessageDTO;
import com.elcom.its.uploadservice.model.dto.UploadDTO;
import com.elcom.its.uploadservice.service.BucketService;
import com.elcom.its.uploadservice.service.impl.FileStorageServiceImpl;
import com.elcom.its.uploadservice.upload.UploadFileResponse;
import com.elcom.its.uploadservice.utils.DateUtil;
import com.elcom.its.uploadservice.validation.UploadValidation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/v1.0")
public class UploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private FileStorageServiceImpl fileStorageService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Value("${user.authen.use}")
    private String authenUse;

    @Value("${user.authen.http.url}")
    private String authenHttpUrl;

    @Autowired
    private BucketService bucketService;

    /**
     * Upload file
     *
     * @param files
     * @param keepFileName
     * @param localUpload
     * @param headerMap
     * @param request
     * @return image upload link
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @RequestMapping(value = "/upload/**", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadFile(@RequestParam(value = "file", required = false) MultipartFile[] files,
            @RequestParam(value = "keepFileName", required = false) Boolean keepFileName,
            @RequestParam(value = "localUpload", required = false) Boolean localUpload,
            @RequestHeader Map<String, String> headerMap, HttpServletRequest request) throws JsonProcessingException {

        if (bucketService.getBucket(request).tryConsume(1)) {
            //Request path
            String requestPath = request.getRequestURI();
            if (requestPath != null && requestPath.contains(Constant.API_ROOT_PATH)) {
                requestPath = requestPath.replace(Constant.API_ROOT_PATH, "/");
            }
            //Service
            int index = requestPath.indexOf("/", "/upload/".length());
            String service = null;
            if (index != -1) {
                service = requestPath.substring("/upload/".length(), index);
            } else {
                service = requestPath.replace("/upload/", "");
            }
            LOGGER.info("requestPath: {}, service: {}", requestPath, service);
            UploadDTO dto = UploadConfig.UPLOAD_DEFINE_MAP.get(requestPath);
            //Validation
            new UploadValidation().validate(requestPath, service, files, dto);

            //Authen 
            ResponseMessage response = null;
            if ("http".equalsIgnoreCase(authenUse)) {
                LOGGER.info("Http authen - authorization : {}", headerMap.get("authorization"));
                // Http -> Call api authen
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", headerMap.get("authorization"));
                headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
                headers.setContentType(MediaType.APPLICATION_JSON);

                // Dữ liệu đính kèm theo yêu cầu.
                HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<ResponseMessage> result = restTemplate.exchange(authenHttpUrl, HttpMethod.GET, requestEntity, ResponseMessage.class);
                if (result != null && result.getStatusCode() == HttpStatus.OK) {
                    response = result.getBody();
                }
                LOGGER.info("Http authen response : {}", response != null ? response.toJsonString() : null);
            } else {
                //Authen -> call rpc authen headerMap
                RequestMessage userRpcRequest = new RequestMessage();
                userRpcRequest.setRequestMethod("POST");
                userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_AUTHEN_URL);
                userRpcRequest.setBodyParam(null);
                userRpcRequest.setUrlParam(null);
                userRpcRequest.setHeaderParam(headerMap);
                LOGGER.info("Call RPC authen - authorization: {}", headerMap.get("authorization"));
                LOGGER.info("RequestMessage userRpcRequest : {}", userRpcRequest.toJsonString());
                String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                        RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
                LOGGER.info("RPC authen response : {}", result);
                if (result != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //mapper.setDateFormat(df);
                    try {
                        response = mapper.readValue(result, ResponseMessage.class);
                    } catch (JsonProcessingException ex) {
                        LOGGER.info("Lỗi parse json khi gọi user service verify: {}", ex.toString());
                    }
                }
            }

            if ((localUpload != null && localUpload == true) || (response != null && response.getStatus() == HttpStatus.OK.value())) {
                //Process upload
                String ddmmyyyy = DateUtil.today("ddMMyyyy");
                String uploadDir = dto.getFolder();

                List<UploadFileResponse> list = new ArrayList<>();
                UploadFileResponse uploadFileResponse = null;
                for (MultipartFile file : files) {
                    String fileName = fileStorageService.storeFile(file, uploadDir, keepFileName);
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path(Constant.API_ROOT_PATH + uploadDir.replace("{ddmmyyyy}", ddmmyyyy) + "/")
                            .path(fileName)
                            .toUriString();
                    LOGGER.info("Upload file url: " + fileDownloadUri);
                    uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
                    list.add(uploadFileResponse);
                }
                //if (list != null && list.size() == 1) {
                //    return new ResponseEntity(new ResponseMessageDTO(list), HttpStatus.OK);
                //}
                return new ResponseEntity(new ResponseMessageDTO(list), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ResponseMessageDTO("Token đăng nhập hết hạn"), HttpStatus.FORBIDDEN);
            }
        } else {
            LOGGER.info("Too many request by ip {}",bucketService.getIpAddress(request));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

    }

    /**
     * View file upload
     *
     * @param request
     * @return file
     * @throws IOException
     */
    @RequestMapping(value = "/upload/**", method = RequestMethod.GET)
    public ResponseEntity<Resource> viewFile(HttpServletRequest request) throws IOException {

        if (bucketService.getBucket(request).tryConsume(1)) {
            String filePath = request.getRequestURI();
            LOGGER.info("view file: {}", filePath);
            if (filePath != null && filePath.contains("/v1.0/")) {
                filePath = filePath.replace("/v1.0/", "");
            }
            int lastIndex = filePath != null ? filePath.lastIndexOf("/") : -1;
            String fileName = lastIndex != -1 ? filePath.substring(lastIndex + 1) : filePath;

            // Fallback to the default content type if type could not be determined
            ContentDisposition contentDisposition = ContentDisposition.builder("inline").filename(fileName).build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(contentDisposition);

            // Load file as Resource
            Resource resource = fileStorageService.loadFileAsResource(filePath);
            // Try to determine file's content type
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                LOGGER.info("Không nhận dạng được kiểu file.ex: " + ex.toString());
            }
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(resource.contentLength())
                    .headers(headers)
                    .body(resource);
        } else {
            LOGGER.info("Too many request by ip {}",bucketService.getIpAddress(request));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

    }
}
