/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.service;

import com.elcom.bff.dto.Response;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface ShiftService {

    Response findUserInShift(String urlParam, Map<String, String> headerParam);
}
