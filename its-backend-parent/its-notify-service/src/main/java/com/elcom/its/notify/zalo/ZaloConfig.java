/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.zalo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Admin
 */
@Configuration
public class ZaloConfig {

    @Value("${zalo.sendmsg.url}")
    private String sendMsgUrl;

    @Value("${zalo.receiver.url}")
    private String receiverUrl;

    @Value("${zalo.upload.url}")
    private String uploadUrl;

    @Value("${zalo.access_token}")
    private String accessToken;

    @Value("${zalo.body.format}")
    private String message;

    @Bean
    public ZaloClient zaloClient() {
        return new ZaloClient(sendMsgUrl, receiverUrl, uploadUrl, accessToken, message);
    }
}
