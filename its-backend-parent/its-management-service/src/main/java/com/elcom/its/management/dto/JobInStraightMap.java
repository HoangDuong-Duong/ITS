/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.elcom.its.management.enums.JobStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Admin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobInStraightMap {

    private String jobId;
    private String jobType;
    private Date startTime;
    private Date endTime;
    private SiteDTO startSite;
    private SiteDTO endSite;
    private String eventId;
    private JobStatus status;
    private String plateId;
    private String plateName;
    private String directionCode;
    private int limitSpeed;

}
