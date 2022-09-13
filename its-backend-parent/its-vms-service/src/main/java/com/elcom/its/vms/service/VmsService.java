/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vms.service;

import com.elcom.its.vms.dto.Response;

/**
 *
 * @author Admin
 */
public interface VmsService {
    public Response getLiveImage(String urlParam);
    public Response cutVideo(String startTime, String endTime,String cameraId);

}
