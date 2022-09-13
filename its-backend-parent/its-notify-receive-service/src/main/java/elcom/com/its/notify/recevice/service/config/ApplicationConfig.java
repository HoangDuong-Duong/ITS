/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.config;

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

    @Value("${redirect.vehicle.track.status.url}")
    public static String URL_VEHICLE_TRACK;

    @Value("${redirect.violation.track.status.url}")
        public static String URL_VIOLATION_TRACK;

    @Value("${redirect.crowd.flow.status.url}")
    public static String URL_CROWD_FLOW;

    @Autowired
    public ApplicationConfig(@Value("${itscore.root.url}") String itscoreRootUrl,
            @Value("${itscore.authen.url}") String itscoreAuthenUrl,
            @Value("${itscore.userid}") String itscoreUserid,
            @Value("${itscore.username}") String itscoreUsername,
             @Value("${redirect.vehicle.track.status.url}") String urlVehicleTrack,
             @Value("${redirect.violation.track.status.url}") String urlViolation,
             @Value("${redirect.crowd.flow.status.url}") String urlCrowd,
            @Value("${itscore.password}") String itscorePassword) {
        //DBM Account
        ITSCORE_ROOT_URL = itscoreRootUrl;
        ITSCORE_AUTHEN_URL = itscoreAuthenUrl;
        ITSCORE_USERID = itscoreUserid;
        ITSCORE_USERNAME = itscoreUsername;
        ITSCORE_PASSWORD = itscorePassword;
        URL_CROWD_FLOW = urlCrowd;
        URL_VEHICLE_TRACK = urlVehicleTrack;
        URL_VIOLATION_TRACK =  urlViolation;

    }
}
