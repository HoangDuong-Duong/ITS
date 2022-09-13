package elcom.com.its.notify.recevice.service.service;

import elcom.com.its.notify.recevice.service.model.dto.*;

import java.util.List;
import java.util.Map;

public interface ITSSiteService {

    List<SiteInfoConfig> getAll();

    Point getPoint( String siteId, String directionCode);

    Map<String, List<PointPlace>> getPointPlace(String place);
}
