/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Admin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Stage implements Comparable<Stage> {

    private String id;

    private String name;

    private String siteStart;

    private long positionStartM;

    private String siteEnd;

    private long positionEndM;

    private String directionCode;

    private String code;

    private String directionString;

    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date createdDate;

    @JsonIgnore
    public String getStageName() {
        return "Km" + positionStartM / 1000 + "+" + positionStartM % 1000 + "-" + "Km" + positionEndM / 1000 + "+" + positionEndM % 1000;

    }

    @JsonIgnore
    @Override
    public int compareTo(Stage stage) {
        return stage.getPositionStartM() > this.positionStartM ? -1 : 1;
    }

}
