package com.elcom.its.config.model;

import com.elcom.its.config.enums.DataStatus;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.model.dto.SiteDTOForCamera;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.List;

/**
 * @author ducduongn
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CameraDTO implements Serializable {

    private String id;

    private String name;

    private String cameraKey;

    private String cameraModel;

    private String cameraType;

    @JsonProperty("siteId")
    private SiteDTOForCamera siteDTOForCameraId;

    private float longitude;

    private float latitude;

    private CameraSetupAttribute setupAttributes;

    private CameraUrls urls;

    private String note;

    private DataStatus status;

    private DeviceStatus cameraStatus;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String modifiedDate;

    private String imageUrl;

    private List<AIEngineJson> aiEngineJson;

    private String liveImagesUrl;

    private String vmsId;

    private Integer ptz;

    private String directionCode;

    private String directionString;

    private List<ActiveCameraLayout> activeLayouts;

    private List<CameraInProcessUnit> processUnitUse;

    private PtzInfo ptzInfo;

    private String stationId;

    private Integer displayLevel;
}

