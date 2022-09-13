package com.elcom.its.vds.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;

/**
 * @author ducduongn
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectTrackingCreateUpdateDto implements Serializable {
    private String model;
    private String infoObject;
    private String reason;
    private String alertChannel;
    private String alertReceiver;
    private String queue;
    private String description;
    private String note;
    private String indentification;
    private String objectType;
    private String objectImages;
}
