/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.config;

import elcom.com.its.notify.recevice.service.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class ITSCoreStartupConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITSCoreStartupConfig.class);

    @Autowired
    private TokenService tokenService;

    @EventListener(classes = ApplicationStartedEvent.class)
    public void getTokenOnAppStartup() {
        //Remove cache
        tokenService.removeAccessToken();
        //Get new token and cache
        String itsCoreToken = tokenService.getAccessToken();
        LOGGER.info("ITS Core Token on startup: {}", itsCoreToken);
    }
}
