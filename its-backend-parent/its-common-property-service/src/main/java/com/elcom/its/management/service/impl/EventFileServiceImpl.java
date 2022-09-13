/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.EventStatus;
import com.elcom.its.management.enums.EventType;
import com.elcom.its.management.enums.JobType;
import com.elcom.its.management.enums.PropertyType;
import com.elcom.its.management.export.WriteExcelProperty;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.management.model.ActionHistory;
import com.elcom.its.management.model.EventFile;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.service.EventFileService;
import com.elcom.its.management.service.PropertyService;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jsoup.Jsoup;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//import com.spire.pdf.FileFormat;
//import com.spire.pdf.PdfDocument;


/**
 *
 * @author Admin
 */
@Service
public class EventFileServiceImpl implements EventFileService {

    @Value("${url.image}")
    private String urlImage;
    @Value("${url.caotoc}")
    private String caotoc;


    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private PropertyService propertyService;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EventFileServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

//    @Value("${gsan.upload.path}")
//    private String path;
    
    @Override
    public EventFile createFileEventHistory(String name, String position, String types, String status, String sort, Integer page, Integer size, String stages, Boolean isAdmin, String uuid) throws Exception {
//        Document document = new Document(PageSize.A4, 30f, 15f, 20f, 20f);
////        String fileName = UUID.randomUUID().toString() + ".pdf";
////        String fileName = "test1" + ".pdf";
////        String urlFile = "" + fileName;
//        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        DateFormat datePasser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        DateFormat time = new SimpleDateFormat("HH-mm-ss");
//        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
////        Date time = eventDTO.getStartTime();
////        String hour = timeFormat.format(time);
////        String date = dateFormat.format(time);
////        Path pathh = Paths.get(urlFile);
//        Long sizeFile = new Long(0);
//        try {
////            http://103.21.151.166:8682/v1.0/management/event/history/b5645c1d-9613-462a-bc47-8daa362c5c5c b5645c1d-9613-462a-bc47-8daa362c5c5c 18a98f84-2ef9-44b9-931a-1b0a777359e5 fbefb6d2-75b7-4e28-836a-fb072975a69d fbefb6d2-75b7-4e28-836a-fb072975a69d
//            PropertyResponseDTO propertyResponseDTO = propertyService.findProperty(name,position,types,status,sort,page,size,stages,isAdmin);
//            if (propertyResponseDTO != null && propertyResponseDTO.getData() != null  && !propertyResponseDTO.getData().isEmpty()) {
//                Map<String,User> mapUser = getAllUser();
//                List<CommonProperty> commonPropertyList = propertyResponseDTO.getData();
//                Date now = new Date();
//                int rowIndex = 0;
//                DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
//                String today = df.format(new Date());
//                final String excelFilePath = "Tài sản"+ ".xlsx";
//                ObjectMapper mapper = new ObjectMapper();
//                Workbook workbook = WriteExcelProperty.getWorkbook(excelFilePath);
//
//                if(commonPropertyList!=null && !commonPropertyList.isEmpty()){
//
//                    if(commonPropertyList.get(0).getType().code()== PropertyType.BRIDGE.code()) {
//                        // Create sheet
//                        Sheet sheet = workbook.createSheet("Cầu"); // Create sheet with sheet name
//
//                        //        // Write header
//                        WriteExcelProperty.writeHeaderBridge(sheet, 0);
//                        WriteExcelProperty.writeExcelBridge(commonPropertyList, 3+3, sheet);
//                    } else if(commonPropertyList.get(0).getType().code()== PropertyType.DRAIN.code()) {
//                        // Create sheet
//                        Sheet sheet = workbook.createSheet("Cống"); // Create sheet with sheet name
//
//                        //        // Write header
//                        WriteExcelProperty.writeHeaderDrain(sheet, 0);
//                        WriteExcelProperty.writeExcelDrain(commonPropertyList, 3+1, sheet);
//                    }  else if(commonPropertyList.get(0).getType().code()== PropertyType.TRENCH.code()) {
//                        // Create sheet
//                        Sheet sheet = workbook.createSheet("Rãnh hở"); // Create sheet with sheet name
//
//                        //        // Write header
//                        WriteExcelProperty.writeHeaderTrench(sheet, 0);
//                        WriteExcelProperty.writeExcelTrench(commonPropertyList, 3+2, sheet);
//                    }  else if(commonPropertyList.get(0).getType().code()== PropertyType.TRAFFIC_SIGNS.code()) {
//                        // Create sheet
//                        Sheet sheet = workbook.createSheet("Biển báo"); // Create sheet with sheet name
//
//                        //        // Write header
//                        WriteExcelProperty.writeHeaderTrafficSigns(sheet, 0);
//                        WriteExcelProperty.writeExcelTrafficSigns(commonPropertyList, 3+1, sheet);
//                    } else if(commonPropertyList.get(0).getType().code()== PropertyType.FENCE.code()) {
//                        // Create sheet
//                        Sheet sheet = workbook.createSheet("Hàng rào"); // Create sheet with sheet name
//
//                        //        // Write header
//                        WriteExcelProperty.writeHeaderFence(sheet, 0);
//                        WriteExcelProperty.writeExcelFence(commonPropertyList, 3+2, sheet);
//                    } else if(commonPropertyList.get(0).getType().code()== PropertyType.ELECTRICAL_CABINETS.code()) {
//                        // Create sheet
//                        Sheet sheet = workbook.createSheet("Tủ điện"); // Create sheet with sheet name
//
//                        //        // Write header
//                        WriteExcelProperty.writeHeaderElectrical(sheet, 0);
//                        WriteExcelProperty.writeExcelElectrical(commonPropertyList, 3+1, sheet);
//                    } else if(commonPropertyList.get(0).getType().code()== PropertyType.AUXILIARY_EQUIPMENT.code()) {
//                        // Create sheet
//                        Sheet sheet = workbook.createSheet("Thiết bị phụ trợ"); // Create sheet with sheet name
//
//                        //        // Write header
//                        WriteExcelProperty.writeHeaderAuxiliaryEquipment(sheet, 0);
//                        WriteExcelProperty.writeExcelAuxiliaryEquipment(commonPropertyList, 3+1, sheet);
//                    }
//                }
//
////                while ((responseDTO != null && !responseDTO.getData().isEmpty()) || page == 0) {
////
////                    responseDTO = dbmViolationProcessService.findViolationsChecked(filter.getDistrictId(), filter.getWardId(), filter.getFromDate(), filter.getToDate(), filter.getPlate(),
////                            filter.getViolationType(),filter.getVehicleType(), filter.getProcessStatusList(), filter.getFilterObjectType(), filter.getFilterObjectIds(),
////                            page, 100, "startTime");
////                    page++;
////                    if (responseDTO != null && !responseDTO.getData().isEmpty()) {
////                        rowIndex = WriteExcelProperty.writeExcelBridge(null, excelFilePath, rowIndex, sheet);
////                    }
////                }
//                java.io.File path = new File(excelFilePath);
//                try {
//                    FileOutputStream os = new FileOutputStream(path);
//                    workbook.write(os);
//                } catch (Exception e) {
//                    workbook.close();
//                }
////                document.close();
////                LOGGER.info("send upload");
////                String link = uploadFile(path);
////                LOGGER.info("upload success");
////                System.out.println(link);
////                Map<String, Object> param = putBodyParam(uuid, link,"dsf");
////                handleSendNotifyToDevice(param);
////                LOGGER.info("export done");
//
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
////        EventFile eventFile = new EventFile(UUID.randomUUID().toString(),datePasser.parse(), violationHistory.getParentId(), "pdf", "Thông tin sự việc", fileName, sizeFile.intValue());
//        return new EventFile();
        return null;
    }


    private void saveSaveFile(EventFile eventFile) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/create/file";
        //Payload request dbm.root.url//v1.0/dbm/management/mons/recognitions
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> bodyParam = new HashMap<>();
//        bodyParam.put("id", eventFile.getId());
        bodyParam.put("eventId", eventFile.getEventId());
        bodyParam.put("fileName", eventFile.getFileName());
        bodyParam.put("fileUrl", eventFile.getFileUrl());
//        bodyParam.put("startTime", dateFormat.format(eventFile.getStartTime()));
        bodyParam.put("size", eventFile.getSize());
        bodyParam.put("createBy", eventFile.getCreateBy());
        bodyParam.put("fileType", eventFile.getFileType());
        bodyParam.put("createDate", dateFormat.format(eventFile.getCreateDate()));
        bodyParam.put("uploadTime", dateFormat.format(eventFile.getUploadTime()));
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        //Call rest api with url, method, payload, response entity
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
    }
    private String uploadFile(java.io.File path) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(path));
        body.add("keepFileName", true);
        body.add("localUpload ",true);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseDto> response = restTemplate.postForEntity(RabbitMQProperties.UPLOAD_URL, requestEntity, ResponseDto.class);
        if (response != null && response.getBody() != null && response.getBody().getData() != null) {
            path.delete();
            return response.getBody().getData().getFileDownloadUri();
        }
        return null;
    }

    private Map<String, Object> putBodyParam(String userId, String url, String eventId) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 5);
        bodyParam.put("content", URLDecoder.decode(url, "UTF-8") );
        bodyParam.put("url",URLDecoder.decode(url, "UTF-8") );
        bodyParam.put("title", "Export file");
        bodyParam.put("objectType", "export-file-event");
        bodyParam.put("objectUuid",eventId);
        bodyParam.put("userId", userId);
        return bodyParam;
    }

    private Map<String,User> getAllUser() {
        //Set body param
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath("/v1.0/user/all/internal");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("[<--] Id return {}",result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    Object data = resultResponse.getData().getData();
                    Map<String, User> mapUser = mapper.convertValue(data, new TypeReference<Map<String,User>>() {
                    });
                    //OK
//                    JsonNode jsonNode = mapper.readTree(result);
//                    Map<String, User> mapUser = mapper.readerFor(new TypeReference<List<String>>() {
//                    }).readValue(jsonNode.get("data").get("data"));
                    return mapUser;
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    private Map<String, Unit> getAllGroup() {
        //Set body param
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath("/v1.0/group/all/internal");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("[<--] Id return {}",result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    Object data = resultResponse.getData().getData();
                    Map<String, Unit> mapUser = mapper.convertValue(data, new TypeReference<Map<String,Unit>>() {
                    });
                    //OK
//                    JsonNode jsonNode = mapper.readTree(result);
//                    Map<String, User> mapUser = mapper.readerFor(new TypeReference<List<String>>() {
//                    }).readValue(jsonNode.get("data").get("data"));
                    return mapUser;
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    private void handleSendNotifyToDevice(Map<String, Object> bodyParam) {
        try {
            RequestMessage rbacRpcRequest = new RequestMessage();
            rbacRpcRequest.setRequestMethod("POST");
            rbacRpcRequest.setRequestPath("/v1.0/notify/send-notify");
            rbacRpcRequest.setBodyParam(bodyParam);
            rbacRpcRequest.setUrlParam(null);
            rbacRpcRequest.setHeaderParam(null);
            rbacRpcRequest.setVersion(ResourcePath.VERSION);
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.NOTIFY_RPC_EXCHANGE,
                    RabbitMQProperties.NOTIFY_RPC_QUEUE, RabbitMQProperties.NOTIFY_RPC_KEY, rbacRpcRequest.toJsonString());
            LOGGER.info("send notify status camera - result: " + result);
            if (result != null) {
                ObjectMapper mapper = new ObjectMapper();
                Response resultResponse = null;
                try {
                    resultResponse = mapper.readValue(result, Response.class);
                    if (resultResponse.getStatus() != HttpStatus.OK.value()) {
                        LOGGER.info("Error send notify to from heartbeat service");
                    }
                } catch (Exception ex) {
                    LOGGER.info("Error parse json in handleSendNotifyToDevice from heartbeat service: " + ex.toString());
                }
            }
        } catch (Exception ex) {
            LOGGER.info("Error parse json in handleSendNotifyToDevice from heartbeat service: " + ex.toString());
        }
    }
}
