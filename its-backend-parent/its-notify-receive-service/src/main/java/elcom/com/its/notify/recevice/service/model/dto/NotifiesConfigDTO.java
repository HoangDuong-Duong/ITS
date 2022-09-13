package elcom.com.its.notify.recevice.service.model.dto;

import java.io.Serializable;

public class NotifiesConfigDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String typeNotifyName;

    private String typeNotifyString;

    private Boolean noti;

    private Boolean sms;

    private Boolean zalo;

    private Boolean email;

    private String groupsRolesCode;

    private Boolean status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeNotifyName() {
        return typeNotifyName;
    }

    public void setTypeNotifyName(String typeNotifyName) {
        this.typeNotifyName = typeNotifyName;
    }

    public String getTypeNotifyString() {
        return typeNotifyString;
    }

    public void setTypeNotifyString(String typeNotifyString) {
        this.typeNotifyString = typeNotifyString;
    }

    public Boolean getNoti() {
        return noti;
    }

    public void setNoti(Boolean noti) {
        this.noti = noti;
    }

    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    public Boolean getZalo() {
        return zalo;
    }

    public void setZalo(Boolean zalo) {
        this.zalo = zalo;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public String getGroupsRolesCode() {
        return groupsRolesCode;
    }

    public void setGroupsRolesCode(String groupsRolesCode) {
        this.groupsRolesCode = groupsRolesCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
