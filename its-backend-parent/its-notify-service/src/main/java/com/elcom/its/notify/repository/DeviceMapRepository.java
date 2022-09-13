package com.elcom.its.notify.repository;

import com.elcom.its.notify.model.DeviceMap;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceMapRepository extends CrudRepository<DeviceMap, String> {

    DeviceMap findByUserId(String userId);

    @Override
    List<DeviceMap> findAll();
}
