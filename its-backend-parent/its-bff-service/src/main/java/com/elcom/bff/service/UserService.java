/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.service;

import com.elcom.bff.dto.Response;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface UserService {
     public Response getAllUser(String urlParam, Map<String,String> headerParam);
     public Response getUserById(String urlParam, Map<String,String> headerParam, String pathParam);
     public Response getUserBySiteId(String urlParam, Map<String,String> headerParam, String pathParam);
     public Response getAllRole(String urlParam, Map<String,String> headerParam);
     public Response saveUser(String urlParam, Map<String, String> headerParam, Map<String, Object> body);
     public Response updateUser(String urlParam, Map<String, String> headerParam, Map<String, Object> body,String pathParam);
     public Response deleteUser(String urlParam, Map<String, String> headerParam, Map<String, Object> body,String pathParam);
     public Response deleteRole(String urlParam, Map<String,String> headerParam, Map<String,Object> body,String pathParam);
}
