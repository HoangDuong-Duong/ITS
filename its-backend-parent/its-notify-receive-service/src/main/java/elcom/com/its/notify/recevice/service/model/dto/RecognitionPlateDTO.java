/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elcom.com.its.notify.recevice.service.enums.RecordDeleteStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Admin
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RecognitionPlateDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date startTime;
    private String sourceId;
    private String sourceName;
    private SiteInfo site;
    private LanesRoute lane;
    private Object bbox;
    private String imageUrl;
    private Float longitude;
    private Float latitude;
    private String plate;
    private Integer event;
    private String eventCode;
    private String eventName;
    private String objectType;
    private String objectName;
    private Float speedOfVehicle;
    private String createBy;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createDate;
    private String modifiedBy;
    private String modifiedAction;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date modifiedDate;
    private Integer isNewest;
    private String isDelete;
    private String reason;
    private String eventIdString;
    private String brand;
    private String color;
    private String identityValue;
    private String parentId;

    public String toJsonString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public String toGMT7JsonString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public String toString() {
        return "com.elcom.itscore.data.model.RecognitionPlate[ recognitionPK=" + this.id + " ]";
    }
}
