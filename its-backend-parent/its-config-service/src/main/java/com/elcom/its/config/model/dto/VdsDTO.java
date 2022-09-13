/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.model.dto;

import com.elcom.its.config.convert.HashMapConverter;
import com.elcom.its.config.enums.CameraLayoutType;
import com.elcom.its.config.model.CameraDTO;
import com.elcom.its.config.model.ProcessUnit;
import com.elcom.its.config.model.Vds;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Admin
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VdsDTO implements Serializable {

    private String cameraId;
    private String processUnitId;
    private Integer layoutType;

    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> capability;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> detectors;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> render;

    public static Vds toEntity(VdsDTO vdsDTO, ProcessUnit processUnit, CameraDTO cameraDTO,
            String userId) {
        Vds vds = new Vds(UUID.randomUUID().toString());
        vds.setCameraId(cameraDTO.getId());
        vds.setCameraName(cameraDTO.getName());
        vds.setCameraStatus(cameraDTO.getCameraStatus().code());
        vds.setCameraType(cameraDTO.getCameraType());
        vds.setCreatedDate(new Date());
        vds.setCreatedBy(userId);
        vds.setModifiedDate(new Date());
        vds.setModifiedBy(userId);
        vds.setVdsKey("VDS-" + cameraDTO.getCameraKey());
        if (cameraDTO.getSiteDTOForCameraId() != null) {
            vds.setVdsName("VDS điểm " + cameraDTO.getSiteDTOForCameraId().getName());
            vds.setSiteId(cameraDTO.getSiteDTOForCameraId().getId());
            vds.setSiteName(cameraDTO.getSiteDTOForCameraId().getName());
        } else {
            vds.setVdsName("VDS chưa có điểm giám sát...");
        }
        vds.setLayoutType(vdsDTO.getLayoutType());
        vds.setLayoutTypeName(CameraLayoutType.parse(vdsDTO.getLayoutType()).description());
        vds.setProcessUnitId(processUnit.getId());
        vds.setProcessUnitName(processUnit.getName());
        vds.setStatus(processUnit.getStatus());
        return vds;
    }

    public static Vds toEntity(Vds existVds, VdsDTO vdsDTO, ProcessUnit processUnit,
            CameraDTO cameraDTO, String userId) {
        Vds vds = new Vds(existVds.getId());
        vds.setCameraId(cameraDTO.getId());
        vds.setCameraName(cameraDTO.getName());
        vds.setCameraStatus(cameraDTO.getCameraStatus().code());
        vds.setCameraType(cameraDTO.getCameraType());
        vds.setCreatedDate(existVds.getCreatedDate());
        vds.setCreatedBy(existVds.getCreatedBy());
        vds.setModifiedDate(new Date());
        vds.setModifiedBy(userId);
        vds.setVdsKey("VDS-" + cameraDTO.getCameraKey());
        vds.setVdsName("VDS điểm " + cameraDTO.getSiteDTOForCameraId().getName());
        vds.setLayoutType(vdsDTO.getLayoutType());
        vds.setLayoutTypeName(CameraLayoutType.parse(vdsDTO.getLayoutType()).description());
        vds.setProcessUnitId(processUnit.getId());
        vds.setProcessUnitName(processUnit.getName());
        vds.setSiteId(cameraDTO.getSiteDTOForCameraId().getId());
        vds.setSiteName(cameraDTO.getSiteDTOForCameraId().getName());
        vds.setStatus(existVds.getStatus());
        return vds;
    }
}
