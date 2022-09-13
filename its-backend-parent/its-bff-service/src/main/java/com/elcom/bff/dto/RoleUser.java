package com.elcom.bff.dto;

import java.util.Date;

public class RoleUser {
    private static final long serialVersionUID = 1L;
    private int id;
    private String uuidUser;
    private Date createdAt;
    private String roleCode;

    public RoleUser() {
    }

    public static long getSerialVersionUID() {
        return 1L;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuidUser() {
        return this.uuidUser;
    }

    public void setUuidUser(String uuidUser) {
        this.uuidUser = uuidUser;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

}
