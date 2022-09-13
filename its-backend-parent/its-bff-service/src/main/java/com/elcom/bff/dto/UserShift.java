/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Admin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserShift implements Serializable {

    private String uuid;

    private String userName;

    private String email;

    private String mobile;

    private String fullName;

    private String password;

    private Integer status;

    private Integer emailVerify;

    private Integer mobileVerify;

    private String skype;

    private String facebook;

    private String avatar;

    private String address;

    private String birthDay;

    private Integer gender;

    private Timestamp createdAt;

    private Timestamp lastUpdate;

    private String loginIp;

    private Timestamp lastLogin;

    private int signupType;

    private String fbId;

    private String ggId;

    private String appleId;

    private Integer isDelete;

    private Integer setPassword;

    private Timestamp profileUpdate;

    private Timestamp avatarUpdate;

    private String otp;

    private Timestamp otpTime;

    private String otpMobile;

    private String otpPassword;

    private Timestamp otpPasswordTime;

    private String groupsUuid;

    private String policeRank;

    private String position;

    public static final Integer STATUS_ACTIVE = 1;

    public static final Integer STATUS_LOCK = -1;

    private String matchingPassword;

    private RBACRoleDTO roleCode;

    private Unit unit;
    
    private Integer inShift;
    
    public UserShift() {
    }

    void preInsert() {
        if (this.getCreatedAt() == null) {
            this.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
    }

    public UserShift(String uuid) {
        this.uuid = uuid;
    }

    public UserShift(String uuid, int signupType) {
        this.uuid = uuid;
        this.signupType = signupType;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getEmailVerify() {
        return this.emailVerify;
    }

    public void setEmailVerify(Integer emailVerify) {
        this.emailVerify = emailVerify;
    }

    public Integer getMobileVerify() {
        return this.mobileVerify;
    }

    public void setMobileVerify(Integer mobileVerify) {
        this.mobileVerify = mobileVerify;
    }

    public String getSkype() {
        return this.skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDay() {
        return this.birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getGender() {
        return this.gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getSignupType() {
        return this.signupType;
    }

    public void setSignupType(int signupType) {
        this.signupType = signupType;
    }

    public String getFbId() {
        return this.fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getGgId() {
        return this.ggId;
    }

    public void setGgId(String ggId) {
        this.ggId = ggId;
    }

    public Integer getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getSetPassword() {
        return this.setPassword;
    }

    public void setSetPassword(Integer setPassword) {
        this.setPassword = setPassword;
    }

    public Timestamp getProfileUpdate() {
        return this.profileUpdate;
    }

    public void setProfileUpdate(Timestamp profileUpdate) {
        this.profileUpdate = profileUpdate;
    }

    public Timestamp getAvatarUpdate() {
        return this.avatarUpdate;
    }

    public void setAvatarUpdate(Timestamp avatarUpdate) {
        this.avatarUpdate = avatarUpdate;
    }

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Timestamp getOtpTime() {
        return this.otpTime;
    }

    public void setOtpTime(Timestamp otpTime) {
        this.otpTime = otpTime;
    }

    public String getOtpMobile() {
        return this.otpMobile;
    }

    public void setOtpMobile(String otpMobile) {
        this.otpMobile = otpMobile;
    }

    public String getOtpPassword() {
        return this.otpPassword;
    }

    public void setOtpPassword(String otpPassword) {
        this.otpPassword = otpPassword;
    }

    public Timestamp getOtpPasswordTime() {
        return this.otpPasswordTime;
    }

    public void setOtpPasswordTime(Timestamp otpPasswordTime) {
        this.otpPasswordTime = otpPasswordTime;
    }

    public String getGroupsUuid() {
        return this.groupsUuid;
    }

    public void setGroupsUuid(String groupsUuid) {
        this.groupsUuid = groupsUuid;
    }

    public String getPoliceRank() {
        return this.policeRank;
    }

    public void setPoliceRank(String policeRank) {
        this.policeRank = policeRank;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public RBACRoleDTO getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(RBACRoleDTO roleCode) {
        this.roleCode = roleCode;
    }

    public String getMatchingPassword() {
        return this.matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash = hash + (this.uuid != null ? this.uuid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        } else {
            UserShift other = (UserShift)object;
            return (this.uuid != null || other.uuid == null) && (this.uuid == null || this.uuid.equals(other.uuid));
        }
    }

    @Override
    public String toString() {
        return "com.elcom.bff.dto.UserShift[ uuid=" + this.uuid + " ]";
    }

    public String getAppleId() {
        return this.appleId;
    }

    public void setAppleId(String appleId) {
        this.appleId = appleId;
    }

    public Integer getInShift() {
        return inShift;
    }

    public void setInShift(Integer inShift) {
        this.inShift = inShift;
    }

    public String toJsonString() {
        return (new Gson()).toJson(this);
    }
}
