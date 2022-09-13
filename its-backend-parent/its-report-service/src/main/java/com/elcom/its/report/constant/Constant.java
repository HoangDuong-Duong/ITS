/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class Constant {
    
    // Validation message
    public static final String VALIDATION_INVALID_PARAM_VALUE = "Tham số truyền lên không đúng";
    public static final String VALIDATION_DATA_NOT_FOUND = "Không tìm thấy dữ liệu";
    
    // Default Role
    public static final String DEFAULT_ROLE = "gsan_USER";
    
    // Fix ADMIN service
    // USER_SERVICE ==> USER_ADMIN, STORE_SERVICE ==> STORE_ADMIN,...
    public static final Map<String, String> SERVICE_ADMIN_MAP = new HashMap<>();

    public static final String[] FILTER_TIME_LEVEL = {"hour", "day", "week", "month", "year"};

    public static final String[] FILTER_OBJECT_TYPE = {"site", "cam"};
}
