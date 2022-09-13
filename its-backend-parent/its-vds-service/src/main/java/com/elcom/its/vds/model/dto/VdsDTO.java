/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model.dto;

import com.elcom.its.utils.StringUtil;
import com.elcom.its.vds.convert.HashMapConverter;
import com.elcom.its.vds.enums.CameraLayoutType;
import com.elcom.its.vds.model.CameraDTO;
import com.elcom.its.vds.model.ProcessUnit;
import com.elcom.its.vds.model.Vds;
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

    private String vdsName;
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
        vds.setVdsName(vdsDTO.getVdsName());
        if (cameraDTO.getSiteDTOForCameraId() != null) {
            vds.setSiteId(cameraDTO.getSiteDTOForCameraId().getId());
            vds.setSiteName(cameraDTO.getSiteDTOForCameraId().getName());
        }
        vds.setLayoutType(vdsDTO.getLayoutType());
        vds.setLayoutTypeName(CameraLayoutType.parse(vdsDTO.getLayoutType()).description());
        vds.setProcessUnitId(processUnit.getId());
        vds.setProcessUnitName(processUnit.getName());
        vds.setStatus(processUnit.getStatus());
        vds.setDirectionCode(cameraDTO.getDirectionCode());
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
        vds.setVdsName(!StringUtil.isNullOrEmpty(vdsDTO.getVdsName()) ? vdsDTO.getVdsName() : existVds.getVdsName());
        vds.setLayoutType(vdsDTO.getLayoutType());
        vds.setLayoutTypeName(CameraLayoutType.parse(vdsDTO.getLayoutType()).description());
        vds.setProcessUnitId(processUnit.getId());
        vds.setProcessUnitName(processUnit.getName());
        vds.setSiteId(cameraDTO.getSiteDTOForCameraId().getId());
        vds.setSiteName(cameraDTO.getSiteDTOForCameraId().getName());
        vds.setStatus(existVds.getStatus());
        vds.setDirectionCode(cameraDTO.getDirectionCode());
        return vds;
    }
}
