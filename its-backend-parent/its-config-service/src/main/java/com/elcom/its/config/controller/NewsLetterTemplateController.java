package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.NewsLetterTemplate;
import com.elcom.its.config.model.Route;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.NewsLetterTemplateService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class NewsLetterTemplateController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);
    @Autowired
    private NewsLetterTemplateService newsLetterTemplateService;

    public ResponseMessage getAllNewsLetterTemplate(Map<String, String> headerParam, String requestPath,
                                                    String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    response = new ResponseMessage(new MessageContent(newsLetterTemplateService.getAllNewsLetterTemplate()));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String search = params.get("search");
                    if (page != null && size != null) {
                        Response newsLetterPaginationDTO = newsLetterTemplateService.getAllNewsLetterTemplate(page, size, search);
                        if (newsLetterPaginationDTO != null && newsLetterPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), newsLetterPaginationDTO.getData(), newsLetterPaginationDTO.getTotal()));
                        }
                    } else {
                        return new ResponseMessage(new MessageContent(newsLetterTemplateService.getAllNewsLetterTemplate()));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền", null));
            }

        }
        return response;
    }

    public ResponseMessage getNewsLetterById(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response newsLetterTemplateDetailDTO = newsLetterTemplateService.getNewsLetterTemplateById(paramPath);
        if (newsLetterTemplateDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), newsLetterTemplateDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(newsLetterTemplateDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage createNewsLetterTemplate(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) {
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
                    String name = (String) bodyParam.get("name");
                    String typeBoard = (String) bodyParam.get("typeBoard");
                    String content = (String) bodyParam.get("content");

                    NewsLetterTemplate newsLetterTemplate = new NewsLetterTemplate();

                    newsLetterTemplate.setName(name);
                    newsLetterTemplate.setTypeBoard(typeBoard);
                    newsLetterTemplate.setContent(content);

                    response = new ResponseMessage(new MessageContent(newsLetterTemplateService.saveNewsLetterTemplate(newsLetterTemplate)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền", null));
            }
        }
        return response;
    }

    public ResponseMessage updateNewsLetterTemplate(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
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
                    String name = (String) bodyParam.get("name");
                    String typeBoard = (String) bodyParam.get("typeBoard");
                    String content = (String) bodyParam.get("content");

                    NewsLetterTemplate newsLetterTemplate = new NewsLetterTemplate();

                    newsLetterTemplate.setId(pathParam);
                    newsLetterTemplate.setName(name);
                    newsLetterTemplate.setTypeBoard(typeBoard);
                    newsLetterTemplate.setContent(content);

                    Response newLetter = newsLetterTemplateService.getNewsLetterTemplateById(pathParam);
                    if (newLetter != null) {
                        response = new ResponseMessage(new MessageContent(newsLetterTemplateService.updateNewsLetterTemplate(newsLetterTemplate)));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy route tương ứng với id: " + pathParam, null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteNewsLetterTemplate(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response NewsLetterTemplate = newsLetterTemplateService.getNewsLetterTemplateById(pathParam);
                if (NewsLetterTemplate == null) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy id tương ứng: " + pathParam, null);
                } else {
                    response = new ResponseMessage(new MessageContent(newsLetterTemplateService.deleteNewsLetterTemplate(pathParam)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiNewsLetter(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;

        List<String> uuid = (List<String>) bodyParam.get("ids");
        ListUuid listUuid = new ListUuid();
        listUuid.setIds(uuid);

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(newsLetterTemplateService.multiDelete(listUuid)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền", null));
            }
        }
        return response;
    }

}
