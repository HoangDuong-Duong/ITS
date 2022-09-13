/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Admin
 */
@Component
public class RabbitMQProperties {

    //User service
    @Value("${user.rpc.exchange}")
    public static String USER_RPC_EXCHANGE;

    @Value("${user.rpc.queue}")
    public static String USER_RPC_QUEUE;

    @Value("${user.rpc.key}")
    public static String USER_RPC_KEY;

    @Value("${user.all.url}")
    public static String USER_ALL_URL;

    @Value("${user.add.url}")
    public static String USER_ADD_URL;

    @Value("${user.add.url}")
    public static String USER_DELETE_URL;

    @Value("${user.rpc.detail.url}")
    public static String USER_DETAIL_URL;

    @Value("${user.detail.list.url}")
    public static String USER_DETAIL_LIST_URL;

    @Value("${user.rpc.auth.url}")
    public static String AUTHENTICATION_URL;

    @Value("${user.rpc.internal.url}")
    public static String USER_RPC_UUIDLIST_URL;
    
    @Value("${user.by.unit}")
    public static String USER_BY_UNIT_URL;

    //Abac service
    @Value("${abac.rpc.exchange}")
    public static String ABAC_RPC_EXCHANGE;

    @Value("${abac.rpc.queue}")
    public static String ABAC_RPC_QUEUE;

    @Value("${abac.rpc.key}")
    public static String ABAC_RPC_KEY;

    @Value("${abac.role.url}")
    public static String ABAC_ROLE_URL;

    @Value("${abac.add.role.url}")
    public static String ABAC_ADD_ROLE_URL;

    @Value("${abac.delete.role.url}")
    public static String ABAC_DELETE_ROLE_URL;

    //Manament service
    @Value("${management.rpc.exchange}")
    public static String MANAGEMENT_RPC_EXCHANGE;

    @Value("${management.rpc.queue}")
    public static String MANAGEMENT_RPC_QUEUE;

    @Value("${management.rpc.key}")
    public static String MANAGEMENT_RPC_KEY;

    @Value("${management.history.url}")
    public static String MANAGEMENT_HISTORY_URL;
    
    //Shift service
    @Value("${shift.rpc.exchange}")
    public static String SHIFT_RPC_EXCHANGE;

    @Value("${shift.rpc.queue}")
    public static String SHIFT_RPC_QUEUE;

    @Value("${shift.rpc.key}")
    public static String SHIFT_RPC_KEY;

    @Value("${shift.user.url}")
    public static String SHIFT_USER_URL;
    
    //Shift service
    @Value("${report.rpc.exchange}")
    public static String REPORT_RPC_EXCHANGE;

    @Value("${report.rpc.queue}")
    public static String REPORT_RPC_QUEUE;

    @Value("${report.rpc.key}")
    public static String REPORT_RPC_KEY;

    @Value("${report.violation.url}")
    public static String REPORT_VIOLATION_URL;
   
    @Autowired
    public RabbitMQProperties(
            @Value("${user.rpc.exchange}") String userRpcExchange,
            @Value("${user.rpc.queue}") String userRpcQueue,
            @Value("${user.rpc.key}") String userRpcKey,
            @Value("${user.all.url}") String userAllUrl,
            @Value("${user.add.url}") String userAddUrl,
            @Value("${user.delete.url}") String userDeleteUrl,
            @Value("${user.rpc.detail.url}") String userDetailUrl,
            @Value("${user.detail.list.url}") String userDetailListUrl,
            @Value("${user.rpc.internal.url}") String userInternalUrl,
            @Value("${user.by.unit}") String userByUnitUrl,
            @Value("${abac.rpc.exchange}") String abacRpcExchange,
            @Value("${abac.rpc.queue}") String abacRpcQueue,
            @Value("${abac.rpc.key}") String abacRpcKey,
            @Value("${abac.role.url}") String abacRoleUrl,
            @Value("${abac.add.role.url}") String abacAddRoleUrl,
            @Value("${abac.delete.role.url}") String abacDeleteRoleUrl,
            @Value("${user.rpc.auth.url}") String authUrl,
            @Value("${management.rpc.exchange}") String managementRpcExchange,
            @Value("${management.rpc.queue}") String managementRpcQueue,
            @Value("${management.rpc.key}") String managementRpcKey,
            @Value("${management.history.url}") String managementHistory,
            @Value("${shift.rpc.exchange}") String shiftRpcExchange,
            @Value("${shift.rpc.queue}") String shiftRpcQueue,
            @Value("${shift.rpc.key}") String shiftRpcKey,
            @Value("${shift.user.url}") String shiftUser,
            @Value("${report.rpc.exchange}") String reportRpcExchange,
            @Value("${report.rpc.queue}") String reportRpcQueue,
            @Value("${report.rpc.key}") String reportRpcKey,
            @Value("${report.violation.url}") String reportViolation
    ) {
        //User
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_ALL_URL = userAllUrl;
        USER_ADD_URL = userAddUrl;
        USER_DELETE_URL = userDeleteUrl;
        USER_DETAIL_URL = userDetailUrl;
        USER_DETAIL_LIST_URL = userDetailListUrl;
        AUTHENTICATION_URL = authUrl;
        USER_RPC_UUIDLIST_URL = userInternalUrl;
        USER_BY_UNIT_URL = userByUnitUrl;

        //Abac
        ABAC_RPC_EXCHANGE = abacRpcExchange;
        ABAC_ROLE_URL = abacRoleUrl;
        ABAC_RPC_QUEUE = abacRpcQueue;
        ABAC_RPC_KEY = abacRpcKey;
        ABAC_ADD_ROLE_URL = abacAddRoleUrl;
        ABAC_DELETE_ROLE_URL = abacDeleteRoleUrl;

        //Management
        MANAGEMENT_HISTORY_URL = managementHistory;
        MANAGEMENT_RPC_EXCHANGE = managementRpcExchange;
        MANAGEMENT_RPC_QUEUE = managementRpcQueue;
        MANAGEMENT_RPC_KEY = managementRpcKey;
        
        //Shift
        SHIFT_USER_URL = shiftUser;
        SHIFT_RPC_EXCHANGE = shiftRpcExchange;
        SHIFT_RPC_QUEUE = shiftRpcQueue;
        SHIFT_RPC_KEY = shiftRpcKey;
        
        //Report
        REPORT_VIOLATION_URL = reportViolation;
        REPORT_RPC_EXCHANGE = reportRpcExchange;
        REPORT_RPC_QUEUE = reportRpcQueue;
        REPORT_RPC_KEY = reportRpcKey;
    }
}
