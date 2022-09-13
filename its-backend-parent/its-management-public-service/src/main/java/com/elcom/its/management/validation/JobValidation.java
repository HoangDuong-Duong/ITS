/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.validation;

import com.elcom.its.management.model.Job;
import com.elcom.its.utils.StringUtil;

/**
 *
 * @author Admin
 */
public class JobValidation extends AbstractValidation {

    public String validateJob(String action, Job job) {
        if (action.equalsIgnoreCase("CREATE")) {
            if (StringUtil.isNullOrEmpty(job.getGroupId())) {
                getMessageDes().add("Đội không được để trống");
            }

            if (StringUtil.isNullOrEmpty(job.getJobType())) {
                getMessageDes().add("Loại công việc được để trống");
            }
        }
        /**/
        return !isValid() ? this.buildValidationMessage() : null;
    }
}
