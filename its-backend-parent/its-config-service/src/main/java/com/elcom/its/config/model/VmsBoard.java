package com.elcom.its.config.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@Setter
@Getter
@RequiredArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class VmsBoard implements Serializable {
    private String id;
    private String name;
    private String site;
    private Float longitude;
    private Float latitude;
    private String positionM;
    private String typeBoard;
    private String model;
    private Float length;
    private Float width;
    private String ip;
    private String direction;
    private String manufacturer;
    private String language;
    private String content;
    private String onTime;
    private String offTime;
}
