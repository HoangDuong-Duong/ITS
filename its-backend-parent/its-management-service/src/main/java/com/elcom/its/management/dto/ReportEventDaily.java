package com.elcom.its.management.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportEventDaily {
    List<EventInfo> accidents;
    List<EventDTO> explosions;
    List<EventDTO> brokenVehicles;
    List<EventDTO> noEntry;
    List<EventInfo> eventOther;
    Long countMonth;
}
