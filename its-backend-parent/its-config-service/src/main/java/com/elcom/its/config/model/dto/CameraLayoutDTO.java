/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.model.dto;

import com.elcom.its.config.convert.HashMapConverter;
import com.elcom.its.config.enums.DataStatus;
import com.elcom.its.config.model.CameraLayouts;
import java.io.Serializable;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
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
@SuperBuilder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class CameraLayoutDTO implements Serializable {

    private Long id;

    @NotBlank(message = "Name required and unique")
    @Size(max = 200)
    private String name;

    @Size(max = 255)
    private String usedCaptureImage;

    private String cameraId;

    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> jsonCanvas;

    private int widthImgScale;

    private int heightImgScale;

    @Size(max = 255)
    private String sizedImg;

    @Size(max = 255)
    private String scaledFitImg;

    private int originWidth;

    private int originHeight;

    private String layoutImage;

    @Size(max = 255)
    private String description;

    private int layoutType;

    @Builder.Default
    private int version = 1;

    private long modelProfileId;

    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> layoutAreas;

    @Builder.Default
    private DataStatus status = DataStatus.ENABLE;

    @Convert(converter = HashMapConverter.class)
    @Column(columnDefinition = "json")
    private Map<String, Object> metaAttributes;

    public CameraLayoutDTO(String name, String usedCaptureImage, String cameraId,
            Map<String, Object> jsonCanvas, int widthImgScale, int heightImgScale,
            String sizedImg, String scaledFitImg, int originWidth, int originHeight,
            String layoutImage, String description, int version, long modelProfileId,
            int layoutType, Map<String, Object> layoutAreas, DataStatus status,
            Map<String, Object> metaAttributes) {
        this.name = name;
        this.usedCaptureImage = usedCaptureImage;
        this.cameraId = cameraId;
        this.jsonCanvas = jsonCanvas;
        this.widthImgScale = widthImgScale;
        this.heightImgScale = heightImgScale;
        this.sizedImg = sizedImg;
        this.scaledFitImg = scaledFitImg;
        this.originWidth = originWidth;
        this.originHeight = originHeight;
        this.layoutImage = layoutImage;
        this.description = description;
        this.version = version;
        this.modelProfileId = modelProfileId;
        this.layoutType = layoutType;
        this.layoutAreas = layoutAreas;
        this.status = status;
        this.metaAttributes = metaAttributes;
    }

    public static CameraLayouts toEntity(CameraLayoutDTO cameraLayoutDto) {
        CameraLayouts cameraLayouts = new CameraLayouts();
        cameraLayouts.setCameraId(cameraLayoutDto.getCameraId());
        cameraLayouts.setDescription(cameraLayoutDto.getDescription());
        cameraLayouts.setHeightImgScale(cameraLayoutDto.getHeightImgScale());
        cameraLayouts.setJsonCanvas(cameraLayoutDto.getJsonCanvas());
        cameraLayouts.setLayoutAreasJson(cameraLayoutDto.getLayoutAreas());
        cameraLayouts.setLayoutImage(cameraLayoutDto.getLayoutImage());
        cameraLayouts.setMetaAttributes(cameraLayoutDto.getMetaAttributes());
        cameraLayouts.setName(cameraLayoutDto.getName());
        cameraLayouts.setOriginHeight(cameraLayoutDto.getOriginHeight());
        cameraLayouts.setOriginWidth(cameraLayoutDto.getOriginWidth());
        cameraLayouts.setScaledFitImg(cameraLayoutDto.getScaledFitImg());
        cameraLayouts.setSizedImg(cameraLayoutDto.getSizedImg());
        cameraLayouts.setUsedCaptureImage(cameraLayoutDto.getUsedCaptureImage());
        cameraLayouts.setVersion(cameraLayoutDto.getVersion());
        cameraLayouts.setWidthImgScale(cameraLayoutDto.getWidthImgScale());
        cameraLayouts.setModelProfileId(cameraLayoutDto.getModelProfileId());
        cameraLayouts.setLayoutType(cameraLayoutDto.getLayoutType());
        cameraLayouts.setCameraId(cameraLayoutDto.getCameraId());
        return cameraLayouts;
    }
}
