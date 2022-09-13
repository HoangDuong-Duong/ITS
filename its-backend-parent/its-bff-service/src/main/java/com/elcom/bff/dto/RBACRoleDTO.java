package com.elcom.bff.dto;

import com.elcom.its.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Map;

public class RBACRoleDTO {
    private Integer id;
    private String roleCode;
    private String roleName;
    private Integer isAdmin;
    @JsonIgnore
    private Integer isDelete;
    private Date createdAt;

    public RBACRoleDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("id")) {
                this.id = (Integer)map.get("id");
            }

            if (map.containsKey("roleCode")) {
                this.roleCode = (String)map.get("roleCode");
            }

            if (map.containsKey("roleName")) {
                this.roleName = (String)map.get("roleName");
            }

            if (map.containsKey("isAdmin")) {
                this.isAdmin = (Integer)map.get("isAdmin");
            }

            if (map.containsKey("isDelete")) {
                this.isDelete = (Integer)map.get("isDelete");
            }

            if (map.containsKey("createdAt") && map.get("createdAt") != null) {
                this.createdAt = DateUtil.getDateTime((String)map.get("createdAt"), "yyyy-MM-dd HH:mm:ss");
            }
        }

    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleCode() {
        return this.roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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

    public Integer getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
