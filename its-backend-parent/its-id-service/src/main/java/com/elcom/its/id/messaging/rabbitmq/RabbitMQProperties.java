/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.id.messaging.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class RabbitMQProperties {

    @Value("${user.rpc.authen.url}")
    public static String USER_RPC_AUTHEN_URL;

    @Value("${user.rpc.exchange}")
    public static String USER_RPC_EXCHANGE;

    @Value("${user.rpc.queue}")
    public static String USER_RPC_QUEUE;

    @Value("${user.rpc.key}")
    public static String USER_RPC_KEY;

    //RBAC service
    @Value("${rbac.rpc.exchange}")
    public static String RBAC_RPC_EXCHANGE;

    @Value("${rbac.rpc.queue}")
    public static String RBAC_RPC_QUEUE;

    @Value("${rbac.rpc.key}")
    public static String RBAC_RPC_KEY;

    @Value("${rbac.rpc.default.role.url}")
    public static String RBAC_RPC_DEFAULT_ROLE_URL;

    @Value("${rbac.rpc.author.url}")
    public static String RBAC_RPC_AUTHOR_URL;

    @Value("${rbac.rpc.admin.url}")
    public static String RBAC_RPC_ADMIN_URL;

    //ABAC service
    @Value("${abac.rpc.exchange}")
    public static String ABAC_RPC_EXCHANGE;

    @Value("${abac.rpc.queue}")
    public static String ABAC_RPC_QUEUE;

    @Value("${abac.rpc.key}")
    public static String ABAC_RPC_KEY;

    @Value("${abac.rpc.author.url}")
    public static String ABAC_RPC_AUTHOR_URL;

    @Value("${systemconfig.rpc.queue}")
    public static String SYSTEM_CONFIG_RPC_QUEUE;

    @Value("${systemconfig.rpc.exchange}")
    public static String SYSTEM_CONFIG_RPC_EXCHANGE;

    @Value("${systemconfig.rpc.key}")
    public static String SYSTEM_CONFIG_RPC_KEY;

    @Value("${delete.data.publish.exchange}")
    public static String DELETE_DATA_PUBLISH_EXCHANGE;

    @Value("${delete.data.publish.queue}")
    public static String DELETE_DATA_PUBLISH_QUEUE;

    @Value("${delete.data.publish.key}")
    public static String DELETE_DATA_PUBLISH_KEY;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
            @Value("${user.rpc.queue}") String userRpcQueue,
            @Value("${user.rpc.key}") String userRpcKey,
            @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
            @Value("${rbac.rpc.exchange}") String rbacRpcExchange,
            @Value("${rbac.rpc.queue}") String rbacRpcQueue,
            @Value("${rbac.rpc.key}") String rbacRpcKey,
            @Value("${rbac.rpc.default.role.url}") String rbacRpcDefaultRoleUrl,
            @Value("${rbac.rpc.author.url}") String rbacRpcAuthorUrl,
            @Value("${rbac.rpc.admin.url}") String rbacRpcAdminUrl,
            @Value("${abac.rpc.exchange}") String abacRpcExchange,
            @Value("${abac.rpc.queue}") String abacRpcQueue,
            @Value("${abac.rpc.key}") String abacRpcKey,
            @Value("${abac.rpc.author.url}") String abacRpcAuthorUrl,
            @Value("${systemconfig.rpc.queue}") String systemConfigRpcQueue,
            @Value("${systemconfig.rpc.exchange}") String systemConfigRpcExchange,
            @Value("${systemconfig.rpc.key}") String systemConfigRpcKey,
            @Value("${delete.data.publish.exchange}") String deleteDataPublishExchange,
            @Value("${delete.data.publish.queue}") String deleteDataPublishQueue,
            @Value("${delete.data.publish.key}") String deleteDataPublishKey
    ) {

        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;

        //RBAC
        RBAC_RPC_EXCHANGE = rbacRpcExchange;
        RBAC_RPC_QUEUE = rbacRpcQueue;
        RBAC_RPC_KEY = rbacRpcKey;
        RBAC_RPC_DEFAULT_ROLE_URL = rbacRpcDefaultRoleUrl;
        RBAC_RPC_AUTHOR_URL = rbacRpcAuthorUrl;

        //ABAC
        ABAC_RPC_EXCHANGE = abacRpcExchange;
        ABAC_RPC_QUEUE = abacRpcQueue;
        ABAC_RPC_KEY = abacRpcKey;
        ABAC_RPC_AUTHOR_URL = abacRpcAuthorUrl;
        RBAC_RPC_ADMIN_URL = rbacRpcAdminUrl;
        SYSTEM_CONFIG_RPC_QUEUE = systemConfigRpcQueue;
        SYSTEM_CONFIG_RPC_EXCHANGE = systemConfigRpcExchange;
        SYSTEM_CONFIG_RPC_KEY = systemConfigRpcKey;

        DELETE_DATA_PUBLISH_EXCHANGE = deleteDataPublishExchange;
        DELETE_DATA_PUBLISH_QUEUE = deleteDataPublishQueue;
        DELETE_DATA_PUBLISH_KEY = deleteDataPublishKey;

    }
}
