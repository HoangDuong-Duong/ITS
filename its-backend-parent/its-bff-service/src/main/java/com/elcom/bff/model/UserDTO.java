package com.elcom.bff.model;
import com.elcom.bff.dto.Role;
import com.elcom.bff.dto.Unit;
import com.elcom.bff.dto.User;

public class UserDTO {
    private String uuid;
    private String userName;
    private String email;
    private String mobile;
    private String fullName;
    private Unit unit;
    private String policeRank;
    private String position;
    private String roleName;
    private String roleCode;
    private Integer roleId;
    private String birthDay;


    public UserDTO(User dto, Role role) {
        this.uuid = dto.getUuid();
        this.userName = dto.getUserName();
        this.email = dto.getEmail();
        this.mobile = dto.getMobile();
        this.fullName = dto.getFullName();
        this.unit = dto.getUnit();
        this.position = dto.getPosition();
        this.policeRank = dto.getPoliceRank();
        this.birthDay = dto.getBirthDay();
        if(role!=null){
            this.roleCode = role.getRoleCode();
            this.roleName = role.getRoleName();
            this.roleId = role.getId();
        }
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

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
}
