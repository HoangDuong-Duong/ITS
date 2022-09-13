/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.model.dto;

import com.elcom.its.config.convert.HashMapConverter;
import com.elcom.its.config.enums.DataStatus;
import com.elcom.its.config.model.ProcessUnit;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.persistence.Convert;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProcessUnitDTO implements Serializable {

    private String id;

    private String createdBy;

    private Date createdDate;

    private String modifiedBy;

    private Date modifiedDate;

    @NotNull(message = "Code required and unique")
    @Size(max = 200)
    private String code;
    @NotNull(message = "Name required")
    @Size(max = 255)
    private String name;
    @NotNull(message = "Type required")
    @Size(max = 50)
    private String type;
    @Size(max = 255)
    private String note;
    @Size(max = 255)
    private String description;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> modelProfiles;

    private Long serves;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> appServices;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> tvRenders;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> eventNotifiers;

    private Integer status;
    private int version;

    private String serverName;
    
    private Integer puType;

    public ProcessUnitDTO(String code, String name, String type, String note, String description, 
            Map<String, Object> modelProfiles, Long serves, Map<String, Object> appServices, 
            Map<String, Object> tvRenders, Map<String, Object> eventNotifiers, Integer status, 
            int version, Integer puType) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.note = note;
        this.description = description;
        this.modelProfiles = modelProfiles;
        this.serves = serves;
        this.appServices = appServices;
        this.tvRenders = tvRenders;
        this.eventNotifiers = eventNotifiers;
        this.status = status;
        this.version = version;
        this.puType = puType;
    }
    
    public static ProcessUnit toEntity(ProcessUnitDTO processUnitDto) {
        ProcessUnit processUnit = new ProcessUnit();
        processUnit.setAppServices(processUnitDto.getAppServices());
        processUnit.setCode(processUnitDto.getCode());
        processUnit.setCreatedDate(new Date());
        processUnit.setDescription(processUnitDto.getDescription());
        processUnit.setEventNotifiers(processUnitDto.getEventNotifiers());
        processUnit.setModelProfiles(processUnitDto.getModelProfiles());
        processUnit.setName(processUnitDto.getName());
        processUnit.setNote(processUnitDto.getNote());
        processUnit.setServers(processUnitDto.getServes());
        processUnit.setTvRenders(processUnitDto.getTvRenders());
        processUnit.setType(processUnitDto.getType());
        processUnit.setStatus(DataStatus.ENABLE.code());
        return processUnit;
    }
}
