/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.zalo;

import com.elcom.its.notify.tools.ImageUtil;
import com.elcom.its.notify.zalo.dto.ZaloResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Admin
 */
public class ZaloClient {

    //Logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ZaloClient.class);

    //Zalo parameter connection
    private String sendMsgUrl;
    private String receiverUrl;
    private String uploadUrl;
    private String accessToken;
    private String messagePattern;

    //Constructor
    public ZaloClient(String sendMsgUrl, String receiverUrl, String uploadUrl, String accessToken,
            String messagePattern) {
        this.sendMsgUrl = sendMsgUrl;
        this.receiverUrl = receiverUrl;
        this.uploadUrl = uploadUrl;
        this.accessToken = accessToken;
        this.messagePattern = messagePattern;
    }

    //Get list zalo follower with connect parameter
    public ZaloResponse getAllFollowers() {
        RestTemplate restTemplate = new RestTemplate();
        String urlRequest = receiverUrl.replace("{access_token}", accessToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlRequest);
        URI uri = builder.build().encode().toUri();
        String result = restTemplate.getForObject(uri, String.class);

        if (result != null) {
            LOGGER.info("result: " + result);
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.readValue(result, ZaloResponse.class);
            } catch (JsonProcessingException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    //Send messagePattern to list follower
    public ZaloResponse sendZaloMessage(String userId, String message, String attachmentId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        //Headers
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Body
        String escapeMessage = StringEscapeUtils.escapeJava(message);
        String messageParternFix = messagePattern;
        int firstIndex = messagePattern.indexOf(",\"buttons\"");
        int lastIndex = firstIndex > 0 ? messagePattern.indexOf("]", firstIndex) : 0;
        if(firstIndex > 0 && lastIndex > 0) {
            messageParternFix = messagePattern.substring(0, firstIndex) + messagePattern.substring(lastIndex + 1);
        }
        String bodyRequest = messageParternFix.replace("USER_ID", userId).replace("MESSAGE", escapeMessage)
                .replace("ATTACHMENT_ID", attachmentId);
        LOGGER.info("bodyRequest: " + bodyRequest);
        HttpEntity<String> requestBody = new HttpEntity<>(bodyRequest, headers);
        String urlRequest = sendMsgUrl.replace("{access_token}", accessToken);
        return restTemplate.postForObject(urlRequest, requestBody, ZaloResponse.class);
    }

    //Send messagePattern to list follower
    public ZaloResponse sendZaloMessage(String userId, String message, String attachmentId,
            String urlLink, String urlTitle) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        //Headers
        HttpHeaders headers = new HttpHeaders();
        //headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //Body
        String escapeMessage = StringEscapeUtils.escapeJava(message);
        String bodyRequest = messagePattern.replace("USER_ID", userId).replace("MESSAGE", escapeMessage)
                .replace("ATTACHMENT_ID", attachmentId).replace("URL_TITLE", urlTitle)
                .replace("URL_LINK", urlLink);
        LOGGER.info("bodyRequest: " + bodyRequest);
        HttpEntity<String> requestBody = new HttpEntity<>(bodyRequest, headers);
        String urlRequest = sendMsgUrl.replace("{access_token}", accessToken);
        return restTemplate.postForObject(urlRequest, requestBody, ZaloResponse.class);
    }

    //Upload image to zalo to get attachment id
    public ZaloResponse uploadImage(String imageName, String imageUrl) {
        LOGGER.info("imageName: {} - imageUrl: {}", imageName, imageUrl);
        ByteArrayOutputStream byteArrayOutputStream = null;
        URL url = null;
        BufferedImage image = null;
        try {
            //Get image byte from url
            url = new URL(imageUrl);
            image = ImageIO.read(url);
            byteArrayOutputStream = new ByteArrayOutputStream();
            if (imageName != null && imageName.toLowerCase().endsWith(".png")) {
                ImageIO.write(image, "png", byteArrayOutputStream);
            } else {
                ImageIO.write(image, "jpg", byteArrayOutputStream);
            }
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
            ex.printStackTrace();
        }

        //Upload 2 zalo
        if (byteArrayOutputStream != null && byteArrayOutputStream.size() > 0) {
            ByteArrayOutputStream sendOutputStream = byteArrayOutputStream;
            LOGGER.info("Image upload size: {}", byteArrayOutputStream.size());
            //If image size > 1MB => Compress
            if (byteArrayOutputStream.size() > 1024 * 1000) {
                ByteArrayOutputStream compressed = ImageUtil.resize(image, imageName, 360);
                sendOutputStream = compressed;
                LOGGER.info("Image upload size after: " + (sendOutputStream != null ? sendOutputStream.size() : 0));
            }
            //
            LinkedMultiValueMap<String, String> imgHeaderMap = new LinkedMultiValueMap<>();
            imgHeaderMap.add("Content-disposition", "form-data; name=file; filename=" + imageName);
            imgHeaderMap.add("Content-type", "image/jpeg");
            HttpEntity<byte[]> img = new HttpEntity<>(sendOutputStream.toByteArray(), imgHeaderMap);
            LinkedMultiValueMap<String, Object> multipartReqMap = new LinkedMultiValueMap<>();
            multipartReqMap.add("file", img);
            //Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartReqMap, headers);
            RestTemplate restTemplate = new RestTemplate();
            String urlRequest = uploadUrl.replace("{access_token}", accessToken);
            String result = null;
            try {
                result = restTemplate.postForObject(urlRequest, requestEntity, String.class);
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.info("Error to upload zalo: {}", ExceptionUtils.getStackTrace(ex));
            }
            if (result != null) {
                LOGGER.info("result: " + result);
                ObjectMapper mapper = new ObjectMapper();
                try {
                    return mapper.readValue(result, ZaloResponse.class);
                } catch (JsonProcessingException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public String getSendMsgUrl() {
        return sendMsgUrl;
    }

    public void setSendMsgUrl(String sendMsgUrl) {
        this.sendMsgUrl = sendMsgUrl;
    }

    public String getReceiverUrl() {
        return receiverUrl;
    }

    public void setReceiverUrl(String receiverUrl) {
        this.receiverUrl = receiverUrl;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getMessagePattern() {
        return messagePattern;
    }

    public void setMessagePattern(String messagePattern) {
        this.messagePattern = messagePattern;
    }
}
