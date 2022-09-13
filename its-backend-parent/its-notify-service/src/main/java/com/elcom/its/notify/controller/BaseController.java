package com.elcom.its.notify.controller;

import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.notify.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.notify.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.notify.model.dto.AuthorizationResponseDTO;
import com.elcom.its.notify.model.dto.RBACResponseDTO;
import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.MessageContent;
import com.elcom.its.notify.model.dto.ABACResponseDTO;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Check token qua id service => Trả về detail user
     *
     * @param headerMap header chứa jwt token
     * @return detail user
     */
    public AuthorizationResponseDTO authenToken(Map<String, String> headerMap) {
        //Authen -> call rpc authen headerMap
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_AUTHEN_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerMap);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("authenToken - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //mapper.setDateFormat(df);
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                return null;
            }

            if (response != null && response.getStatus() == HttpStatus.OK.value()) {
                try {
                    //Process
                    MessageContent content = response.getData();
                    Object data = content.getData();
                    if (data != null) {
                        AuthorizationResponseDTO dto = null;
                        if (data.getClass() == LinkedHashMap.class) {
                            dto = new AuthorizationResponseDTO((Map<String, Object>) data);
                        } else if (data.getClass() == AuthorizationResponseDTO.class) {
                            dto = (AuthorizationResponseDTO) data;
                        }
                        if (dto != null && !StringUtil.isNullOrEmpty(dto.getUuid())) {
                            return dto;
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.info("Lỗi giải mã AuthorizationResponseDTO khi gọi user service verify: " + ex.toString());
                    return null;
                }
            } else {
                //Forbidden
                return null;
            }
        } else {
            //Forbidden
            return null;
        }
        return null;
    }

    /**
     * Check quyền của user ứng với request method và api đang gọi
     *
     * @param requestMethod : Request method POST, GET, PUT, DELETE
     * @param userUuid user uuid
     * @param apiPath api link
     * @return boolean
     */
    public boolean authorizeRBAC(String requestMethod, String userUuid, String apiPath) {
        //Set body param
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("requestMethod", requestMethod);
        bodyParam.put("uuid", userUuid);
        bodyParam.put("apiPath", apiPath);

        //Authorize user action with api -> call rbac service
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("POST");
        rbacRpcRequest.setRequestPath(RabbitMQProperties.RBAC_RPC_AUTHOR_URL);
        rbacRpcRequest.setBodyParam(bodyParam);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.RBAC_RPC_EXCHANGE,
                RabbitMQProperties.RBAC_RPC_QUEUE, RabbitMQProperties.RBAC_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("authorizeRBAC - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    RBACResponseDTO rbacResponseDTO = mapper.treeToValue(jsonNode.get("data").get("data"), RBACResponseDTO.class);
                    return rbacResponseDTO != null && rbacResponseDTO.getPermission();
                }
            } catch (Exception ex) {
                LOGGER.info("Lỗi parse json khi gọi kiểm tra quyền từ rbac service: " + ex.toString());
                return false;
            }
        }
        return false;
    }

    /**
     * Check quyền của user ứng với request method và api đang gọi
     *
     * @param requestMethod : Request method POST, GET, PUT, DELETE
     * @param userUuid user uuid
     * @param apiPath api link
     * @return RBACResponseDTO chua quyen va nhung data duoc truy cap
     */
    public RBACResponseDTO authorizeRBACResponse(String requestMethod, String userUuid, String apiPath) {
        //Set body param
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("requestMethod", requestMethod);
        bodyParam.put("uuid", userUuid);
        bodyParam.put("apiPath", apiPath);

        //Authorize user action with api -> call rbac service
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("POST");
        rbacRpcRequest.setRequestPath(RabbitMQProperties.RBAC_RPC_AUTHOR_URL);
        rbacRpcRequest.setBodyParam(bodyParam);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.RBAC_RPC_EXCHANGE,
                RabbitMQProperties.RBAC_RPC_QUEUE, RabbitMQProperties.RBAC_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("authorizeRBAC - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    RBACResponseDTO rbacResponseDTO = mapper.treeToValue(jsonNode.get("data").get("data"), RBACResponseDTO.class);
                    return rbacResponseDTO;
                }
            } catch (Exception ex) {
                LOGGER.info("Lỗi parse json khi gọi kiểm tra quyền từ rbac service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    public List<String> getCameraIdList(List<String> groupCameraIdList, List<String> siteIdList) {
        //Set body param
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("cameraGroupIds", groupCameraIdList);
        bodyParam.put("siteIdList", siteIdList);

        //Get list site id -> call systemconfig service
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("POST");
        rbacRpcRequest.setRequestPath(RabbitMQProperties.SYSTEMCONFIG_RPC_CAMERA_LIST);
        rbacRpcRequest.setBodyParam(bodyParam);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.SYSTEMCONFIG_RPC_EXCHANGE,
                RabbitMQProperties.SYSTEMCONFIG_RPC_QUEUE, RabbitMQProperties.SYSTEMCONFIG_RPC_KEY,
                rbacRpcRequest.toJsonString());
        LOGGER.info("getCameraIdList - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<String> dtoList = mapper.treeToValue(jsonNode.get("data").get("data"), List.class);
                    return dtoList;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.info("Lỗi parse json khi gọi từ systemconfig service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    public ABACResponseDTO authorizeABAC(Map<String, Object> bodyParam, String requestMethod,
            String userUuid, String apiPath) {
        Map<String, Object> bodyParamSend = new HashMap<>();
        if (bodyParam != null && !bodyParam.isEmpty()) {
            bodyParamSend.putAll(bodyParam);
        }
        bodyParamSend.put("uuid", userUuid);
        if (apiPath != null && !apiPath.startsWith(ResourcePath.VERSION)) {
            bodyParamSend.put("api", ResourcePath.VERSION + apiPath);
        } else {
            bodyParamSend.put("api", apiPath);
        }
        bodyParamSend.put("method", requestMethod);
        RequestMessage abacRpcRequest = new RequestMessage();
        abacRpcRequest.setRequestMethod("POST");
        abacRpcRequest.setRequestPath(RabbitMQProperties.ABAC_RPC_AUTHOR_URL);
        abacRpcRequest.setBodyParam(bodyParamSend);
        abacRpcRequest.setUrlParam(null);
        abacRpcRequest.setHeaderParam(null);

        LOGGER.info("REQUEST: {}", abacRpcRequest.toJsonString());
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.ABAC_RPC_EXCHANGE,
                RabbitMQProperties.ABAC_RPC_QUEUE, RabbitMQProperties.ABAC_RPC_KEY,
                abacRpcRequest.toJsonString());
        LOGGER.info("RESULT: {}", result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    JsonNode jsonNode = mapper.readTree(result);
                    ABACResponseDTO resultCheckDto = mapper.treeToValue(jsonNode.get("data").get("data"), ABACResponseDTO.class);
                    return resultCheckDto;
                }
                return null;
            } catch (JsonProcessingException | IllegalArgumentException ex) {
                return null;
            }
        } else {
            return null;
        }
    }
}
