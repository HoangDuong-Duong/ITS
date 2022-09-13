/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import elcom.com.its.notify.recevice.service.enums.DataStatus;
import elcom.com.its.notify.recevice.service.enums.DeviceStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 *
 * @author Admin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VmsBoard {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private Site siteId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Float longitude;
    private Float latitude;
    private String positionM;
    private String startTime;
    private String model;
    private String manufacturer;
    private String language;
    private Float length;
    private Float width;
    private String ip;
    private DeviceStatus boardStatus;
    private Date installDate;
    private String createdBy;
    private Date createDate;
    private String directionCode;
    private String directionString;
    private String endTime;
    private String installer;
    private String typeBoard;
    private String realId;


}
