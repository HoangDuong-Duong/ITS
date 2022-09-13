package com.elcom.its.management.dto;

import com.elcom.its.management.model.Job;
import lombok.Data;

@Data
public class EventJobDTO {
    private EventDTO event;
    private Job job;

    public EventJobDTO(EventDTO event, Job job) {
        this.event = event;
        this.job = job;
    }
}
