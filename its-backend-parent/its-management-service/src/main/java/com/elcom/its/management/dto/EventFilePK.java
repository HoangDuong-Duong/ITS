package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class EventFilePK {

    private String id;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date startTime;

    public EventFilePK() {
    }

    public EventFilePK(String id, Date startTime) {
        this.id = id;
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

}
