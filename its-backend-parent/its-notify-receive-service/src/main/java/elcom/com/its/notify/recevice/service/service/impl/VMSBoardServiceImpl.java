package elcom.com.its.notify.recevice.service.service.impl;

import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
import elcom.com.its.notify.recevice.service.model.dto.VMSBoardResponseList;
import elcom.com.its.notify.recevice.service.model.dto.VMSResponseList;
import elcom.com.its.notify.recevice.service.model.dto.Vms;
import elcom.com.its.notify.recevice.service.model.dto.VmsBoard;
import elcom.com.its.notify.recevice.service.service.StageService;
import elcom.com.its.notify.recevice.service.service.VMSBoardService;
import elcom.com.its.notify.recevice.service.service.VMSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class VMSBoardServiceImpl implements VMSBoardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StageService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<VmsBoard> getAll() {
        List<VmsBoard> vmsList = null;
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/vms-board";
        VMSBoardResponseList dto = restTemplate.getForObject(urlRequest, VMSBoardResponseList.class);
        if (dto != null && dto.getStatus() == HttpStatus.OK.value()) {
            vmsList = dto.getData();
        }
        return vmsList;
    }
}
