package com.elcom.its.vmsboard.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vmsboard.constant.Constant;
import com.elcom.its.vmsboard.dto.ABACResponseDTO;
import com.elcom.its.vmsboard.dto.AuthorizationResponseDTO;
import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.ContentLayout;
import com.elcom.its.vmsboard.model.Layout;
import com.elcom.its.vmsboard.model.LayoutComponentAttribute;
import com.elcom.its.vmsboard.model.NewsLetterTemplate;
import com.elcom.its.vmsboard.service.VmsLayoutService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class VmsLayoutController extends BaseController {
    @Autowired
    private VmsLayoutService layoutService;


    public ResponseMessage getAllLayout(Map<String, String> headerParam, String requestPath, String method, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Response layout = layoutService.getAllLayout(paramPath);
        if (layout.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), layout.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(layout.getData()));
        }
        return response;
    }

    public ResponseMessage createLayout(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "POST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Integer contentSite = Integer.parseInt(bodyParam.get("contentSite").toString());
                    Integer type = Integer.parseInt(bodyParam.get("type").toString());
                    Integer layoutId = Integer.parseInt(bodyParam.get("layoutId").toString());
                    Object properties = (Object) bodyParam.get("properties");

                    Layout layout = new Layout();
                    layout.setLayoutId(layoutId);
                    layout.setContentSite(contentSite);
                    layout.setType(type);
                    layout.setProperties(properties);

                    response = new ResponseMessage(new MessageContent(layoutService.createLayout(layout)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền tạo mới bản tin", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteLayout(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(layoutService.deleteLayout(pathParam)));

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa bản tin", null));
            }
        }
        return response;
    }

// Content layout


    public ResponseMessage getAllContentLayout(Map<String, String> headerParam, String requestPath,
                                               String method, String urlParam, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        Integer siteLayout = params.get("siteLayout") != null ? Integer.parseInt(params.get("siteLayout")) : null;
        Integer layoutId = params.get("layoutId") != null ? Integer.parseInt(params.get("layoutId")) : null;
        Response contentLayout = layoutService.getAllContent(siteLayout,layoutId);
        if (contentLayout.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), contentLayout.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(contentLayout.getData()));
        }
        return response;
    }

    public ResponseMessage createContentLayout(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "POST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String name = bodyParam.get("name").toString();
                    String content = bodyParam.get("content").toString();
                    Integer site = Integer.parseInt(bodyParam.get("site").toString());
                    Integer layoutId = Integer.parseInt(bodyParam.get("layoutId").toString());

                    ContentLayout layout = new ContentLayout();
                    layout.setLayoutId(layoutId);
                    layout.setSite(site);
                    layout.setName(name);
                    layout.setContent(content);

                    response = new ResponseMessage(new MessageContent(layoutService.createContent(layout)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền tạo mới bản tin", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteContentLayout(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(layoutService.deleteContent(pathParam)));

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa bản tin", null));
            }
        }
        return response;
    }

    // layout atrribute

    public ResponseMessage getAttributeInLayout(Map<String, String> headerParam, String requestPath, String method, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Response layout = layoutService.getAttributeInLayout(paramPath);
        if (layout.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), layout.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(layout.getData()));
        }
        return response;
    }

    public ResponseMessage createAttribute(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Object properties = bodyParam.get("listdata");
                    ObjectMapper mapper = new ObjectMapper();
                    List<LayoutComponentAttribute> listString = mapper.convertValue(properties, new TypeReference<List<LayoutComponentAttribute>>(){});
                    response = new ResponseMessage(new MessageContent(layoutService.updateAttributeInLayout(paramPath,listString)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền tạo mới bản tin", null));
            }
        }
        return response;
    }

}
