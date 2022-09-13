/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.service.impl;

import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
import elcom.com.its.notify.recevice.service.model.dto.Camera;
import elcom.com.its.notify.recevice.service.model.dto.CameraResponseList;
import elcom.com.its.notify.recevice.service.service.ITSCameraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 *
 * @author Admin
 */
@Service
public class ITSCameraServiceImpl implements ITSCameraService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITSCameraServiceImpl.class);
    private static final String URI = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/camera";
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Camera> getAll() {
        List<Camera> cameraList = null;
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/camera?page=0&size=1000";
        CameraResponseList dto = restTemplate.getForObject(urlRequest, CameraResponseList.class);
        LOGGER.info("Get camera list from ITS Core: {}", dto);
        if (dto != null && dto.getStatus() == HttpStatus.OK.value()) {
            cameraList = dto.getData();
        }
        return cameraList;
    }

}
