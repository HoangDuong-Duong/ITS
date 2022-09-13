package com.elcom.its.config.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TrafficStatus implements Serializable {
    private Integer id;
    private long volume;
    private String statusCode;
    private String statusName;
    private String createdBy;
    private Date createdDate;
}
