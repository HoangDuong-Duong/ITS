package elcom.com.its.notify.recevice.service.service.impl;

import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
import elcom.com.its.notify.recevice.service.model.dto.Stage;
import elcom.com.its.notify.recevice.service.model.dto.StageConfig;
import elcom.com.its.notify.recevice.service.model.dto.StageResponseList;
import elcom.com.its.notify.recevice.service.service.StageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class StageServiceImpl implements StageService{
    private static final Logger LOGGER = LoggerFactory.getLogger(StageService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public List<StageConfig> getAll() {
        List<StageConfig> stageList = null;
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/stage?page=0&size=1000&isAdmin=true&stageIdLs=null";
        StageResponseList dto = restTemplate.getForObject(urlRequest, StageResponseList.class);
        LOGGER.info("Get camera list from ITS Core: {}", dto);
        if (dto != null && dto.getStatus() == HttpStatus.OK.value()) {
            stageList = dto.getData();
        }
        return stageList;
    }

}
