/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.id.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class ApplicationConfig {

    //Link quen mat khau
    @Value("${frontend.forgotpass.url}")
    public static String FRONTEND_FORGOTPASS_URL;

    //Thoi han link
    @Value("${forgotpass.expired.time}")
    public static int FORGOTPASS_EXPIRED_TIME;

    //DBM Account
    @Value("${its.root.url}")
    public static String ITS_ROOT_URL;

    @Value("${its.authen.url}")
    public static String ITS_AUTHEN_URL;
    
    @Value("${its.username}")
    public static String ITS_USERNAME;

    @Value("${its.password}")
    public static String ITS_PASSWORD;

    @Autowired
    public ApplicationConfig(@Value("${its.root.url}") String dbmRootUrl,
             @Value("${frontend.forgotpass.url}") String forgotPassUrl,
             @Value("${forgotpass.expired.time}") int forgotPassExpiredTime,
             @Value("${its.authen.url}") String dbmAuthenUrl,
             @Value("${its.username}") String dbmUsername,
             @Value("${its.password}") String dbmPassword) {
        //Frontend
        FRONTEND_FORGOTPASS_URL = forgotPassUrl;
        FORGOTPASS_EXPIRED_TIME = forgotPassExpiredTime;
        
        //DBM Account
        ITS_ROOT_URL = dbmRootUrl;
        ITS_AUTHEN_URL = dbmAuthenUrl;
        ITS_USERNAME = dbmUsername;
        ITS_PASSWORD = dbmPassword;
    }
}
