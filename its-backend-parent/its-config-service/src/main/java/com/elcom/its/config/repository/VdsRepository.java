/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.repository;

import com.elcom.its.config.model.Vds;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Repository
public interface VdsRepository extends CrudRepository<Vds, String> {

    boolean existsByLayoutTypeAndCameraIdAndProcessUnitId(Integer layoutType, String cameraId, String processUnitId);
    
    Page<Vds> findAll(Specification<Vds> spec, Pageable pageable);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Vds u SET u.modifiedBy = :uuid, u.status = :status, u.modifiedDate = now() WHERE u.id = :id ")
    int updateStatus(@Param("id") String id, @Param("status") Integer status, @Param("uuid") String uuid);
    
    List<Vds> findByProcessUnitIdAndStatus(String processUnitId, Integer status);
    
    @Query("SELECT DISTINCT(v.processUnitId) FROM Vds v WHERE v.layoutId = :layoutId")
    List<String> findAllProcessUnitIdByLayoutId(@Param("layoutId") Long layoutId);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Vds u SET u.cameraStatus = :cameraStatus, u.modifiedDate = now() WHERE u.cameraId = :cameraId ")
    int updateCameraStatus(@Param("cameraStatus") Integer cameraStatus, @Param("cameraId") String cameraId);
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Vds u SET u.siteId = :siteId, u.siteName= :siteName, u.modifiedDate = now() WHERE u.cameraId = :cameraId ")
    int updateVdsSiteId(@Param("cameraId") String cameraId, @Param("siteId") String siteId,@Param("siteName")  String siteName);
}
