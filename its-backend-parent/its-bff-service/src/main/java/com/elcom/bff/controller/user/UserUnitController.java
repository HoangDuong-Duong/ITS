/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.controller.user;

import com.elcom.bff.contants.ResourcePath;
import com.elcom.bff.controller.BaseController;
import com.elcom.bff.dto.Response;
import com.elcom.bff.dto.UserShift;
import com.elcom.bff.service.ShiftService;
import com.elcom.bff.service.UserUnitService;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.DateUtil;
import com.elcom.its.utils.StringUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping(ResourcePath.ManagementUserUnit)
public class UserUnitController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserUnitController.class);

    @Autowired
    private UserUnitService userUnitService;

    @Autowired
    private ShiftService shiftService;

    @GetMapping("/all-user/{id}")
    public ResponseEntity<Response> findUserListByUnit(@RequestHeader Map<String, String> headerParam,
            @RequestParam Map<String, String> reqParam, @PathVariable String id) {
        ResponseMessage responseMessage = authenToken(headerParam);
        if (responseMessage == null){
            return new ResponseEntity<>(new Response(HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(), null), HttpStatus.UNAUTHORIZED);
        } else if (responseMessage.getData().getStatus() != HttpStatus.OK.value()) {
            return new ResponseEntity<>(new Response(responseMessage.getData().getStatus(),
                    responseMessage.getData().getMessage(), null), HttpStatus.UNAUTHORIZED);
        } else {
            String date = DateUtil.today("yyyy-MM-dd HH:mm:ss");
            reqParam.put("date", date);
            String urlParam = StringUtil.generateMapString(reqParam);
            // Gọi lấy dữ liệu từ Id
            Response idServiceResponse = userUnitService.findUserByUnit(urlParam, id, headerParam);
            if (idServiceResponse.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(idServiceResponse, HttpStatus.OK);
            }
            List<UserShift> fromIdUserList = (List<UserShift>) idServiceResponse.getData();
            // Gọi shift service lấy dữ liệu trực
            Response shiftServiceResponse = shiftService.findUserInShift(urlParam, headerParam);
            if (shiftServiceResponse.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(shiftServiceResponse, HttpStatus.OK);
            }
            List<String> fromShiftUserIdList = (List<String>) shiftServiceResponse.getData();
            //Set in shift
            if (!CollectionUtils.isEmpty(fromIdUserList) && !CollectionUtils.isEmpty(fromShiftUserIdList)) {
                UserShift userShift = null;
                int size = fromIdUserList.size();
                for (int i = 0; i < size; i++) {
                    userShift = fromIdUserList.get(i);
                    if (fromShiftUserIdList.contains(userShift.getUuid())) {
                        userShift.setInShift(1);
                    } else {
                        userShift.setInShift(0);
                    }
                    fromIdUserList.set(i, userShift);
                }
                //Sort
                Collections.sort(fromIdUserList, (UserShift o1, UserShift o2) -> o2.getInShift().compareTo(o1.getInShift()));
            }
            //Response
            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), fromIdUserList), HttpStatus.OK);
        }
    }
}
