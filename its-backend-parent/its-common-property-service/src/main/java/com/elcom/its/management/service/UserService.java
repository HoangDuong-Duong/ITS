/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.Unit;
import com.elcom.its.management.dto.User;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface UserService {

    List<User> findByListId(String uuid);

    Unit findUnitById(String unitId);
}
