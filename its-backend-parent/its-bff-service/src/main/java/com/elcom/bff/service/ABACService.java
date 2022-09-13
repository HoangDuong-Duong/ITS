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
public interface ABACService {

    public Response saveRole(String urlParam, Map<String, String> headerParam, Map<String, Object> body);
    public Response deleteRole(String urlParam, Map<String, String> headerParam, String pathParam);
    public Response updateRoleUser(String urlParam,Map<String, String> headerParam, Map<String, Object> bodyParam);
}
