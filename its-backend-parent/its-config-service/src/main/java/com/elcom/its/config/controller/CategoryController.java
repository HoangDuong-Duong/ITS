package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.ListId;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.Category;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.CategoryService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class CategoryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);
    @Autowired
    private CategoryService categoryService;

    public ResponseMessage getAllCategory(Map<String, String> headerParam, String requestPath,
            String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    response = new ResponseMessage(new MessageContent(categoryService.getAllCategory()));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String search = params.get("search");
                    String catType = params.get("catType") != null ? params.get("catType") : "";

                    if (StringUtil.isNullOrEmpty(catType)) {
                        if (page != null && size != null) {
                            Response categoryPaginationDTO = categoryService.getAllCategory(page, size, search);
                            if (categoryPaginationDTO != null && categoryPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), categoryPaginationDTO.getData(), categoryPaginationDTO.getTotal()));
                            }
                        } else {
                            return new ResponseMessage(new MessageContent(categoryService.getAllCategory()));
                        }
                    } else {
                        Response categoryPaginationDTO = categoryService.getAllCategoryByType(catType);
                        if (categoryPaginationDTO != null && categoryPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), categoryPaginationDTO.getData(), categoryPaginationDTO.getTotal()));
                        } else {
                            return new ResponseMessage(new MessageContent(null));
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách danh mục", null));
            }

        }
        return response;
    }

    public ResponseMessage getCategoryByType(Map<String, String> headerParam, String requestPath,
            String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;

        LOGGER.info("cate type");

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tham số truyền vào không đúng", null));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    String catType = params.get("catType");
                    if (catType != null) {
                        Response categoryPaginationDTO = categoryService.getAllCategoryByType(catType);
                        if (categoryPaginationDTO.getData() != null && categoryPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), categoryPaginationDTO.getData(), categoryPaginationDTO.getTotal()));
                        } else {
                            response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy danh mục tương ứng", null));
                        }
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tham số loại danh mục không được bỏ trống", null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền", null));
            }
        }
        return response;
    }

    public ResponseMessage getCategoryById(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response categoryDetailDTO = categoryService.getCategoryById(paramPath);
                if (categoryDetailDTO.getData() == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), categoryDetailDTO.getMessage(), null));
                } else {
                    response = new ResponseMessage(new MessageContent(categoryDetailDTO.getData()));
                }
            }else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết danh mục", null));
            }
        }
        return response;
    }

    public ResponseMessage createCategory(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) {
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
//                    String name = (String) bodyParam.get("name");
                    String code = (String) bodyParam.get("code");
                    String value = (String) bodyParam.get("value");
                    String catType = (String) bodyParam.get("catType");
                    String catName = (String) bodyParam.get("catName");
                    String description = (String) bodyParam.get("description");
                    Category category = new Category();

                    category.setCode(code);
                    category.setValue(value);
                    category.setCatName(catName);
                    category.setCatType(catType);
                    category.setDescription(description);

                    response = new ResponseMessage(new MessageContent(categoryService.saveCategory(category)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thêm mới danh mục", null));
            }
        }
        return response;
    }

    public ResponseMessage updateCategory(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
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
                    String code = (String) bodyParam.get("code");
                    String value = (String) bodyParam.get("value");
                    String catType = (String) bodyParam.get("catType");
                    String catName = (String) bodyParam.get("catName");
                    String description = (String) bodyParam.get("description");
                    Category category = new Category();
                    category.setId(Long.parseLong(pathParam));
                    category.setCode(code);
                    category.setValue(value);
                    category.setCatName(catName);
                    category.setCatType(catType);
                    category.setDescription(description);

                    Response categoryDetailDTO = categoryService.getCategoryById(pathParam);
                    if (categoryDetailDTO != null) {
                        response = new ResponseMessage(new MessageContent(categoryService.updateCategory(category)));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy danh mục tương ứng với id: " + pathParam, null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền sửa danh mục", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteCategory(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response categoryDetailDTO = categoryService.getCategoryById(pathParam);
                if (categoryDetailDTO == null) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy id tương ứng: " + pathParam, null);
                } else {
                    response = new ResponseMessage(new MessageContent(categoryService.deleteCategory(pathParam)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa danh mục", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiCategory(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;

        List<Integer> uuid = (List<Integer>) bodyParam.get("ids");
        ListId listId = new ListId();
        listId.setIds(uuid);

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(categoryService.deleteMultiCategory(listId)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa danh mục", null));
            }
        }
        return response;
    }
}
