/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vmsboard.service;

/**
 *
 * @author Admin
 */
public interface TokenService {

    String getAccessToken();
    
    boolean removeAccessToken();
}
