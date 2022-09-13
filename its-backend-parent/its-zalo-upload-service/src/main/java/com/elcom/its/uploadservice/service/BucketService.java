/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.uploadservice.service;

import io.github.bucket4j.Bucket;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Admin
 */
public interface BucketService {

    public Bucket getBucket(HttpServletRequest request);

    public String getIpAddress(HttpServletRequest request);
}
