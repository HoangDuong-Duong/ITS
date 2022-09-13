/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.messaging.rabbitmq;

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

    @Value("${mobile-app.notify.queue}")
    public static String MOBILE_APP_NOTIFY_QUEUE;

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
    
    @Value("${user.rpc.internal.url}")
    public static String USER_RPC_INTERNAL_URL;

    @Value("${group.rpc.internal.list.url}")
    public static String GROUP_RPC_INTERNAL_LIST_URL;

    @Value("${unit.rpc.get.url}")
    public static String GROUP_GET_URL;

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

    @Value("${management.rpc.queue}")
    public static String MANAGEMENT_RPC_QUEUE;

    @Value("${management.rpc.exchange}")
    public static String MANAGEMENT_RPC_EXCHANGE;

    @Value("${management.rpc.key}")
    public static String MANAGEMENT_RPC_KEY;

    @Value("${user.rpc.get.user}")
    public static String USER_RPC_GET_USER;

    //Config
    @Value("${systemconfig.rpc.queue}")
    public static String SYSTEMCONFIG_RPC_QUEUE;

    @Value("${systemconfig.rpc.exchange}")
    public static String SYSTEMCONFIG_RPC_EXCHANGE;

    @Value("${systemconfig.rpc.key}")
    public static String SYSTEMCONFIG_RPC_KEY;

    @Value("${systemconfig.rpc.sites.list}")
    public static String SYSTEMCONFIG_RPC_SITES_LIST;

    @Value("${systemconfig.rpc.camera.list}")
    public static String SYSTEMCONFIG_RPC_CAMERA_LIST;

    @Value("${notify.rpc.queue}")
    public static String NOTIFY_RPC_QUEUE;

    @Value("${notify.rpc.exchange}")
    public static String NOTIFY_RPC_EXCHANGE;

    @Value("${notify.rpc.key}")
    public static String NOTIFY_RPC_KEY;

    @Value("${its.notify.rpc.internal.list.url}")
    public static String its_NOTIFY_RPC_INTERNAL_LIST_URL;

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

    @Value("${abac.rpc.admin.url}")
    public static String ABAC_RPC_ADMIN_URL;

    @Value("${abac.rpc.attribute.url}")
    public static String ABAC_RPC_ATTRIBUTE_URL;

    @Value("${upload.service.url}")
    public static String UPLOAD_URL;

    @Value("${notify.job.worker.queue}")
    public static String NOTIFY_JOB_WOKER_QUEUE;

    @Value("${notify.scheduled.event.worker.queue}")
    public static String NOTIFY_SCHEDULED_EVENT_WOKER_QUEUE;

    @Value("${notify.event.worker.queue}")
    public static String NOTIFY_EVENT_WORKER_QUEUE;

    @Value("${notify.finish.event.worker.queue}")
    public static String NOTIFY_FINISH_EVENT_WOKER_QUEUE;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
            @Value("${user.rpc.queue}") String userRpcQueue,
            @Value("${user.rpc.key}") String userRpcKey,
            @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
            @Value("${user.rpc.uuidLst.url}") String userRpcUuidLstUrl,
            @Value("${user.rpc.internal.list.url}") String userRpcInternalLstUrl,
            @Value("${user.rpc.internal.url}")String userRpcInternal,
            @Value("${group.rpc.internal.list.url}") String groupRpcInternalLstUrl,
            @Value("${rbac.rpc.exchange}") String rbacRpcExchange,
            @Value("${rbac.rpc.queue}") String rbacRpcQueue,
            @Value("${rbac.rpc.key}") String rbacRpcKey,
            @Value("${rbac.rpc.default.role.url}") String rbacRpcDefaultRoleUrl,
            @Value("${rbac.rpc.author.url}") String rbacRpcAuthorUrl,
            @Value("${rbac.rpc.admin.url}") String rbacRpcAdminUrl,
            @Value("${management.rpc.queue}") String managementRpcQueue,
            @Value("${management.rpc.exchange}") String managementRpcExchange,
            @Value("${management.rpc.key}") String managementRpcKey,
            @Value("${systemconfig.rpc.queue}") String systemconfigRpcQueue,
            @Value("${systemconfig.rpc.exchange}") String systemconfigRpcExchange,
            @Value("${systemconfig.rpc.key}") String systemconfigRpcKey,
            @Value("${systemconfig.rpc.sites.list}") String systemconfigRpcSitesList,
            @Value("${systemconfig.rpc.camera.list}") String systemconfigRpcCameraList,
            @Value("${mobile-app.notify.queue}") String mobileAppNotifyQueue,
            @Value("${notify.rpc.queue}") String notifyRpcQueue,
            @Value("${notify.rpc.exchange}") String notifyRpcExchange,
            @Value("${notify.rpc.key}") String notifyRpcKey,
            @Value("${its.notify.rpc.internal.list.url}") String itsNotifyRpcInternalListUrl,
            @Value("${abac.rpc.exchange}") String abacRpcExchange,
            @Value("${abac.rpc.queue}") String abacRpcQueue,
            @Value("${abac.rpc.key}") String abacRpcKey,
            @Value("${abac.rpc.default.role.url}") String abacRpcDefaultRoleUrl,
            @Value("${abac.rpc.author.url}") String abacRpcAuthorUrl,
            @Value("${abac.rpc.admin.url}") String abacRpcAdminUrl,
            @Value("${abac.rpc.attribute.url}") String abacRpcAttributeUrl,
            @Value("${upload.service.url}") String uploadUrl,
            @Value("${notify.job.worker.queue}") String notifyJobWorkerQueue,
            @Value("${notify.event.worker.queue}") String notifyEventWorkerQueue,
            @Value("${notify.finish.event.worker.queue}") String notifyFinishEventWorkerQueue,
            @Value("${notify.scheduled.event.worker.queue}") String notifyScheduledEventQueue,
            @Value("${unit.rpc.get.url}") String getUnitUrl
    ) {
        //User
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;
        USER_RPC_UUIDLIST_URL = userRpcUuidLstUrl;
        USER_RPC_INTERNAL_LIST_URL = userRpcInternalLstUrl;
        USER_RPC_INTERNAL_URL = userRpcInternal;
        GROUP_RPC_INTERNAL_LIST_URL = groupRpcInternalLstUrl;
        GROUP_GET_URL = getUnitUrl;

        //RBAC
        RBAC_RPC_EXCHANGE = rbacRpcExchange;
        RBAC_RPC_QUEUE = rbacRpcQueue;
        RBAC_RPC_KEY = rbacRpcKey;
        RBAC_RPC_DEFAULT_ROLE_URL = rbacRpcDefaultRoleUrl;
        RBAC_RPC_AUTHOR_URL = rbacRpcAuthorUrl;
        RBAC_RPC_ADMIN_URL = rbacRpcAdminUrl;
        MANAGEMENT_RPC_QUEUE = managementRpcQueue;
        MANAGEMENT_RPC_EXCHANGE = managementRpcExchange;
        MANAGEMENT_RPC_KEY = managementRpcKey;
        MOBILE_APP_NOTIFY_QUEUE = mobileAppNotifyQueue;

        //CONFIG
        SYSTEMCONFIG_RPC_EXCHANGE = systemconfigRpcExchange;
        SYSTEMCONFIG_RPC_QUEUE = systemconfigRpcQueue;
        SYSTEMCONFIG_RPC_KEY = systemconfigRpcKey;
        SYSTEMCONFIG_RPC_SITES_LIST = systemconfigRpcSitesList;
        SYSTEMCONFIG_RPC_CAMERA_LIST = systemconfigRpcCameraList;

        NOTIFY_RPC_QUEUE = notifyRpcQueue;
        NOTIFY_RPC_EXCHANGE = notifyRpcExchange;
        NOTIFY_RPC_KEY = notifyRpcKey;
        its_NOTIFY_RPC_INTERNAL_LIST_URL = itsNotifyRpcInternalListUrl;
        ABAC_RPC_EXCHANGE = abacRpcExchange;
        ABAC_RPC_QUEUE = abacRpcQueue;
        ABAC_RPC_KEY = abacRpcKey;
        ABAC_RPC_DEFAULT_ROLE_URL = abacRpcDefaultRoleUrl;
        ABAC_RPC_AUTHOR_URL = abacRpcAuthorUrl;
        ABAC_RPC_ADMIN_URL = abacRpcAdminUrl;
        ABAC_RPC_ATTRIBUTE_URL = abacRpcAttributeUrl;
        UPLOAD_URL = uploadUrl;

        NOTIFY_JOB_WOKER_QUEUE = notifyJobWorkerQueue;
        NOTIFY_FINISH_EVENT_WOKER_QUEUE = notifyFinishEventWorkerQueue;
        NOTIFY_EVENT_WORKER_QUEUE = notifyEventWorkerQueue;
        NOTIFY_SCHEDULED_EVENT_WOKER_QUEUE = notifyScheduledEventQueue;
    }
}
