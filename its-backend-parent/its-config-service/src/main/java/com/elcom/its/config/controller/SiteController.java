package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Site;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.SiteService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SiteController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);
    @Autowired
    private SiteService siteService;

    public static Map<String, String> getUrlParamValuesNoDecode(String url) {
        Map<String, String> paramsMap = new HashMap();
        String[] params = url.split("&");
        String[] var4 = params;
        int var5 = params.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String param = var4[var6];
            String[] temp = param.split("=");

            try {
                paramsMap.put(temp[0], temp.length > 1 ? temp[1] : "");
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        }

        return paramsMap;
    }

    public ResponseMessage getAllSite(Map<String, String> headerParam, String requestPath,
            String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    if (StringUtil.isNullOrEmpty(urlParam)) {
                        response = new ResponseMessage(new MessageContent(siteService.getAllSite(stages, false)));
                    } else {
                        Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                        String search = params.get("search");
                        String stageCodes = params.get("stageCodes");
                        if (!StringUtil.isNullOrEmpty(stageCodes)) {
                            Response getSiteByStageResponse = siteService.getListSiteInListStage(stageCodes);
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), getSiteByStageResponse.getData()));
                        } else if (page != null && size != null) {
                            Response stretchPaginationDTO = siteService.getAllSite(stages, page, size, search, false);
                            if (stretchPaginationDTO != null && stretchPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), stretchPaginationDTO.getData(), stretchPaginationDTO.getTotal()));
                            }
                        } else {
                            return new ResponseMessage(new MessageContent(siteService.getAllSite(stages, false)));
                        }
                    }
                } else {
                    if (StringUtil.isNullOrEmpty(urlParam)) {
                        response = new ResponseMessage(new MessageContent(siteService.getAllSite(null, true)));
                    } else {
                        Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                        String search = params.get("search");
                        String stageCodes = params.get("stageCodes");
                        if (!StringUtil.isNullOrEmpty(stageCodes)) {
                            Response getSiteByStageResponse = siteService.getListSiteInListStage(stageCodes);
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), getSiteByStageResponse.getData()));
                        }
                        else if (page != null && size != null) {
                            Response stretchPaginationDTO = siteService.getAllSite(null, page, size, search, true);
                            if (stretchPaginationDTO != null && stretchPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), stretchPaginationDTO.getData(), stretchPaginationDTO.getTotal()));
                            }
                        } else {
                            return new ResponseMessage(new MessageContent(siteService.getAllSite(null, true)));
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách vị trí", null));
            }

        }
        return response;
    }

    public ResponseMessage getAllSiteHaveCamera(Map<String, String> headerParam, String requestPath,
                                      String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    Response siteDetailDTO = siteService.getAllSiteHaveCam(stages, false);
                    response = new ResponseMessage(new MessageContent(siteDetailDTO));
                } else {
                    Response siteDetailDTO = siteService.getAllSiteHaveCam(null, true);
                    response = new ResponseMessage(new MessageContent(siteDetailDTO));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết vị trí", null));
            }
        }
        return response;
    }

    public ResponseMessage getSiteById(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    Response siteDetailDTO = siteService.getSiteById(stages, paramPath, false);
                    response = new ResponseMessage(new MessageContent(siteDetailDTO));
                } else {
                    Response siteDetailDTO = siteService.getSiteById(null, paramPath, true);
                    response = new ResponseMessage(new MessageContent(siteDetailDTO));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết vị trí", null));
            }
        }
        return response;
    }

    public ResponseMessage createSite(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "POST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                String stages;
                Boolean isAdmin;
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    stages = dto.getUnit().getLisOfStage();
                    isAdmin = false;
                } else {
                    stages = null;
                    isAdmin = true;
                }

                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String name = (String) bodyParam.get("name");
                    String address = (String) bodyParam.get("address");
                    String note = (String) bodyParam.get("note");
                    String code = (String) bodyParam.get("code");
                    Float longitude = null;
                    Float latitude = null;
                    Long wardId = null;
                    Long provinceId = null;
                    Long districtId = null;
                    Integer km = null;
                    Integer m = null;
                    if (bodyParam.get("longitude") != null) {
                        longitude = Float.parseFloat(bodyParam.get("longitude").toString());
                    }
                    if (bodyParam.get("latitude") != null) {
                        latitude = Float.parseFloat(bodyParam.get("latitude").toString());
                    }
                    if (bodyParam.get("wardId") != null) {
                        wardId = Long.parseLong(bodyParam.get("wardId").toString());
                    }
                    if (bodyParam.get("provinceId") != null) {
                        provinceId = Long.parseLong(bodyParam.get("provinceId").toString());
                    }
                    if (bodyParam.get("districtId") != null) {
                        districtId = Long.parseLong(bodyParam.get("districtId").toString());
                    }
                    if (bodyParam.get("km") != null) {
                        km = Integer.parseInt(bodyParam.get("km").toString());
                    }
                    if (bodyParam.get("m") != null) {
                        m = Integer.parseInt(bodyParam.get("m").toString());
                    }

                    Site site = new Site();

                    site.setName(name);
                    site.setAddress(address);
                    site.setNote(note);
                    site.setLatitude(latitude);
                    site.setLongitude(longitude);
                    site.setWardId(wardId);
                    site.setDistrictId(districtId);
                    site.setProvinceId(provinceId);
                    site.setKm(km);
                    site.setM(m);
                    site.setCode(code);

                    response = new ResponseMessage(new MessageContent(siteService.saveSite(stages, site, isAdmin)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền tạo mới vị trí", null));
            }
        }
        return response;
    }

    public ResponseMessage updateRoute(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                String stages;
                Boolean isAdmin;
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    stages = dto.getUnit().getLisOfStage();
                    isAdmin = false;
                } else {
                    stages = null;
                    isAdmin = true;
                }

                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String name = (String) bodyParam.get("name");
                    String address = (String) bodyParam.get("address");
                    String note = (String) bodyParam.get("note");
                    String code = (String) bodyParam.get("code");
                    Float longitude = null;
                    Float latitude = null;
                    Long wardId = null;
                    Long provinceId = null;
                    Long districtId = null;
                    Integer km = null;
                    Integer m = null;
                    if (bodyParam.get("longitude") != null) {
                        longitude = Float.parseFloat(bodyParam.get("longitude").toString());
                    }
                    if (bodyParam.get("latitude") != null) {
                        latitude = Float.parseFloat(bodyParam.get("latitude").toString());
                    }
                    if (bodyParam.get("wardId") != null) {
                        wardId = Long.parseLong(bodyParam.get("wardId").toString());
                    }
                    if (bodyParam.get("provinceId") != null) {
                        provinceId = Long.parseLong(bodyParam.get("provinceId").toString());
                    }
                    if (bodyParam.get("districtId") != null) {
                        districtId = Long.parseLong(bodyParam.get("districtId").toString());
                    }
                    if (bodyParam.get("km") != null) {
                        km = Integer.parseInt(bodyParam.get("km").toString());
                    }
                    if (bodyParam.get("m") != null) {
                        m = Integer.parseInt(bodyParam.get("m").toString());
                    }

                    Site site = new Site();
                    site.setId(pathParam);
                    site.setName(name);
                    site.setAddress(address);
                    site.setNote(note);
                    site.setLatitude(latitude);
                    site.setLongitude(longitude);
                    site.setWardId(wardId);
                    site.setDistrictId(districtId);
                    site.setProvinceId(provinceId);
                    site.setKm(km);
                    site.setM(m);
                    site.setCode(code);

                    Response siteDetailDTO = siteService.getSiteById(null, pathParam, true);
                    if (siteDetailDTO != null) {
                        response = new ResponseMessage(new MessageContent(siteService.updateSite(stages, site, isAdmin)));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy site tương ứng với id: " + pathParam, null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền sửa vị trí", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteSite(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);

            String stages;
            Boolean isAdmin;
            if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                stages = dto.getUnit().getLisOfStage();
                isAdmin = false;
            } else {
                stages = null;
                isAdmin = true;
            }

            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response siteDetailDTO = siteService.getSiteById(null, pathParam, true);
                if (siteDetailDTO == null) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy id tương ứng: " + pathParam, null);
                } else {
                    response = new ResponseMessage(new MessageContent(siteService.deleteSite(stages, pathParam, isAdmin)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa vị trí", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiSite(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
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
                String stages;
                Boolean isAdmin;
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    stages = dto.getUnit().getLisOfStage();
                    isAdmin = false;
                } else {
                    stages = null;
                    isAdmin = true;
                }
                response = new ResponseMessage(new MessageContent(siteService.deleteMultiSite(stages, listUuid, isAdmin)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa vị trí", null));
            }
        }
        return response;
    }

    public ResponseMessage getAllProvinces(Map<String, String> headerParam, String requestPath, String requestMethod, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(new MessageContent(siteService.getProvinces()));
        }
        return response;
    }

    public ResponseMessage getAllDistrict(Map<String, String> headerParam, String requestPath, String requestMethod, String urlParam, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(new MessageContent(siteService.getDistricts(pathParam)));
        }
        return response;
    }

    public ResponseMessage getAllWard(Map<String, String> headerParam, String requestPath, String requestMethod, String urlParam, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(new MessageContent(siteService.getWards(pathParam)));
        }
        return response;
    }

    public ResponseMessage getPointByPositionM(Map<String, String> headerParam, String requestPath, String requestMethod, String urlParam, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Response getPointResponse = siteService.getPointByPositionM(urlParam);
            response = new ResponseMessage(new MessageContent(getPointResponse.getStatus(), getPointResponse.getMessage(), getPointResponse.getData()));
        }
        return response;
    }

    public ResponseMessage filterSite(Map<String, String> headerParam, String requestPath,
            String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                String stages;
                Boolean isAdmin;
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    stages = dto.getUnit().getLisOfStage();
                    isAdmin = false;
                } else {
                    stages = null;
                    isAdmin = true;
                }

                if (StringUtil.isNullOrEmpty(urlParam)) {
                    response = new ResponseMessage(new MessageContent(siteService.getAllSite(stages, isAdmin)));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String id = params.get("id");
                    String name = params.get("name");
                    Long districtId = Long.parseLong(params.get("districtId"));
                    Long provinceId = Long.parseLong(params.get("provinceId"));
                    Long wardId = Long.parseLong(params.get("wardId"));
                    Float km = Float.parseFloat(params.get("km"));
                    if (page != null && size != null || id != null || name != null || districtId != null || provinceId != null || wardId != null || km != null) {
                        Response distancePaginationDTO = siteService.filter(stages, page, size, id, name, provinceId, districtId, wardId, km, isAdmin);
                        if (distancePaginationDTO != null && distancePaginationDTO.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), distancePaginationDTO.getData(), distancePaginationDTO.getTotal()));
                        }
                    } else {
                        return new ResponseMessage(new MessageContent(siteService.getAllSite(stages, isAdmin)));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem", null));
            }

        }
        return response;
    }

    public ResponseMessage createListSite(Map<String, String> headerParam, String requestPath,
            String urlParam, Map<String, Object> bodyParam) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<SiteTestData> listData = objectMapper.convertValue(bodyParam.get("data"), new TypeReference<List<SiteTestData>>() {
//        });
//        for (SiteTestData dataTest : listData) {
//            Site site = new Site();
//            site.setName(dataTest.getName());
//            site.setAddress("Địa chỉ real - sửa thì chịu trách nhiệm nhá");
//            site.setNote("Thêm mới data");
//            site.setLatitude(Float.parseFloat(dataTest.getLatitude()));
//            site.setLongitude(Float.parseFloat(dataTest.getLongitude()));
//            site.setWardId(1l);
//            site.setDistrictId(1l);
//            site.setProvinceId(1l);
//            String[] nameSplit = dataTest.getName().split("\\+");
//            site.setKm(Integer.parseInt(nameSplit[0].substring(2)));
//            site.setM(Integer.parseInt(nameSplit[1]));
//            site.setCode(UUID.randomUUID().toString());
//            siteService.saveSite(site);
//        }
//
//        return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "OK", null));
        return null;
    }
}
