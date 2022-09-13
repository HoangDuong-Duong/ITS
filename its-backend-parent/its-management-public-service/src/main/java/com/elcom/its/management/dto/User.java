/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * @author Admin
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String uuid;
    private String userName;
    private String email;
    private String mobile;
    private String fullName;
    private int status;
    private int emailVerify;
    private int mobileVerify;
    private String skype;
    private String facebook;
    private String avatar;
    private String address;
    private String birthDay;
    private int gender;
    private String loginIp;
    private int signupType;
    private String fbId;
    private String ggId;
    private String appleId;
    private int isDelete;
    private int setPassword;
    private String otp;
    private String otpMobile;
    private String otpPassword;
    private String policeRank;
    private String position;
    private Unit unit;
}
