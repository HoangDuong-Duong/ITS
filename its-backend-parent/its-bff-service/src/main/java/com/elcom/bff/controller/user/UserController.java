package com.elcom.bff.controller.user;

import com.elcom.bff.contants.ResourcePath;
import com.elcom.bff.controller.BaseController;
import com.elcom.bff.dto.AuthorizationResponseDTO;
import com.elcom.bff.dto.Response;
import com.elcom.bff.dto.Role;
import com.elcom.bff.dto.User;
import com.elcom.bff.model.ManagementUser;
import com.elcom.bff.model.UserDTO;
import com.elcom.bff.service.ABACService;
import com.elcom.bff.service.UserService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(ResourcePath.ManagementUser)
public class UserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ABACService abacService;

    @Autowired
    private BaseController baseController;

    @GetMapping("")
    public ResponseEntity<Response> getUserRole(@RequestHeader Map<String, String> headerParam, @RequestParam Map<String, String> reqParam) throws ExecutionException, InterruptedException, JsonProcessingException {
        ResponseMessage response;
        MessageContent messageContent;
        response = authenToken(headerParam);
        AuthorizationResponseDTO dto = null;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        if(response != null){
            if (response.getData().getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(new Response(response.getData().getStatus(), response.getData().getMessage(), null), HttpStatus.UNAUTHORIZED);
            } else {
                dto = (AuthorizationResponseDTO) response.getData().getData();
                String dtoUuid = mapper.writeValueAsString(dto);
                reqParam.put("dto", dtoUuid);
                String urlParam = StringUtil.generateMapString(reqParam);

                // Gọi lấy dữ liệu từ Id
                Response responseService;
                responseService = userService.getAllUser(urlParam, headerParam);
                Long total = responseService.getTotal();
                List<User> data = new ArrayList<>();
                if (responseService.getStatus() != HttpStatus.OK.value()) {
                    return new ResponseEntity<>(responseService, HttpStatus.OK);
                }
                data = (List<User>) responseService.getData();

                //Gọi Abac lấy role
                responseService = userService.getAllRole(urlParam, headerParam);
                Map<String, List<Role>> mapUuidRole = new HashMap<>();
                if (responseService.getStatus() != HttpStatus.OK.value()) {
                    return new ResponseEntity<>(responseService, HttpStatus.OK);
                }

                mapUuidRole = (Map<String, List<Role>>) responseService.getData();
                List<ManagementUser> managementUserList = new ArrayList<>();
                ManagementUser managementUser;
                if(!CollectionUtils.isEmpty(data)){
                    for (User user : data) {
                        if (mapUuidRole.get(user.getUuid()) == null || mapUuidRole.get(user.getUuid()).isEmpty()) {
                            managementUser = new ManagementUser(user, null);
                        } else {
                            managementUser = new ManagementUser(user, mapUuidRole.get(user.getUuid()).get(0));
                        }
                        managementUserList.add(managementUser);
                    }
                    return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), managementUserList, total), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), null), HttpStatus.OK);
                }

            }
        }else{
            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), null), HttpStatus.OK);
        }

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Response> getUserRoleById(@RequestHeader Map<String, String> headerParam, @RequestParam Map<String, String> reqParam, @PathVariable String id) throws ExecutionException, InterruptedException, JsonProcessingException {
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
            responseService = userService.getUserById(urlParam, headerParam, id);
            User data = null;
            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }
            data = (User) responseService.getData();

            //Gọi Abac lấy role
            responseService = userService.getAllRole(urlParam, headerParam);
            Map<String, List<Role>> mapUuidRole = new HashMap<>();
            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }

            mapUuidRole = (Map<String, List<Role>>) responseService.getData();
            List<ManagementUser> managementUserList = new ArrayList<>();
            ManagementUser managementUser;
            if (mapUuidRole.get(data.getUuid()) == null || mapUuidRole.get(data.getUuid()).isEmpty()) {
                managementUser = new ManagementUser(data, null);
            } else {
                managementUser = new ManagementUser(data, mapUuidRole.get(data.getUuid()).get(0));
            }
            managementUserList.add(managementUser);

            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), managementUserList), HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<Response> createUser(@RequestHeader Map<String, String> headerParam, @RequestBody Map<String, Object> body) throws ExecutionException, InterruptedException, JsonProcessingException {
        ResponseMessage response = authenToken(headerParam);
        if (response.getData().getStatus() != HttpStatus.OK.value()) {
            return new ResponseEntity<>(new Response(response.getData().getStatus(), response.getData().getMessage(), null), HttpStatus.UNAUTHORIZED);
        } else {
            AuthorizationResponseDTO dto = (AuthorizationResponseDTO) response.getData().getData();
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            Map<String, String> reqParam = new HashMap<>();
            String dtoUuid = mapper.writeValueAsString(dto);
            reqParam.put("dto", dtoUuid);
            String urlParam = StringUtil.generateMapString(reqParam);
            Response responseService;
            responseService = userService.saveUser(urlParam, headerParam, body);
            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }
            User data = (User) responseService.getData();
            //Gọi Abac lấy role
            body.put("uuidUser", data.getUuid());
            responseService = abacService.saveRole(urlParam, headerParam, body);
            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }
            UserDTO userDTO;
            Role role = new Role();
            role.setRoleCode((String) body.get("roleCode"));
            role.setId((Integer) body.get("id"));
            role.setDescription((String) body.get("description"));
            userDTO = new UserDTO(data, role);
            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), userDTO), HttpStatus.OK);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> updateUser(@RequestHeader Map<String, String> headerParam, @RequestBody Map<String, Object> body, @PathVariable String id) throws ExecutionException, InterruptedException, JsonProcessingException {
        ResponseMessage response = authenToken(headerParam);
        if (response.getData().getStatus() != HttpStatus.OK.value()) {
            return new ResponseEntity<>(new Response(response.getData().getStatus(), response.getData().getMessage(), null), HttpStatus.UNAUTHORIZED);
        } else {
            AuthorizationResponseDTO dto = (AuthorizationResponseDTO) response.getData().getData();
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            Map<String, String> reqParam = new HashMap<>();
            String dtoUuid = mapper.writeValueAsString(dto);
            reqParam.put("dto", dtoUuid);
            String urlParam = StringUtil.generateMapString(reqParam);
            Response responseService;
            responseService = userService.updateUser(urlParam, headerParam, body, id);
            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }
            User data = (User) responseService.getData();
            //Gọi Abac lấy role
            body.put("uuidUser", data.getUuid());
            if(body.get("roleCode").equals("")){
                body.replace("roleCode",body.get("newRole"));
                responseService = abacService.saveRole(urlParam, headerParam, body);
            } else {
                responseService = abacService.updateRoleUser(urlParam, headerParam, body);
            }

            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }
            UserDTO userDTO;
            Role role = new Role();
            role.setRoleCode((String) body.get("newRole"));
            role.setId((Integer) body.get("id"));
            role.setDescription((String) body.get("description"));
            userDTO = new UserDTO(data, role);
            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), userDTO), HttpStatus.OK);
        }
    }

    @DeleteMapping("**")
    public ResponseEntity<Response> deleteUser(@RequestHeader Map<String, String> headerParam, HttpServletRequest req) throws ExecutionException, InterruptedException, JsonProcessingException {
        String requestPath = req.getRequestURI();
        System.out.println(requestPath);
        ResponseMessage response = authenToken(headerParam);
        AuthorizationResponseDTO dto = null;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        if (response.getData().getStatus() != HttpStatus.OK.value()) {
            return new ResponseEntity<>(new Response(response.getData().getStatus(), response.getData().getMessage(), null), HttpStatus.UNAUTHORIZED);
        } else {
            dto = (AuthorizationResponseDTO) response.getData().getData();
            Map<String, String> reqParam = new HashMap<>();
            String dtoUuid = mapper.writeValueAsString(dto);
            reqParam.put("dto", dtoUuid);

            String urlParam = StringUtil.generateMapString(reqParam);
            String pathParam = "";
            int lastIndex = requestPath.lastIndexOf("/");
            if (lastIndex != -1) {
                String lastStr = requestPath.substring(lastIndex + 1);
                if (StringUtil.isNumberic(lastStr) || StringUtil.isUUID(lastStr)) {
                    requestPath = requestPath.substring(0, lastIndex);
                    pathParam = lastStr;
                }
            }

            // Gọi Id xoa user
            Response responseService = userService.deleteUser(urlParam, headerParam, null, pathParam);
            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }
            Map<String, Object> body = new HashMap<>();
            if (pathParam != null && !pathParam.equals("")) {
                body = new HashMap<>();
                List<String> uuids = new ArrayList<>();
                uuids.add(pathParam);
                body.put("uuids", uuids);
            }
            //Goi abac xoa user
            responseService = userService.deleteRole(urlParam, headerParam, body, pathParam);
            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }

            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), null), HttpStatus.OK);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Response> deleteUserList(@RequestHeader Map<String, String> headerParam, @RequestBody Map<String, Object> body, HttpServletRequest req) throws ExecutionException, InterruptedException, JsonProcessingException {
        ResponseMessage response;
        String requestPath = req.getRequestURI();
        System.out.println(requestPath);
        response = baseController.authenToken(headerParam);
        AuthorizationResponseDTO dto = null;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        if (response.getData().getStatus() != HttpStatus.OK.value()) {
            return new ResponseEntity<>(new Response(response.getData().getStatus(), response.getData().getMessage(), null), HttpStatus.UNAUTHORIZED);
        } else {
            dto = (AuthorizationResponseDTO) response.getData().getData();
            Map<String, String> reqParam = new HashMap<>();
            String dtoUuid = mapper.writeValueAsString(dto);
            reqParam.put("dto", dtoUuid);

            String urlParam = StringUtil.generateMapString(reqParam);
            String pathParam = "";
            int lastIndex = requestPath.lastIndexOf("/");
            if (lastIndex != -1) {
                String lastStr = requestPath.substring(lastIndex + 1);
                if (StringUtil.isNumberic(lastStr) || StringUtil.isUUID(lastStr)) {
                    requestPath = requestPath.substring(0, lastIndex);
                    pathParam = lastStr;
                }
            }

            // Gọi Id xoa user
            Response responseService = userService.deleteUser(urlParam, headerParam, body, pathParam);
            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }
            //Goi abac xoa user
            responseService = userService.deleteRole(urlParam, headerParam, body, null);
            if (responseService.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(responseService, HttpStatus.OK);
            }

            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), null), HttpStatus.OK);
        }
    }


    

//    @GetMapping(value = "site/{siteId}")
//    public ResponseEntity<Response> getUserRoleBySite(@RequestHeader Map<String, String> headerParam, @RequestParam Map<String, String> reqParam, @PathVariable String siteId) throws ExecutionException, InterruptedException, JsonProcessingException {
//        ResponseMessage response;
//        response = authenToken(headerParam);
//        AuthorizationResponseDTO dto = null;
//        ObjectMapper mapper = new ObjectMapper();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        mapper.setDateFormat(df);
//        if (response.getData().getStatus() != HttpStatus.OK.value()) {
//            return new ResponseEntity<>(new Response(response.getData().getStatus(), response.getData().getMessage(), null), HttpStatus.UNAUTHORIZED);
//        } else {
//            dto = (AuthorizationResponseDTO) response.getData().getData();
//            String dtoUuid = mapper.writeValueAsString(dto);
//            reqParam.put("dto", dtoUuid);
//            String urlParam = StringUtil.generateMapString(reqParam);
//
//            // Gọi lấy dữ liệu từ Id
//            Response responseService;
//            responseService = userService.getUserBySiteId(urlParam, headerParam, siteId);
//            List<User> data = null;
//            if (responseService.getStatus() != HttpStatus.OK.value()) {
//                return new ResponseEntity<>(responseService, HttpStatus.OK);
//            }
//            data = (List<User>) responseService.getData();
//
//            //Gọi Abac lấy role
//            responseService = userService.getAllRole(urlParam, headerParam);
//            Map<String, List<Role>> mapUuidRole = new HashMap<>();
//            if (responseService.getStatus() != HttpStatus.OK.value()) {
//                return new ResponseEntity<>(responseService, HttpStatus.OK);
//            }
//
//            mapUuidRole = (Map<String, List<Role>>) responseService.getData();
//            List<ManagementUser> managementUserList = new ArrayList<>();
//            ManagementUser managementUser;
//            for (User user : data) {
//                if (mapUuidRole.get(user.getUuid()) == null || mapUuidRole.get(user.getUuid()).isEmpty()) {
//                    managementUser = new ManagementUser(user, null);
//                } else {
//                    managementUser = new ManagementUser(user, mapUuidRole.get(user.getUuid()).get(0));
//                }
//                managementUserList.add(managementUser);
//            }
//            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), managementUserList), HttpStatus.OK);
//        }
//    }

}
