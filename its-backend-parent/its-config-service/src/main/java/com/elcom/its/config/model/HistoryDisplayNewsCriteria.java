package com.elcom.its.config.model;

import com.elcom.its.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Data
public class HistoryDisplayNewsCriteria implements Serializable {

    String fromDate; //yyyy-MM-dd HH24:mm:ss
    String toDate; //yyyy-MM-dd HH24:mm:ss
    String deviceId;
    String keyword;
    @JsonIgnore
    public LocalDateTime getFromDateTime() {
        LocalDateTime localDateTime;
        try {
            localDateTime = DateUtils.parse(this.fromDate);
        } catch (Exception e) {
            System.out.println("Can not parse fromTime[" + this.fromDate + "]");
            System.out.println("Today's midnight applied.");
            localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        }

        return localDateTime;
    }

    @JsonIgnore
    public LocalDateTime getToDateTime() {
        LocalDateTime localDateTime;
        try {
            localDateTime = DateUtils.parse(this.toDate);
        } catch (Exception e) {
            System.out.println("Can not parse toTime[" + this.toDate + "]");
            System.out.println("so latest of today will be used.");
            localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        }

        return localDateTime;
    }
}
