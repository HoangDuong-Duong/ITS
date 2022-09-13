/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.model.EventFile;

/**
 *
 * @author Admin
 */
public interface EventFileService {

    EventFile createFileEventHistory(String name, String position, String types, String status, String sort, Integer page, Integer size, String string, Boolean isAdmin, String uuid) throws Exception;
}
