/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.id.config;

import com.elcom.its.id.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ITSCoreStartupConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITSCoreStartupConfig.class);

    @Autowired
    private TokenService tokenService;

    @EventListener(classes = ApplicationStartedEvent.class )
    public void getTokenOnAppStartup(ApplicationStartedEvent event) {
        //Remove cache
        tokenService.removeAccessToken();
        //Get new token and cache
        String dbmToken = tokenService.getAccessToken();
        LOGGER.info("ITS Core Token on startup: {}", dbmToken);
    }
}
