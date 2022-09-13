package com.elcom.bff.dto;

import com.elcom.its.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;
    private Integer emailVerify;
    private Integer mobileVerify;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdate;
    private String loginIp;
    private Integer isDelete;
    private String birthDay;
    private Integer gender;
    private String fbId;
    private String ggId;
    private String appleId;
    private Integer setPassword;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date profileUpdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date avatarUpdate;
    private String policeRank;
    private String position;
    private Unit unit;


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
                this.createdAt = DateUtil.getDateTime((String) map.get("createdAt"), "yyyy-MM-dd'T'HH:mm:ss");
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

            if (map.containsKey("policeRank")) {
                this.policeRank = (String) map.get("policeRank");
            }

            if (map.containsKey("position")) {
                this.position = (String) map.get("position");
            }

        }

    }

}
