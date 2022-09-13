package com.elcom.bff.dto;

import java.util.Date;

public class Role {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String roleCode;
    private String roleName;
    private String description;
    private Integer isAdmin;
    private Integer parentId;
    private Date createdAt;

    public Role() {
    }

    public Role(String roleName, String description, Integer isAdmin, String roleCode) {
        this.roleName = roleName;
        this.description = description;
        this.isAdmin = isAdmin;
        this.roleCode = roleCode;
    }

    public Role(Integer id, String roleName, String description, Integer isAdmin, String roleCode) {
        this.id = id;
        this.roleName = roleName;
        this.description = description;
        this.isAdmin = isAdmin;
        this.roleCode = roleCode;
    }

    public Role(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
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

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public int hashCode() {
        int hash = 0;
        hash = hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Role)) {
            return false;
        } else {
            Role other = (Role)object;
            return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
        }
    }

    public String toString() {
        return "com.elcom.com.elcom.abac.model.Role[ id=" + this.id + " ]";
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
