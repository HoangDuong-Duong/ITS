/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.id.model.dto;

import com.elcom.its.id.auth.CustomUserDetails;
import com.elcom.its.id.model.Unit;
import com.elcom.its.id.service.UnitService;
import com.elcom.its.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;
/**
 *
 * @author Admin
 */
public class AuthorizationResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String uuid;
    private String userName;
    private String email;
    private String mobile;
    private String fullName;
    private String avatar;
    private Integer status;
    private String address;
    private String skype;
    private String facebook;
    private Integer signupType;
    private Date createdAt;
    private Date lastLogin;
    private Integer emailVerify;
    private Integer mobileVerify;
    private Date lastUpdate;
    private String loginIp;
    private Integer isDelete;
    private String birthDay;
    private Integer gender;
    private String fbId;
    private String ggId;
    private String appleId;
    private Integer setPassword;
    private Date profileUpdate;
    private Date avatarUpdate;
    private String policeRank;
    private String position;
    private Unit unit;

    public AuthorizationResponseDTO(){}

    public AuthorizationResponseDTO(CustomUserDetails userDetails, String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.uuid = userDetails.getUser().getUuid();
        this.userName = userDetails.getUser().getUserName();
        this.email = userDetails.getUser().getEmail();
        this.mobile = userDetails.getUser().getMobile();
        this.fullName = userDetails.getUser().getFullName();
        this.avatar = userDetails.getUser().getAvatar();
        this.status = userDetails.getUser().getStatus();
        this.address = userDetails.getUser().getAddress();
        this.skype = userDetails.getUser().getSkype();
        this.facebook = userDetails.getUser().getFacebook();
        this.signupType = userDetails.getUser().getSignupType();
        this.createdAt = userDetails.getUser().getCreatedAt();
        this.lastLogin = userDetails.getUser().getLastLogin();
        this.emailVerify = userDetails.getUser().getEmailVerify();
        this.mobileVerify = userDetails.getUser().getMobileVerify();
        this.lastUpdate = userDetails.getUser().getLastUpdate();
        this.loginIp = userDetails.getUser().getLoginIp();
        this.isDelete = userDetails.getUser().getIsDelete();
        this.birthDay = userDetails.getUser().getBirthDay();
        this.gender = userDetails.getUser().getGender();
        this.fbId = userDetails.getUser().getFbId();
        this.ggId = userDetails.getUser().getGgId();
        this.setPassword = userDetails.getUser().getSetPassword();
        this.profileUpdate = userDetails.getUser().getProfileUpdate();
        this.avatarUpdate = userDetails.getUser().getAvatarUpdate();
        this.appleId = userDetails.getUser().getAppleId();
        //Update them 3 field
        this.policeRank = userDetails.getUser().getPoliceRank();
        this.position = userDetails.getUser().getPosition();
        this.unit = userDetails.getUser().getUnit();
    }

    public AuthorizationResponseDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("uuid")) {
                this.uuid = (String) map.get("uuid");
            }
            if (map.containsKey("userName")) {
                this.userName = (String) map.get("userName");
            }
            if (map.containsKey("email")) {
                this.email = (String) map.get("email");
            }
            if (map.containsKey("mobile")) {
                this.mobile = (String) map.get("mobile");
            }
            if (map.containsKey("fullName")) {
                this.fullName = (String) map.get("fullName");
            }
            if (map.containsKey("avatar")) {
                this.avatar = (String) map.get("avatar");
            }
            if (map.containsKey("status")) {
                this.status = (Integer) map.get("status");
            }
            if (map.containsKey("address")) {
                this.address = (String) map.get("address");
            }
            if (map.containsKey("skype")) {
                this.skype = (String) map.get("skype");
            }
            if (map.containsKey("facebook")) {
                this.facebook = (String) map.get("facebook");
            }
            if (map.containsKey("signupType")) {
                this.signupType = (Integer) map.get("signupType");
            }
            if (map.containsKey("createdAt") && map.get("createdAt") != null) {
                this.createdAt = DateUtil.getDateTime((String) map.get("createdAt"), "yyyy-MM-dd HH:mm:ss");
            }
            if (map.containsKey("lastLogin") && map.get("lastLogin") != null) {
                this.lastLogin = DateUtil.getDateTime((String) map.get("lastLogin"), "yyyy-MM-dd HH:mm:ss");
            }
            if (map.containsKey("emailVerify")) {
                this.emailVerify = (Integer) map.get("emailVerify");
            }
            if (map.containsKey("mobileVerify")) {
                this.mobileVerify = (Integer) map.get("mobileVerify");
            }
            if (map.containsKey("lastUpdate") && map.get("lastUpdate") != null) {
                this.lastUpdate = DateUtil.getDateTime((String) map.get("lastUpdate"), "yyyy-MM-dd HH:mm:ss");
            }
            if (map.containsKey("loginIp")) {
                this.loginIp = (String) map.get("loginIp");
            }
            if (map.containsKey("isDelete")) {
                this.isDelete = (Integer) map.get("isDelete");
            }
            if (map.containsKey("birthDay")) {
                this.birthDay = (String) map.get("birthDay");
            }
            if (map.containsKey("gender")) {
                this.gender = (Integer) map.get("gender");
            }
            if (map.containsKey("fbId")) {
                this.fbId = (String) map.get("fbId");
            }
            if (map.containsKey("ggId")) {
                this.ggId = (String) map.get("ggId");
            }
            if (map.containsKey("setPassword")) {
                this.setPassword = (Integer) map.get("setPassword");
            }
            if (map.containsKey("profileUpdate") && map.get("profileUpdate") != null) {
                this.profileUpdate = DateUtil.getDateTime((String) map.get("profileUpdate"), "yyyy-MM-dd HH:mm:ss");
            }
            if (map.containsKey("avatarUpdate") && map.get("avatarUpdate") != null) {
                this.avatarUpdate = DateUtil.getDateTime((String) map.get("avatarUpdate"), "yyyy-MM-dd HH:mm:ss");
            }
            if (map.containsKey("appleId")) {
                this.appleId = (String) map.get("appleId");
            }
            //Update them 3 field
            if (map.containsKey("uuid")) {
                this.uuid = (String) map.get("uuid");
            }

            if (map.containsKey("policeRank")) {
                this.policeRank = (String) map.get("policeRank");
            }
            if (map.containsKey("position")) {
                this.position = (String) map.get("position");
            }
            if (map.containsKey("unit")) {
                this.unit = (Unit) map.get("unit");
            }
        }
    }

    public AuthorizationResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public Integer getSignupType() {
        return signupType;
    }

    public void setSignupType(Integer signupType) {
        this.signupType = signupType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getEmailVerify() {
        return emailVerify;
    }

    public void setEmailVerify(Integer emailVerify) {
        this.emailVerify = emailVerify;
    }

    public Integer getMobileVerify() {
        return mobileVerify;
    }

    public void setMobileVerify(Integer mobileVerify) {
        this.mobileVerify = mobileVerify;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getGgId() {
        return ggId;
    }

    public void setGgId(String ggId) {
        this.ggId = ggId;
    }

    public Integer getSetPassword() {
        return setPassword;
    }

    public void setSetPassword(Integer setPassword) {
        this.setPassword = setPassword;
    }

    public Date getProfileUpdate() {
        return profileUpdate;
    }

    public void setProfileUpdate(Date profileUpdate) {
        this.profileUpdate = profileUpdate;
    }

    public Date getAvatarUpdate() {
        return avatarUpdate;
    }

    public void setAvatarUpdate(Date avatarUpdate) {
        this.avatarUpdate = avatarUpdate;
    }

    public String getAppleId() {
        return appleId;
    }

    public void setAppleId(String appleId) {
        this.appleId = appleId;
    }

    public String getPoliceRank() {
        return policeRank;
    }

    public void setPoliceRank(String policeRank) {
        this.policeRank = policeRank;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

}