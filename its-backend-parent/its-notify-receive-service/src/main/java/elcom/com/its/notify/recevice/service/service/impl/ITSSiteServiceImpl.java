package elcom.com.its.notify.recevice.service.service.impl;

import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
import elcom.com.its.notify.recevice.service.model.dto.*;
import elcom.com.its.notify.recevice.service.service.ITSSiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ITSSiteServiceImpl implements ITSSiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITSSiteServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public List<SiteInfoConfig> getAll() {
        List<SiteInfoConfig> sitesList = null;
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/site?page=0&size=1000&isAdmin=true&stageIdLs=null";
        SiteResponseList dto = restTemplate.getForObject(urlRequest, SiteResponseList.class);
        LOGGER.info("Get camera list from ITS Core: {}", dto);
        if (dto != null && dto.getStatus() == HttpStatus.OK.value()) {
            sitesList = dto.getData();
        }
        return sitesList;
    }

    @Override
    public Point getPoint(String siteId, String directionCode) {
        Point point = new Point();
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/site/point-in-road";
        String params = "siteId=" + siteId
                + "&directionCode=" + directionCode;
        urlRequest = urlRequest + "?" + params;
        PointResponse dto = restTemplate.getForObject(urlRequest, PointResponse.class);
        LOGGER.info("Get camera list from ITS Core: {}", dto);
        if (dto != null && dto.getStatus() == HttpStatus.OK.value()) {
            point = dto.getData();
        }
        return point;
    }

    @Override
    public Map<String, List<PointPlace>>  getPointPlace(String place) {
        List<PointPlace> pointPlaces = new ArrayList<>();
        String urlRequest = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/place/listPoint";
        String params = "placeIds=" + place;
        urlRequest = urlRequest + "?" + params;
        PointResponsePlace dto = restTemplate.getForObject(urlRequest, PointResponsePlace.class);
        LOGGER.info("Get camera list from ITS Core: {}", dto);
        if (dto != null && dto.getStatus() == HttpStatus.OK.value()) {
            pointPlaces = dto.getData();
        }
        Map<String, List<PointPlace>> pointPlaceMap = pointPlaces.parallelStream().collect(Collectors.groupingBy(item -> item.getPlaceId()));
        return pointPlaceMap;


    }
}
