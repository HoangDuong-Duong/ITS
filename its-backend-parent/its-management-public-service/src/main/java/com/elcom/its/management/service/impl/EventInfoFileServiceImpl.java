package com.elcom.its.management.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.export.WriteExcelEvent;
import com.elcom.its.management.export.WriteExcelProperty;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.management.service.EventInfoFileService;
import com.elcom.its.management.service.EventInfoService;
import com.elcom.its.management.service.EventService;
import com.elcom.its.management.service.ReportJobService;
import com.elcom.its.message.RequestMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EventInfoFileServiceImpl implements EventInfoFileService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EventFileServiceImpl.class);
    @Autowired
    private RabbitMQClient rabbitMQClient;
    @Autowired
    private EventInfoService eventInfoService;
    @Autowired
    private EventService eventService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ReportJobService reportJobService;

    private static CellStyle createStyleRow(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }

    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex, true);

        }
    }

//    @Override
//    public EventInfoDTO createAccidentReportFile(String startDate, String endDate, String textHeader, String uuid) {
//        try {
//            AccidentReportResponseDTO propertyResponseDTO = eventInfoService.getDataReport(startDate, endDate);
//            if (propertyResponseDTO != null && propertyResponseDTO.getData() != null && !propertyResponseDTO.getData().isEmpty()) {
//                List<AccidentReport> commonPropertyList = propertyResponseDTO.getData();
//                DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
//                DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                DateFormat dfMonth = new SimpleDateFormat("MM");
//                final String excelFilePath = "BÁO CÁO ĐỊNH KỲ TNGT THÁNG _" + dfMonth.format(dfFormat.parse(startDate)) + ".xls";
//                String urlFile = "" + excelFilePath;
//                Path pathh = Paths.get(urlFile);
//                Workbook workbook = WriteExcelProperty.getWorkbook(excelFilePath);
//                if (commonPropertyList != null && !commonPropertyList.isEmpty()) {
//                    // Create sheet
//                    Date start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate);
//                    Date end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDate);
//                    String date = "(Từ ngày " + start.getDate() + " tháng " + (start.getMonth() + 1) + " năm " + (start.getYear() + 1900) + " ÷ " + end.getDate() + " tháng " + (end.getMonth() + 1) + " năm " + (end.getYear() + 1900) + ")";
//                    Sheet sheet = workbook.createSheet("BCTC");
//                    WriteExcelProperty.writeHeader(sheet, 0, textHeader, date, workbook);
//                    WriteExcelProperty.writeExcelBridge(commonPropertyList, 20, sheet);
//                    WriteExcelProperty.writeFooter(sheet, 21, workbook);
//                }
//                Long sizeFile = new Long(0);
//                java.io.File path = new File(excelFilePath);
//                try {
//                    FileOutputStream os = new FileOutputStream(path);
//                    workbook.write(os);
//
//                    String link = uploadFile(path);
////                    sizeFile = Files.size(pathh);
//                    LOGGER.info("send upload");
//
//                    LOGGER.info("upload success");
//
//                    EventInfoFile eventFile = new EventInfoFile();
//                    eventFile.setSize(sizeFile.intValue());
//                    eventFile.setFileType("application/pdf");
//                    eventFile.setId(UUID.randomUUID().toString());
//                    eventFile.setFileName(excelFilePath);
//                    eventFile.setUploadTime(new Date());
//                    eventFile.setCreateBy(uuid);
//                    eventFile.setCreateDate(new Date());
//                    eventFile.setStartTime(new Date());
//                    eventFile.setFileUrl(link);
//
//
//                    saveSaveFile(eventFile);
//                    Map<String, Object> param = putBodyParam(uuid, link);
//                    handleSendNotifyToDevice(param);
//                    LOGGER.info("export done");
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    workbook.close();
//                }
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return new EventInfoDTO();
//    }
@Override
public EventInfoDTO createAccidentReportFile(String startDate, String endDate, String textHeader, String uuid) {
    try {
        EventInfoResponseDTO propertyResponseDTO = eventInfoService.getDataReportEventInfo(startDate, endDate);
        CategoryResponseDTO categoryResponseDTO = eventService.getPrinted("PRINTED");
        if (propertyResponseDTO != null && propertyResponseDTO.getData() != null && !propertyResponseDTO.getData().isEmpty()) {
            List<EventInfo> commonPropertyList = propertyResponseDTO.getData();
            DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            DateFormat dfFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            DateFormat dfMonth = new SimpleDateFormat("MM");
            Date now= new Date();
            final String excelFilePath = "BÁO CÁO ĐỊNH KỲ TNGT THÁNG " + dfMonth.format(dfFormat.parse(startDate))+"_"+df.format(now) + ".xls";
            String urlFile = "" + excelFilePath;
            Path pathh = Paths.get(urlFile);
            Workbook workbook = WriteExcelProperty.getWorkbook(excelFilePath);
            if (commonPropertyList != null && !commonPropertyList.isEmpty()) {
                // Create sheet
                Date start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate);
                Date end = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endDate);
                String fromDate ="";
                if(start.getDate()<10){
                    fromDate="0"+start.getDate();
                }else {
                    fromDate= start.getDate()+"";
                }
                String toDate ="";
                if(end.getDate()<10){
                    toDate="0"+end.getDate();
                }else {
                    toDate= end.getDate()+"";
                }
                String date = "(Từ ngày " + fromDate + " tháng " + (start.getMonth() + 1) + " năm " + (start.getYear() + 1900) + " đến ngày " + toDate + " tháng " + (end.getMonth() + 1) + " năm " + (end.getYear() + 1900) + ")";
                Font font= workbook.createFont();
                font.setFontName("Times New Roman");
                font.setFontHeightInPoints((short) 13);
                Sheet sheet = workbook.createSheet("BCTC");
                WriteExcelProperty.writeHeaderBCDKTNGT(sheet, 0, textHeader, date, workbook);
                int count=0;
                String check= textHeader;
                while (check.indexOf("\n")>=0){
                    count++;
                    check=check.substring(check.indexOf("\n")+1);
                }
                WriteExcelProperty.writeExcelEventInfo(commonPropertyList, 12+count, sheet,start);
                WriteExcelProperty.writeExcelFoot(categoryResponseDTO.getData(),12+commonPropertyList.size()+1+count,sheet);
//                WriteExcelProperty.writeFooter(sheet, 21, workbook);
            }
            Long sizeFile = new Long(0);
            java.io.File path = new File(excelFilePath);
            try {
                FileOutputStream os = new FileOutputStream(path);
                workbook.write(os);
                workbook.close();
                String link = uploadFile(path);
//                    sizeFile = Files.size(pathh);
                LOGGER.info("send upload");
                System.out.println(link);

                LOGGER.info("upload success");

                EventInfoFile eventFile = new EventInfoFile();
                eventFile.setSize(sizeFile.intValue());
                eventFile.setFileType("application/pdf");
                eventFile.setId(UUID.randomUUID().toString());
                eventFile.setFileName(excelFilePath);
                eventFile.setUploadTime(new Date());
                eventFile.setCreateBy(uuid);
                eventFile.setCreateDate(new Date());
                eventFile.setStartTime(new Date());
                eventFile.setFileUrl(link);


                saveSaveFile(eventFile);
                Map<String, Object> param = putBodyParam(uuid, link);
                handleSendNotifyToDevice(param);
                LOGGER.info("export done");

            } catch (Exception e) {
                e.printStackTrace();
                workbook.close();
            }
        }
    } catch (Exception ex) {
        Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new EventInfoDTO();
}

    @Override
    public EventInfoDTO createEventReportFile(Integer size, Integer page, String startDate, String endDate, String textHeader, String uuid) {
        try {
            EventInfoPage propertyResponseDTO = eventInfoService.getAllEventInfo(size, page, startDate, endDate);
            if (propertyResponseDTO != null && propertyResponseDTO.getData() != null && !propertyResponseDTO.getData().isEmpty()) {
                List<EventInfo> commonPropertyList = propertyResponseDTO.getData();
                DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
                final String excelFilePath = "Sự kiện_" + df.format(new Date()) + ".xls";
                String urlFile = "" + excelFilePath;
                Path pathh = Paths.get(urlFile);
                Workbook workbook = WriteExcelProperty.getWorkbook(excelFilePath);
                if (commonPropertyList != null && !commonPropertyList.isEmpty()) {
                    // Create sheet
                    Date start = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startDate);
                    Integer rowIndex = 14 + commonPropertyList.size();
                    String date = start.getMonth() + 1 + "/" + (start.getYear() + 1900);
                    Sheet sheet = workbook.createSheet("BCTC");
                    WriteExcelEvent.writeHeader(sheet, 0, textHeader, date, workbook);
                    WriteExcelEvent.writeExcelBridge(commonPropertyList, 14, sheet);
                    WriteExcelEvent.writeFooter(sheet, rowIndex, workbook);
                }
                Long sizeFile = new Long(0);
                java.io.File path = new File(excelFilePath);
                try {
                    FileOutputStream os = new FileOutputStream(path);
                    workbook.write(os);

                    String link = uploadFile(path);
//                    sizeFile = Files.size(pathh);
                    LOGGER.info("send upload");

                    LOGGER.info("upload success");

                    EventInfoFile eventFile = new EventInfoFile();
                    eventFile.setSize(sizeFile.intValue());
                    eventFile.setFileType("application/pdf");
                    eventFile.setId(UUID.randomUUID().toString());
                    eventFile.setFileName(excelFilePath);
                    eventFile.setUploadTime(new Date());
                    eventFile.setCreateBy(uuid);
                    eventFile.setCreateDate(new Date());
                    eventFile.setStartTime(new Date());
                    eventFile.setFileUrl(link);


                    saveSaveFile(eventFile);
                    Map<String, Object> param = putBodyParamInfo(uuid, link);
                    handleSendNotifyToDevice(param);
                    LOGGER.info("export done");
                } catch (Exception e) {
                    workbook.close();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new EventInfoDTO();
    }

    @Override
    public String createHistoryDisplayFile(String startDate, String endDate, String vmsId, String uuid) {
        String url = "";
        try {
            List<HistoryDisplayScript> commonPropertyList = eventInfoService.getHistory(startDate, endDate, vmsId);
                DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
                final String excelFilePath = "history_" + df.format(new Date()) + ".xlsx";
                String urlFile = "" + excelFilePath;
                Path pathh = Paths.get(urlFile);
                Workbook workbook = WriteExcelProperty.getWorkbookXSS(excelFilePath);
                Sheet sheet = workbook.createSheet("BCTC");
                WriteExcelEvent.writeHeaderHistory(commonPropertyList,sheet, 0, workbook);
                WriteExcelEvent.writeExcelHistory(commonPropertyList, 1, sheet);
                Long sizeFile = new Long(0);
                java.io.File path = new File(excelFilePath);
                try {
                    FileOutputStream os = new FileOutputStream(path);
                    workbook.write(os);

                    String link = uploadFile(path);
                    LOGGER.info("send upload");

                    LOGGER.info("upload success");

                    EventInfoFile eventFile = new EventInfoFile();
                    eventFile.setSize(sizeFile.intValue());
                    eventFile.setFileType("application/pdf");
                    eventFile.setId(UUID.randomUUID().toString());
                    eventFile.setFileName(excelFilePath);
                    eventFile.setUploadTime(new Date());
                    eventFile.setCreateBy(uuid);
                    eventFile.setCreateDate(new Date());
                    eventFile.setStartTime(new Date());
                    eventFile.setFileUrl(link);

                    url = link;

                    saveSaveFile(eventFile);
                    Map<String, Object> param = putBodyParamInfo(uuid, link);
                    handleSendNotifyToDevice(param);
                    LOGGER.info("export done");
                } catch (Exception e) {
                    workbook.close();
                }
        } catch (Exception ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }

    @Override
    public String createActionStatusFile(String startDate, String endDate, String uuid) {
        String url = "";
        try {
            Pageable pageable = PageRequest.of(0, 1000, Sort.by("startTime").descending());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<ReportOnlineStatusDTO> commonPropertyList = reportJobService.reportJobOnlineStatus(dateFormat.parse(startDate), dateFormat.parse(endDate),pageable).getContent();
            DateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            final String excelFilePath = "Bao_Cao_Tinh_Trang_Hoat_Dong_Tuyen_" + df.format(new Date()) + ".xlsx";
            String urlFile = "" + excelFilePath;
            Path pathh = Paths.get(urlFile);
            Workbook workbook = WriteExcelProperty.getWorkbookXSS(excelFilePath);
            Sheet sheet = workbook.createSheet("BCTC");
            WriteExcelEvent.writeHeaderActionStatus(sheet, 0);
            WriteExcelEvent.writeExcelActionStatus(commonPropertyList, 1, sheet);
            Long sizeFile = new Long(0);
            java.io.File path = new File(excelFilePath);
            try {
                FileOutputStream os = new FileOutputStream(path);
                workbook.write(os);

                String link = uploadFile(path);
                LOGGER.info("send upload");

                LOGGER.info("upload success");

                EventInfoFile eventFile = new EventInfoFile();
                eventFile.setSize(sizeFile.intValue());
                eventFile.setFileType("application/pdf");
                eventFile.setId(UUID.randomUUID().toString());
                eventFile.setFileName(excelFilePath);
                eventFile.setUploadTime(new Date());
                eventFile.setCreateBy(uuid);
                eventFile.setCreateDate(new Date());
                eventFile.setStartTime(new Date());
                eventFile.setFileUrl(link);

                url = link;

                saveSaveFile(eventFile);
                Map<String, Object> param = putBodyParamInfo(uuid, link);
                handleSendNotifyToDevice(param);
                LOGGER.info("export done");
            } catch (Exception e) {
                workbook.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }

    private String uploadFile(java.io.File path) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(path));
        body.add("keepFileName", true);
        body.add("localUpload ", true);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseDto> response = restTemplate.postForEntity(RabbitMQProperties.UPLOAD_URL, requestEntity, ResponseDto.class);
        if (response != null && response.getBody() != null && response.getBody().getData() != null) {
            path.delete();
            return response.getBody().getData().getFileDownloadUri();
        }
        return null;
    }

    private void saveSaveFile(EventInfoFile eventFile) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/create/file";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("fileName", eventFile.getFileName());
        bodyParam.put("fileUrl", eventFile.getFileUrl());
        bodyParam.put("size", eventFile.getSize());
        bodyParam.put("createBy", eventFile.getCreateBy());
        bodyParam.put("fileType", eventFile.getFileType());
        bodyParam.put("createDate", dateFormat.format(eventFile.getCreateDate()));
        bodyParam.put("uploadTime", dateFormat.format(eventFile.getUploadTime()));
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
    }


    private Map<String, Object> putBodyParam(String userId, String url) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 5);
        bodyParam.put("content", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("url", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("title", "Export file");
        bodyParam.put("objectType", "export-file-accident-report");
        bodyParam.put("objectUuid", "");
        bodyParam.put("userId", userId);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamInfo(String userId, String url) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 5);
        bodyParam.put("content", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("url", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("title", "Export file");
        bodyParam.put("objectType", "export-file-event-info-report");
        bodyParam.put("objectUuid", "");
        bodyParam.put("userId", userId);
        return bodyParam;
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
                    LOGGER.info("Error parse json in handleSendNotifyToDevice from heartbeat service: " + ex);
                }
            }
        } catch (Exception ex) {
            LOGGER.info("Error parse json in handleSendNotifyToDevice from heartbeat service: " + ex);
        }
    }
}