/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.zalo.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Admin
 */
public class FollowerDataResult implements Serializable {

    private Integer total;
    private List<UserId> followers;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<UserId> getFollowers() {
        return followers;
    }

    public void setFollowers(List<UserId> followers) {
        this.followers = followers;
    }
}
