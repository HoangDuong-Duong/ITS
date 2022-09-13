package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Type;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class StageConfig {
    public String stageId;
    public String stageName;
    public String siteStart;
    public String siteStartName;
    public Long positionStartM;
    public String siteEnd;
    public String siteEndName;
    public Long positionEndM;
    public String directionCode;
    public String directionString;
    public String createBy;
    public String code;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    public Date createDate;
    public TrafficStatus trafficStatus;

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getSiteStart() {
        return siteStart;
    }

    public void setSiteStart(String siteStart) {
        this.siteStart = siteStart;
    }

    public String getSiteStartName() {
        return siteStartName;
    }

    public void setSiteStartName(String siteStartName) {
        this.siteStartName = siteStartName;
    }

    public Long getPositionStartM() {
        return positionStartM;
    }

    public void setPositionStartM(Long positionStartM) {
        this.positionStartM = positionStartM;
    }

    public String getSiteEnd() {
        return siteEnd;
    }

    public void setSiteEnd(String siteEnd) {
        this.siteEnd = siteEnd;
    }

    public String getSiteEndName() {
        return siteEndName;
    }

    public void setSiteEndName(String siteEndName) {
        this.siteEndName = siteEndName;
    }

    public Long getPositionEndM() {
        return positionEndM;
    }

    public void setPositionEndM(Long positionEndM) {
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public TrafficStatus getTrafficStatus() {
        return trafficStatus;
    }

    public void setTrafficStatus(TrafficStatus trafficStatus) {
        this.trafficStatus = trafficStatus;
    }
}
