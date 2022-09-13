package com.elcom.its.vds.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vds.model.Vds;
import com.elcom.its.vds.model.dto.ABACResponseDTO;
import com.elcom.its.vds.model.dto.AuthorizationResponseDTO;
import com.elcom.its.vds.service.VdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class VdsDashboardController extends BaseController{

    @Autowired
    private VdsService vdsService;

    public ResponseMessage findVds(String requestPath, Map<String, String> headerParam,
                                   Map<String, Object> bodyParam, String requestMethod, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                String siteId = params.get("siteId");
                String cameraId = params.get("cameraId");
                String processId = params.get("processId");
                String search = params.get("search");

                Page<Vds> pagedResult = vdsService.findVds(siteId, cameraId, processId, search, page, size);
                if (pagedResult != null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), pagedResult.getContent(), pagedResult.getTotalElements()));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null, 0L));
                }
            }
        }
        return response;
    }
}
