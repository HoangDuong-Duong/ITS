/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.elcom.its.management.model.Job;
import lombok.Data;

/**
 *
 * @author Admin
 */
@Data
public class JobInMapNotification {

    private Point startPoint;
    private Point endPoint;
    private Job job;
}
