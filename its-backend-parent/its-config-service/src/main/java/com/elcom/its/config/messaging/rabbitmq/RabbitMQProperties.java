/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.messaging.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
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

    @Value("${user.rpc.authen.url}")
    public static String USER_RPC_AUTHEN_URL;

    @Value("${user.rpc.uuidLst.url}")
    public static String USER_RPC_UUIDLIST_URL;

    @Value("${user.rpc.internal.list.url}")
    public static String USER_RPC_INTERNAL_LIST_URL;

    @Value("${group.rpc.internal.list.url}")
    public static String GROUP_RPC_INTERNAL_LIST_URL;

    @Value("${user.rpc.internal.list.bygroup.url}")
    public static String USER_RPC_INTERNAL_LIST_BYGROUP_URL;

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

    //Config
    @Value("${systemconfig.rpc.queue}")
    public static String SYSTEMCONFIG_RPC_QUEUE;

    @Value("${systemconfig.rpc.exchange}")
    public static String SYSTEMCONFIG_RPC_EXCHANGE;

    @Value("${systemconfig.rpc.key}")
    public static String SYSTEMCONFIG_RPC_KEY;

    @Value("${systemconfig.rpc.sites.list}")
    public static String SYSTEMCONFIG_RPC_SITES_LIST;

    @Value("${systemconfig.rpc.cameraDTO.list}")
    public static String SYSTEMCONFIG_RPC_CAMERA_LIST;

    @Value("${notify.worker.message.queue}")
    public static String WORKER_QUEUE_MESSAGE;

    //Config
    @Value("${socket.rpc.queue.name}")
    public static String SOCKET_RPC_QUEUE;

    @Value("${socket.rpc.exchange.name}")
    public static String SOCKET_RPC_EXCHANGE;

    @Value("${socket.rpc.routing.key}")
    public static String SOCKET_RPC_KEY;

    //ABAC service
    @Value("${abac.rpc.exchange}")
    public static String ABAC_RPC_EXCHANGE;

    @Value("${abac.rpc.queue}")
    public static String ABAC_RPC_QUEUE;

    @Value("${abac.rpc.key}")
    public static String ABAC_RPC_KEY;

    @Value("${abac.rpc.default.role.url}")
    public static String ABAC_RPC_DEFAULT_ROLE_URL;

    @Value("${abac.rpc.author.url}")
    public static String ABAC_RPC_AUTHOR_URL;

    @Value("${abac.rpc.getRole.url}")
    public static String ABAC_RPC_GET_ROLE_URL;

    @Value("${abac.rpc.getRoleByUserId.url}")
    public static String ABAC_RPC_GET_ROLE_BY_USER_URL;

    @Value("${abac.rpc.admin.url}")
    public static String ABAC_RPC_ADMIN_URL;

    @Value("${abac.rpc.attribute.url}")
    public static String ABAC_RPC_ATTRIBUTE_URL;

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
            @Value("${user.rpc.uuidLst.url}") String userRpcUuidLstUrl,
            @Value("${user.rpc.internal.list.url}") String userRpcInternalLstUrl,
            @Value("${group.rpc.internal.list.url}") String groupRpcInternalLstUrl,
            @Value("${user.rpc.internal.list.bygroup.url}") String userRpcInternalLstByGroupUrl,
            @Value("${rbac.rpc.exchange}") String rbacRpcExchange,
            @Value("${rbac.rpc.queue}") String rbacRpcQueue,
            @Value("${rbac.rpc.key}") String rbacRpcKey,
            @Value("${rbac.rpc.default.role.url}") String rbacRpcDefaultRoleUrl,
            @Value("${rbac.rpc.author.url}") String rbacRpcAuthorUrl,
            @Value("${rbac.rpc.admin.url}") String rbacRpcAdminUrl,
            @Value("${systemconfig.rpc.queue}") String systemconfigRpcQueue,
            @Value("${systemconfig.rpc.exchange}") String systemconfigRpcExchange,
            @Value("${systemconfig.rpc.key}") String systemconfigRpcKey,
            @Value("${socket.rpc.queue.name}") String socketRpcQueue,
            @Value("${socket.rpc.exchange.name}") String socketRpcExchange,
            @Value("${socket.rpc.routing.key}") String socketRpcKey,
            @Value("${systemconfig.rpc.sites.list}") String systemconfigRpcSitesList,
            @Value("${systemconfig.rpc.camera.list}") String systemconfigRpcCameraList,
            @Value("${notify.worker.message.queue}") String messageQueueWorker,
            @Value("${abac.rpc.exchange}") String abacRpcExchange,
            @Value("${abac.rpc.queue}") String abacRpcQueue,
            @Value("${abac.rpc.key}") String abacRpcKey,
            @Value("${abac.rpc.default.role.url}") String abacRpcDefaultRoleUrl,
            @Value("${abac.rpc.author.url}") String abacRpcAuthorUrl,
            @Value("${abac.rpc.admin.url}") String abacRpcAdminUrl,
            @Value("${abac.rpc.attribute.url}") String abacRpcAttributeUrl,
            @Value("${abac.rpc.getRole.url}") String getRoleUrlString,
            @Value("${abac.rpc.getRoleByUserId.url}") String getRoleUrlByUserString,
            @Value("${delete.data.publish.exchange}") String deleteDataPublishExchange,
            @Value("${delete.data.publish.queue}") String deleteDataPublishQueue,
            @Value("${delete.data.publish.key}") String deleteDataPublishKey) {
        //User
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;
        USER_RPC_UUIDLIST_URL = userRpcUuidLstUrl;
        USER_RPC_INTERNAL_LIST_URL = userRpcInternalLstUrl;
        GROUP_RPC_INTERNAL_LIST_URL = groupRpcInternalLstUrl;
        USER_RPC_INTERNAL_LIST_BYGROUP_URL = userRpcInternalLstByGroupUrl;

        //RBAC
        RBAC_RPC_EXCHANGE = rbacRpcExchange;
        RBAC_RPC_QUEUE = rbacRpcQueue;
        RBAC_RPC_KEY = rbacRpcKey;
        RBAC_RPC_DEFAULT_ROLE_URL = rbacRpcDefaultRoleUrl;
        RBAC_RPC_AUTHOR_URL = rbacRpcAuthorUrl;
        RBAC_RPC_ADMIN_URL = rbacRpcAdminUrl;

        //CONFIG
        SYSTEMCONFIG_RPC_EXCHANGE = systemconfigRpcExchange;
        SYSTEMCONFIG_RPC_QUEUE = systemconfigRpcQueue;
        SYSTEMCONFIG_RPC_KEY = systemconfigRpcKey;
        SYSTEMCONFIG_RPC_SITES_LIST = systemconfigRpcSitesList;
        SYSTEMCONFIG_RPC_CAMERA_LIST = systemconfigRpcCameraList;
        WORKER_QUEUE_MESSAGE = messageQueueWorker;

        //SOCKET
        SOCKET_RPC_QUEUE = socketRpcQueue;
        SOCKET_RPC_EXCHANGE = socketRpcExchange;
        SOCKET_RPC_KEY = socketRpcKey;

        //ABAC
        ABAC_RPC_EXCHANGE = abacRpcExchange;
        ABAC_RPC_QUEUE = abacRpcQueue;
        ABAC_RPC_KEY = abacRpcKey;
        ABAC_RPC_DEFAULT_ROLE_URL = abacRpcDefaultRoleUrl;
        ABAC_RPC_AUTHOR_URL = abacRpcAuthorUrl;
        ABAC_RPC_ADMIN_URL = abacRpcAdminUrl;
        ABAC_RPC_ATTRIBUTE_URL = abacRpcAttributeUrl;
        ABAC_RPC_GET_ROLE_URL = getRoleUrlString;
        ABAC_RPC_GET_ROLE_BY_USER_URL = getRoleUrlByUserString;
        ABAC_RPC_AUTHOR_URL = abacRpcAuthorUrl;

        DELETE_DATA_PUBLISH_EXCHANGE = deleteDataPublishExchange;
        DELETE_DATA_PUBLISH_QUEUE = deleteDataPublishQueue;
        DELETE_DATA_PUBLISH_KEY = deleteDataPublishKey;
    }
}
