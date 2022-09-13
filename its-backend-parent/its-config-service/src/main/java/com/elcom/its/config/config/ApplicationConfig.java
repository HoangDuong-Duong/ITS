/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.config;

import com.elcom.its.config.model.dto.HlsDomainIp;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class ApplicationConfig {

    //DBM Account
    @Value("${its.root.url}")
    public static String ITS_ROOT_URL;

    @Value("${its.authen.url}")
    public static String ITS_AUTHEN_URL;

    @Value("${its.username}")
    public static String ITS_USERNAME;

    @Value("${its.password}")
    public static String ITS_PASSWORD;

    //Camera thumnail image
    @Value("${layout.root.link}")
    public static String LAYOUT_ROOT_LINK;

    //HLS Port
    @Value("${hls.port}")
    public static Integer HLS_PORT;

    @Value("${hls.type}")
    public static String HLS_TYPE;

    @Value("${hls.protocol}")
    public static String HLS_PROTOCOL;

    @Value("${hls.domain}")
    public static String HLS_DOMAIN;

    public static List<HlsDomainIp> HLS_DOMAIN_LIST;

    @Autowired
    public ApplicationConfig(@Value("${its.root.url}") String dbmRootUrl,
            @Value("${its.authen.url}") String itsAuthenUrl,
            @Value("${its.username}") String itsUsername,
            @Value("${its.password}") String itsPassword,
            @Value("${layout.root.link}") String layoutRootLink,
            @Value("${hls.port}") Integer hlsPort,
            @Value("${hls.type}") String hlsType,
            @Value("${hls.protocol}") String hlsProtocol,
            @Value("${hls.domain}") String hlsDomain) {
        //DBM Account
        ITS_ROOT_URL = dbmRootUrl;
        ITS_AUTHEN_URL = itsAuthenUrl;
        ITS_USERNAME = itsUsername;
        ITS_PASSWORD = itsPassword;

        //Camera thumnail image
        LAYOUT_ROOT_LINK = layoutRootLink;

        //HLS Port
        HLS_PORT = hlsPort;
        HLS_TYPE = hlsType;
        HLS_PROTOCOL = hlsProtocol;
        HLS_DOMAIN = hlsDomain;
        if (!StringUtil.isNullOrEmpty(HLS_DOMAIN)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                HLS_DOMAIN_LIST = Arrays.asList(mapper.readValue(HLS_DOMAIN, HlsDomainIp[].class));
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }
    }
}
