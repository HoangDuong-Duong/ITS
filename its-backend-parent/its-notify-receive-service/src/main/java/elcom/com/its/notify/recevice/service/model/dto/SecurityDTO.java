package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import elcom.com.its.notify.recevice.service.enums.EventStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Data
public class SecurityDTO {

    String id;

    private Date startTime;


    private String eventId;

    private String sourceId;

    private String sourceName;

    private SiteInfo site;

    private Object detail;

    private LanesRoute lane;

    private String imageUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date imageTime;

    private String eventCode;

    private String eventName;
    private EventStatus eventStatus;

    private String nameStatus;

    private String directionCode;

    private String modifiedBy;

    private String modifiedAction;

    private String parentId;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;

    private Integer isDelete;

    private Integer isNewest;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    private String imageCtxtUrl;

    private SiteInfo siteCorrect;

    private String videoUrl;

    private String note;
}
