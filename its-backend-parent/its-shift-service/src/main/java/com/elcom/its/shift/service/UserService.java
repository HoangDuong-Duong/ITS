/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.dto.UserDTO;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface UserService {

    List<UserDTO> getAllUser();
}
