/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.Unit;
import com.elcom.its.management.dto.User;
import com.elcom.its.management.dto.UserDTO;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface UserService {

    List<User> findByListId(String uuid);

    User findById(String uuid);

    List<UserDTO> transform(List<User> listUser);
    
    Unit findUnitById(String unitId);
}
