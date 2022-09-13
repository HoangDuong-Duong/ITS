/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Admin
 */
@Entity
@Table(name = "login_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LoginHistory.findAll", query = "SELECT l FROM LoginHistory l")})
public class LoginHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LoginHistoryPK loginHistoryPK;
    @Size(max = 36)
    @Column(name = "user_id")
    private String userId;
    @Size(max = 255)
    @Column(name = "username")
    private String username;
    @Size(max = 255)
    @Column(name = "user_full_name")
    private String userFullName;
    @Column(name = "last_request")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRequest;

    public LoginHistory() {
    }

    public LoginHistory(LoginHistoryPK loginTimePK) {
        this.loginHistoryPK = loginTimePK;
    }

    public LoginHistory(String id, Date loginTime) {
        this.loginHistoryPK = new LoginHistoryPK(id, loginTime);
    }

    public LoginHistoryPK getLoginHistoryPK() {
        return loginHistoryPK;
    }

    public void setLoginHistoryPK(LoginHistoryPK loginHistoryPK) {
        this.loginHistoryPK = loginHistoryPK;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public Date getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Date lastRequest) {
        this.lastRequest = lastRequest;
    }

    @JsonIgnore
    public String getHistoryView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(loginHistoryPK.getLoginTime()) + " - " + dateFormat.format(lastRequest);
    }

    @JsonIgnore
    public boolean isExpired() {
        Date beforeTenMinutes = new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(2));
        if (lastRequest.getTime() < beforeTenMinutes.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    @JsonIgnore
    public long getTotalTimeLogin() {
        long diffInMillies = lastRequest.getTime() - loginHistoryPK.getLoginTime().getTime();
        return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (loginHistoryPK != null ? loginHistoryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoginHistory)) {
            return false;
        }
        LoginHistory other = (LoginHistory) object;
        if ((this.loginHistoryPK == null && other.loginHistoryPK != null) || (this.loginHistoryPK != null && !this.loginHistoryPK.equals(other.loginHistoryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.its.shift.model.LoginTime[ loginTimePK=" + loginHistoryPK + " ]";
    }

}
