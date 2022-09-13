/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.convert.HashMapConverter;
import com.elcom.its.vds.model.LayoutAreas;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Admin
 */
@SuperBuilder
@Setter
@Getter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
@ToString
public class LayoutAreaDTO {

    private Long id;

    private long cameraLayoutId;

    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> jsonDetailArea;

    private long roiType;

    public LayoutAreaDTO(long cameraLayoutId, Map<String, Object> jsonDetailArea, long roiType) {
        this.cameraLayoutId = cameraLayoutId;
        this.jsonDetailArea = jsonDetailArea;
        this.roiType = roiType;
    }

    public static LayoutAreas toEntity(LayoutAreaDTO layoutAreaDto) {
        LayoutAreas layoutAreas = new LayoutAreas();
        layoutAreas.setLayoutId(layoutAreaDto.getCameraLayoutId());
        layoutAreas.setRoiType(layoutAreaDto.getRoiType());
        layoutAreas.setJsonDetailArea(layoutAreaDto.getJsonDetailArea());
        return layoutAreas;
    }
}
