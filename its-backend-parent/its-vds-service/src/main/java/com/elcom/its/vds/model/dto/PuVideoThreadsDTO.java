/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.convert.HashMapConverter;
import com.elcom.its.vds.enums.DataStatus;
import com.elcom.its.vds.model.PuVideoThreads;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Admin
 */
@Setter
@Getter
@RequiredArgsConstructor
@SuperBuilder
@ToString
@Data
public class PuVideoThreadsDTO implements Serializable {

    private Long id;
    private String cameraId;
    private Long layoutId;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> capability;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> detectors;
    
    private String cacheUrl;
    private int cacheLength;
    private int autoAdjust;
    
    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> render;
    
    private String cameraName;
    private String idProcessUnit;
    
    @Builder.Default
    private DataStatus status = DataStatus.ENABLE;
    
    private String createdBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    
    private String modifiedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    public PuVideoThreadsDTO(String cameraId, long layoutId, Map<String, Object> capability, 
            Map<String, Object> detectors, String cacheUrl, int cacheLength, int autoAdjust, 
            Map<String, Object> render, DataStatus status, String idProcessUnit) {
        this.cameraId = cameraId;
        this.layoutId = layoutId;
        this.capability = capability;
        this.detectors = detectors;
        this.cacheUrl = cacheUrl;
        this.cacheLength = cacheLength;
        this.autoAdjust = autoAdjust;
        this.render = render;
        this.status = status;
        this.idProcessUnit = idProcessUnit;
    }

    public static PuVideoThreadsDTO fromModel(PuVideoThreads model) {
        if (model == null) {
            return null;
        }
        return PuVideoThreadsDTO.builder().id(model.getId())
                .cameraId(model.getCameraId())
                .layoutId(model.getLayoutId())
                .capability(model.getCapability())
                .detectors(model.getDetectors())
                .cacheUrl(model.getCacheUrl())
                .cacheLength(model.getCacheLength())
                .autoAdjust(model.getAutoAdjust())
                .render(model.getRender())
                .status(DataStatus.of(model.getStatus()))
                .createdBy(model.getCreatedBy())
                .createdDate(model.getCreatedDate())
                .modifiedBy(model.getModifiedBy())
                .modifiedDate(model.getModifiedDate())
                .build();
    }

}
