/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.messaging.rabbitmq;

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

    @Value("${user.rpc.unit.detail.url}")
    public static String USER_RPC_UNIT_DETAIL_URL;

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

    @Value("${its.shift.export.worker.queue}")
    public static String SHIFT_EXPORT_WORKER_QUEUE;
    @Value("${its.upload.url}")
    public static String UPLOAD_URL;

    @Value("${notify.rpc.queue}")
    public static String NOTIFY_RPC_QUEUE;

    @Value("${notify.rpc.exchange}")
    public static String NOTIFY_RPC_EXCHANGE;

    @Value("${notify.rpc.key}")
    public static String NOTIFY_RPC_KEY;

    @Value("${its.notify.rpc.internal.send.url}")
    public static String its_NOTIFY_RPC_INTERNAL_SEND_URL;

//    MANAGEMENT
    @Value("${management.rpc.exchange}")
    public static String MANAGEMENT_RPC_EXCHANGE;

    @Value("${management.rpc.queue}")
    public static String MANAGEMENT_RPC_QUEUE;

    @Value("${management.rpc.key}")
    public static String MANAGEMENT_RPC_KEY;

    @Value("${management.job.report.status.url}")
    public static String MANAGEMENT_JOB_REPORT_BY_STATUS_URL;

    @Value("${management.scheduled.event.report.url}")
    public static String MANAGEMENT_SCHEDULED_EVENT_REPORT_URL;

    @Autowired
    public RabbitMQProperties(@Value("${user.rpc.exchange}") String userRpcExchange,
            @Value("${user.rpc.queue}") String userRpcQueue,
            @Value("${user.rpc.key}") String userRpcKey,
            @Value("${user.rpc.authen.url}") String userRpcAuthenUrl,
            @Value("${user.rpc.uuidLst.url}") String userRpcUuidLstUrl,
            @Value("${user.rpc.internal.list.url}") String userRpcInternalLstUrl,
            @Value("${user.rpc.unit.detail.url}") String detailUnitUrl,
            @Value("${abac.rpc.exchange}") String abacRpcExchange,
            @Value("${abac.rpc.queue}") String abacRpcQueue,
            @Value("${abac.rpc.key}") String abacRpcKey,
            @Value("${abac.rpc.default.role.url}") String abacRpcDefaultRoleUrl,
            @Value("${abac.rpc.author.url}") String abacRpcAuthorUrl,
            @Value("${abac.rpc.admin.url}") String abacRpcAdminUrl,
            @Value("${abac.rpc.attribute.url}") String abacRpcAttributeUrl,
            @Value("${abac.rpc.getRole.url}") String getRoleUrlString,
            @Value("${abac.rpc.getRoleByUserId.url}") String getRoleUrlByUserString,
            @Value("${its.shift.export.worker.queue}") String shiftExportWorkerQueue,
            @Value("${its.upload.url}") String uploadUrl,
            @Value("${notify.rpc.queue}") String notifyRpcQueue,
            @Value("${notify.rpc.exchange}") String notifyRpcExchange,
            @Value("${notify.rpc.key}") String notifyRpcKey,
            @Value("${its.notify.rpc.internal.send.url}") String itsNotifyRpcInternalListUrl,
            @Value("${management.rpc.exchange}") String managementExchange,
            @Value("${management.rpc.queue}") String managementQueue,
            @Value("${management.rpc.key}") String managementKey,
            @Value("${management.job.report.status.url}") String managementJobReportByStatusUrl,
            @Value("${management.scheduled.event.report.url}") String managementScheduledEventUrl
    ) {
        //User
        USER_RPC_EXCHANGE = userRpcExchange;
        USER_RPC_QUEUE = userRpcQueue;
        USER_RPC_KEY = userRpcKey;
        USER_RPC_AUTHEN_URL = userRpcAuthenUrl;
        USER_RPC_UUIDLIST_URL = userRpcUuidLstUrl;
        USER_RPC_INTERNAL_LIST_URL = userRpcInternalLstUrl;
        USER_RPC_UNIT_DETAIL_URL = detailUnitUrl;

        ABAC_RPC_EXCHANGE = abacRpcExchange;
        ABAC_RPC_QUEUE = abacRpcQueue;
        ABAC_RPC_KEY = abacRpcKey;
        ABAC_RPC_DEFAULT_ROLE_URL = abacRpcDefaultRoleUrl;
        ABAC_RPC_AUTHOR_URL = abacRpcAuthorUrl;
        ABAC_RPC_ADMIN_URL = abacRpcAdminUrl;
        ABAC_RPC_ATTRIBUTE_URL = abacRpcAttributeUrl;
        ABAC_RPC_GET_ROLE_URL = getRoleUrlString;
        ABAC_RPC_GET_ROLE_BY_USER_URL = getRoleUrlByUserString;
        SHIFT_EXPORT_WORKER_QUEUE = shiftExportWorkerQueue;
        UPLOAD_URL = uploadUrl;

        NOTIFY_RPC_QUEUE = notifyRpcQueue;
        NOTIFY_RPC_EXCHANGE = notifyRpcExchange;
        NOTIFY_RPC_KEY = notifyRpcKey;
        its_NOTIFY_RPC_INTERNAL_SEND_URL = itsNotifyRpcInternalListUrl;

        MANAGEMENT_RPC_EXCHANGE = managementExchange;
        MANAGEMENT_RPC_KEY = managementKey;
        MANAGEMENT_RPC_QUEUE = managementQueue;
        MANAGEMENT_JOB_REPORT_BY_STATUS_URL = managementJobReportByStatusUrl;
        MANAGEMENT_SCHEDULED_EVENT_REPORT_URL = managementScheduledEventUrl;

    }
}
