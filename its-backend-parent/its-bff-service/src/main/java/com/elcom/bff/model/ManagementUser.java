package com.elcom.bff.model;

import com.elcom.bff.dto.Role;
import com.elcom.bff.dto.Unit;
import com.elcom.bff.dto.User;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@ToString
public class ManagementUser {
    private String uuid;
    private String userName;
    private String email;
    private String mobile;
    private String fullName;
    private String policeRank;
    private String position;
    private String roleName;
    private String roleCode;
    private Integer roleId;
    private String birthDay;
    private String avatar;
    private Unit unit;
    public ManagementUser(User dto, Role role) {
        this.uuid = dto.getUuid();
        this.userName = dto.getUserName();
        this.email = dto.getEmail();
        this.mobile = dto.getMobile();
        this.fullName = dto.getFullName();
        this.position = dto.getPosition();
        this.policeRank = dto.getPoliceRank();
        this.birthDay = dto.getBirthDay();
        this.avatar = dto.getAvatar();
        this.unit = dto.getUnit();
        if(role!=null){
            this.roleCode = role.getRoleCode();
            this.roleName = role.getRoleName();
            this.roleId = role.getId();
        }
    }

}
