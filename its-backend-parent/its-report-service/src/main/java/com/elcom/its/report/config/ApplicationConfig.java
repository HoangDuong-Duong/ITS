package com.elcom.its.report.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author ducduongn
 */
@Configuration
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

    @Autowired
    public ApplicationConfig(@Value("${its.root.url}") String dbmRootUrl,
                             @Value("${its.authen.url}") String itsAuthenUrl,
                             @Value("${its.username}") String itsUsername,
                             @Value("${its.password}") String itsPassword,
                             @Value("${layout.root.link}") String layoutRootLink) {
        //DBM Account
        ITS_ROOT_URL = dbmRootUrl;
        ITS_AUTHEN_URL = itsAuthenUrl;
        ITS_USERNAME = itsUsername;
        ITS_PASSWORD = itsPassword;

        //Camera thumnail image
        LAYOUT_ROOT_LINK = layoutRootLink;
    }
}
