/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.service.impl;

import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
import elcom.com.its.notify.recevice.service.model.dto.NotifyTrigger;
import elcom.com.its.notify.recevice.service.model.dto.NotifyTriggerDTO;
import elcom.com.its.notify.recevice.service.service.ITSNotifyTriggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
@Service
public class ITSNotifyTriggerServiceImpl implements ITSNotifyTriggerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITSNotifyTriggerServiceImpl.class.getName());

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public NotifyTrigger save(NotifyTrigger notifyTrigger) {
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/notifications/trigger";
        HttpEntity<NotifyTrigger> requestEntity = new HttpEntity<>(notifyTrigger);
        NotifyTriggerDTO dto = restTemplate.postForObject(urlRequest, requestEntity, NotifyTriggerDTO.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto.getData();
        }
        return null;
    }

    @Override
    public NotifyTrigger update(NotifyTrigger notifyTrigger) {
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/notifications/trigger/" + notifyTrigger.getId();
        HttpEntity<NotifyTrigger> requestEntity = new HttpEntity<>(notifyTrigger);
        HttpEntity<NotifyTriggerDTO> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, NotifyTriggerDTO.class);
        NotifyTriggerDTO dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto.getData();
        }
        return null;
    }

    @Override
    public boolean remove(String notifyTriggerId) {
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/notifications/trigger/" + notifyTriggerId;
        try {
            HttpEntity<NotifyTriggerDTO> response = restTemplate.exchange(urlRequest, HttpMethod.DELETE, null, NotifyTriggerDTO.class);
            NotifyTriggerDTO dto = response != null ? response.getBody() : null;
            return dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value());
        } catch (Exception ex) {
            LOGGER.error("Error to delete notify trigger {} from ITS Core >>> {}", notifyTriggerId, ex.toString());
            return false;
        }
    }

    @Override
    public boolean removeList(List<String> notifyTriggerIds) {
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/notifications/trigger";
        try {
            //Payload request
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("ids", notifyTriggerIds);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyMap);
            //Call rest api with url, mthod, payload, response entity
            HttpEntity<NotifyTriggerDTO> response = restTemplate.exchange(urlRequest, HttpMethod.DELETE, requestEntity, NotifyTriggerDTO.class);
            NotifyTriggerDTO dto = response != null ? response.getBody() : null;
            return dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value());
        } catch (Exception ex) {
            LOGGER.error("Error to delete notify trigger list {} from DBM >>> {}", notifyTriggerIds, ex.toString());
            return false;
        }
    }

}
