/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static final String DEFAULT_ROLE = "ITS_USER";

    // Fix ADMIN service
    // USER_SERVICE ==> USER_ADMIN, STORE_SERVICE ==> STORE_ADMIN,...
    public static final Map<String, String> SERVICE_ADMIN_MAP = new HashMap<>();

    //Fiter time level
    public static final String[] FILTER_TIME_LEVEL = {"hour", "day", "week", "month", "year"};

    //Filter object type
    public static final String[] FILTER_OBJECT_TYPE = {"site", "cam"};

    //Vds event code
    public static final List<String> VDS_EVENT_CODE = new ArrayList<>(
            List.of("WRONGLINE", "ACCIDENT", "FIRE", "CROWD", "BROKENVEHICLE",
                    "ENCROACHING", "BARRIER", "RESTRICTED", "INVERSEDIRECTION",
                    "OBJECTTRACK", "SPEEDLIMIT", "ILLEGALSTOP", "NOENTRY", "TRAFFICJAM"));
    
    //Vds traffic event code
    public static final List<String> VDS_VIOLATION_CODE = new ArrayList<>(
            List.of("WRONGLINE", "RESTRICTED", "INVERSEDIRECTION",
                    "SPEEDLIMIT", "ILLEGALSTOP", "NOENTRY"));
    
    //Vds security event code
    public static final List<String> VDS_SECURITY_CODE = new ArrayList<>(
            List.of("ACCIDENT", "FIRE", "CROWD", "BROKENVEHICLE", "ENCROACHING",
                    "BARRIER", "OBJECTTRACK", "TRAFFICJAM"));

}
