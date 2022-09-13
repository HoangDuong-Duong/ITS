package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stage {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String siteStart;
    private long positionStartM;
    private String siteEnd;
    private long positionEndM;
    private String directionCode;
    private String directionString;
    private TrafficStatus trafficStatus;
    private String createdBy;
    private String  code;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createdDate;

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteStart() {
        return siteStart;
    }

    public void setSiteStart(String siteStart) {
        this.siteStart = siteStart;
    }

    public long getPositionStartM() {
        return positionStartM;
    }

    public void setPositionStartM(long positionStartM) {
        this.positionStartM = positionStartM;
    }

    public String getSiteEnd() {
        return siteEnd;
    }

    public void setSiteEnd(String siteEnd) {
        this.siteEnd = siteEnd;
    }

    public long getPositionEndM() {
        return positionEndM;
    }

    public void setPositionEndM(long positionEndM) {
        this.positionEndM = positionEndM;
    }

    public String getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    public String getDirectionString() {
        return directionString;
    }

    public void setDirectionString(String directionString) {
        this.directionString = directionString;
    }

    public TrafficStatus getTrafficStatus() {
        return trafficStatus;
    }

    public void setTrafficStatus(TrafficStatus trafficStatus) {
        this.trafficStatus = trafficStatus;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
