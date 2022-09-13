package com.elcom.its.report.messaging.rabbitmq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ducduongn
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
    @Value("${report.rpc.queue}")
    public static String REPORT_RPC_QUEUE;

    @Value("${report.rpc.exchange}")
    public static String REPORT_RPC_EXCHANGE;

    @Value("${report.rpc.key}")
    public static String REPORT_RPC_KEY;

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

    //Notify
    @Value("${notify.rpc.queue}")
    public static String NOTIFY_RPC_QUEUE;

    @Value("${notify.rpc.exchange}")
    public static String NOTIFY_RPC_EXCHANGE;

    @Value("${notify.rpc.key}")
    public static String NOTIFY_RPC_KEY;

    @Value("${its.notify.rpc.url}")
    public static String ITS_NOTIFY_RPC_URL;

    //Upload
    @Value("${upload.service.url}")
    public static String UPLOAD_URL;

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
            @Value("${report.rpc.queue}") String reportRpcQueue,
            @Value("${report.rpc.exchange}") String reportRpcExchange,
            @Value("${report.rpc.key}") String reportRpcKey,
            @Value("${socket.rpc.queue.name}") String socketRpcQueue,
            @Value("${socket.rpc.exchange.name}") String socketRpcExchange,
            @Value("${socket.rpc.routing.key}") String socketRpcKey,
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
            @Value("${notify.rpc.exchange}") String notifyRpcExchange,
            @Value("${notify.rpc.queue}") String notifyRpcQueue,
            @Value("${notify.rpc.key}") String notifyRpcKey,
            @Value("${its.notify.rpc.url}") String notifyRpcUrl,
            @Value("${upload.service.url}") String uploadUrl) {

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
        REPORT_RPC_EXCHANGE = reportRpcExchange;
        REPORT_RPC_QUEUE = reportRpcQueue;
        REPORT_RPC_KEY = reportRpcKey;

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
        
        //Notify
        NOTIFY_RPC_EXCHANGE = notifyRpcExchange;
        NOTIFY_RPC_QUEUE = notifyRpcQueue;
        NOTIFY_RPC_KEY = notifyRpcKey;
        ITS_NOTIFY_RPC_URL = notifyRpcUrl;
        
        //Upload
        UPLOAD_URL = uploadUrl;
    }
}
