package com.elcom.its.management.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.management.dto.CommonProperty;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.enums.PropertyType;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.management.service.PropertyService;
import com.elcom.its.message.RequestMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ImportExcel {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportExcel.class);

    @Autowired
    private ImportMutil importMutil;

    @Autowired
    private PropertyService eventService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Async("import")
    public CompletableFuture<String> importExcel(MultipartFile file, String uuid, Integer type) throws IOException {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            List<CompletableFuture<List<CommonProperty>>> listResult = new ArrayList<>();
            if(type==PropertyType.BRIDGE.code()) {
                Sheet sheetBridge = workbook.getSheetAt(0);
                CompletableFuture<List<CommonProperty>> bridge = importMutil.listBridge(sheetBridge);
                listResult.add(bridge);
            } else if(type==PropertyType.DRAIN.code()) {
                Sheet sheetDrain =  workbook.getSheetAt(0);
                CompletableFuture<List<CommonProperty>> drain = importMutil.listDrain(sheetDrain);
                listResult.add(drain);
            } else if(type==PropertyType.TRENCH.code()) {
                Sheet sheetTrench =  workbook.getSheetAt(0);
                CompletableFuture<List<CommonProperty>> trench = importMutil.listTrench(sheetTrench);
                listResult.add(trench);
            } else if(type==PropertyType.TRAFFIC_SIGNS.code()) {
                Sheet sheetTrafficSigns =  workbook.getSheetAt(0);
                CompletableFuture<List<CommonProperty>> trafficSigns = importMutil.listTrafficSigns(sheetTrafficSigns);
                listResult.add(trafficSigns);
            } else if(type==PropertyType.FENCE.code()) {
                Sheet sheetFence =  workbook.getSheetAt(0);
                CompletableFuture<List<CommonProperty>> fence = importMutil.listFence(sheetFence);
                listResult.add(fence);
            } else if(type==PropertyType.ELECTRICAL_CABINETS.code()) {
                Sheet sheetElectricalCabinets =  workbook.getSheetAt(0);
                CompletableFuture<List<CommonProperty>> electricalCabinets = importMutil.listElectricalCabinets(sheetElectricalCabinets);
                listResult.add(electricalCabinets);
            } else if(type ==PropertyType.AUXILIARY_EQUIPMENT.code()) {
                Sheet sheetAuxiliary =  workbook.getSheetAt(0);
                CompletableFuture<List<CommonProperty>> auxiliary = importMutil.listAuxiliaryEquipment(sheetAuxiliary);
                listResult.add(auxiliary);
            }

            CompletableFuture.allOf(listResult.toArray(new CompletableFuture<?>[0]))
                    .thenApply(v -> listResult.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.toList())
                    );

            List<CommonProperty> data =  new ArrayList<>();
            for (CompletableFuture<List<CommonProperty>> result : listResult) {
                data.addAll(result.get());
            }
            eventService.saveListPropertyImport(data,uuid);
            Map<String,Object> body = putBodyParam(uuid,data.size());
            handleSendNotifyToDevice(body);
            workbook.close();
            return CompletableFuture.completedFuture("oke");
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture("false");
        }
    }
    private Map<String, Object> putBodyParam(String userId,  Integer count) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 7);
        bodyParam.put("content", URLDecoder.decode("Lưu thành công " + count + " bản ghi", "UTF-8") );
        bodyParam.put("url",URLDecoder.decode("Lưu thành công " + count + " bản ghi", "UTF-8") );
        bodyParam.put("title", "import file");
        bodyParam.put("objectType", "import-file-fms");
        bodyParam.put("objectUuid","import");
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
                    LOGGER.info("Error parse json in handleSendNotifyToDevice from heartbeat service: " + ex.toString());
                }
            }
        } catch (Exception ex) {
            LOGGER.info("Error parse json in handleSendNotifyToDevice from heartbeat service: " + ex.toString());
        }
    }




}
