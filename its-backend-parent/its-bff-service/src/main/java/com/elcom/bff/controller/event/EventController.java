package com.elcom.bff.controller.event;


import com.elcom.bff.contants.ResourcePath;
import com.elcom.bff.controller.BaseController;
import com.elcom.bff.dto.AuthorizationResponseDTO;
import com.elcom.bff.dto.Response;
import com.elcom.bff.dto.Role;
import com.elcom.bff.dto.User;
import com.elcom.bff.model.ManagementUser;
import com.elcom.bff.service.EventService;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(ResourcePath.ManagementEvent)
public class EventController extends BaseController {

    @Autowired
    private EventService eventService;

    @GetMapping(value = "history/{id}")
    public ResponseEntity<Response> getHistoryId(@RequestHeader Map<String, String> headerParam, @RequestParam Map<String, String> reqParam, @PathVariable String id) throws ExecutionException, InterruptedException, JsonProcessingException {
        ResponseMessage response;
        response = authenToken(headerParam);
        AuthorizationResponseDTO dto = null;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        if (response.getData().getStatus() != HttpStatus.OK.value()) {
            return new ResponseEntity<>(new Response(response.getData().getStatus(), response.getData().getMessage(), null), HttpStatus.UNAUTHORIZED);
        } else {
            dto = (AuthorizationResponseDTO) response.getData().getData();
            String dtoUuid = mapper.writeValueAsString(dto);
            reqParam.put("dto", dtoUuid);
            String urlParam = StringUtil.generateMapString(reqParam);

            // Gọi lấy dữ liệu từ Id
            Response responseService;
            responseService = eventService.getHistory(urlParam,headerParam,id);

            return new ResponseEntity<>(responseService, HttpStatus.OK);
        }
    }
}
