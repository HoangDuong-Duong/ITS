/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.config;

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
    @Value("${itscore.root.url}")
    public static String ITSCORE_ROOT_URL;

    @Value("${itscore.authen.url}")
    public static String ITSCORE_AUTHEN_URL;

    @Value("${itscore.userid}")
    public static String ITSCORE_USERID;

    @Value("${itscore.username}")
    public static String ITSCORE_USERNAME;

    @Value("${itscore.password}")
    public static String ITSCORE_PASSWORD;

    @Autowired
    public ApplicationConfig(@Value("${itscore.root.url}") String itscoreRootUrl,
            @Value("${itscore.authen.url}") String itscoreAuthenUrl,
            @Value("${itscore.userid}") String itscoreUserid,
            @Value("${itscore.username}") String itscoreUsername,
            @Value("${itscore.password}") String itscorePassword) {
        //DBM Account
        ITSCORE_ROOT_URL = itscoreRootUrl;
        ITSCORE_AUTHEN_URL = itscoreAuthenUrl;
        ITSCORE_USERID = itscoreUserid;
        ITSCORE_USERNAME = itscoreUsername;
        ITSCORE_PASSWORD = itscorePassword;
    }
}
