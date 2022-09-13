/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.VmsBoardDisplayNewResponse;
import java.util.Date;

/**
 *
 * @author Admin
 */
public interface VmsBoardService {

    VmsBoardDisplayNewResponse callDisPlayNew(String vmsId, String name, String newsletterId, Object content, Object source,
            String preview, Date date, String userId);
}
