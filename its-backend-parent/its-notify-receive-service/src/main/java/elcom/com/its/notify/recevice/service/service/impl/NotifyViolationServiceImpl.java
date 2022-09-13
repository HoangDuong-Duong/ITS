package elcom.com.its.notify.recevice.service.service.impl;

import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
import elcom.com.its.notify.recevice.service.model.dto.ViolationListResponse;
import elcom.com.its.notify.recevice.service.service.NotifyViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class NotifyViolationServiceImpl implements NotifyViolationService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String URI = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/violations";

    @Override
    public ViolationListResponse getListNotifyBySite(String camIds, int page, int size) {
        String url = URI + "/pushed/site?";
        Date now = new Date();
        String params = "cameraIds=" + camIds
                + "&startDate=" + getStartOfDay(now)
                + "&endDate=" + now
                + "&page=" + page
                + "&size=" + size;
        ViolationListResponse violationDTOS = restTemplate.getForObject(url + params, ViolationListResponse.class);
        return violationDTOS;
    }

    @Override
    public ViolationListResponse getListNotify(int page, int size) {
        String url = URI + "/pushed?";
        Date now = new Date();
        String params = "startDate=" + getStartOfDay(now)
                + "&endDate=" + now
                + "&page=" + page
                + "&size=" + size;
        ViolationListResponse violationDTOS = restTemplate.getForObject(url + params, ViolationListResponse.class);
        return violationDTOS;
    }

    public Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }/*from   w w  w .  ja v  a  2s.  c o m*/
        return Date.from(date.toInstant().atZone(ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.DAYS).toInstant());
    }
}
