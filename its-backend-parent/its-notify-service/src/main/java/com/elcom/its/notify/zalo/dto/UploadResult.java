/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.zalo.dto;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class UploadResult implements Serializable {

    private String attachment_id;

    public String getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_id(String attachment_id) {
        this.attachment_id = attachment_id;
    }
}
