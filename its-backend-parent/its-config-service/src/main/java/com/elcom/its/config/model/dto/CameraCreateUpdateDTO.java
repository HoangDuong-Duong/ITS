package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.CameraDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.*;

/**
 * @author ducduongn
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CameraCreateUpdateDTO {

    private String name;

    private String cameraKey;

    private String cameraModel;

    private String cameraType;

    private String siteId;

    private Float longitude;

    private Float latitude;

    private CameraSetupAttribute setupAttributes;

    private CameraUrls urls;

    private String directionCode;

    private PtzInfo ptzInfo;

    private Integer ptz;

    private String directionString;

    private String vmsId;

    private String status;

    private String cameraStatus;

    private String note;

    private String imageUrl;

    private List<ActiveCameraLayout> activeLayouts;

    private List<CameraInProcessUnit> processUnitUse;

    private String stationId;

    private Integer displayLevel;

    public CameraCreateUpdateDTO(CameraDTO detailDTO) {
        if (detailDTO != null) {
            this.name = detailDTO.getName();
            this.cameraKey = detailDTO.getCameraKey();
            this.cameraModel = detailDTO.getCameraModel();
            this.cameraType = detailDTO.getCameraType();
            this.siteId = detailDTO.getSiteDTOForCameraId().getId();
            this.longitude = detailDTO.getLongitude();
            this.latitude = detailDTO.getLatitude();
            this.setupAttributes = detailDTO.getSetupAttributes();
            this.urls = detailDTO.getUrls();
            this.directionCode = detailDTO.getDirectionCode();
            this.ptzInfo = detailDTO.getPtzInfo();
            this.ptz = detailDTO.getPtz();
            this.directionString = detailDTO.getDirectionString();
            this.vmsId = detailDTO.getVmsId();
            this.status = detailDTO.getStatus().name();
            this.cameraStatus = detailDTO.getCameraStatus().name();
            this.status = detailDTO.getStatus().name();
            this.note = detailDTO.getNote();
            this.imageUrl = detailDTO.getImageUrl();
            this.activeLayouts = detailDTO.getActiveLayouts();
            this.processUnitUse = detailDTO.getProcessUnitUse();
            this.stationId = detailDTO.getStationId();
            this.displayLevel = detailDTO.getDisplayLevel();
        }
    }
}
