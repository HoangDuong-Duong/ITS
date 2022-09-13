package elcom.com.its.notify.recevice.service.service;

import elcom.com.its.notify.recevice.service.model.dto.Stage;
import elcom.com.its.notify.recevice.service.model.dto.StageConfig;

import java.util.List;

public interface StageService {
    List<StageConfig> getAll();
}
