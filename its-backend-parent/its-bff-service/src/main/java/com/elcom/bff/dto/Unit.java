package com.elcom.bff.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Unit {
    private String uuid;

    private String code;

    private String name;

    private String address;

    private String phone;

    private String email;

    private String description;

    private String lisOfStage;

    private String listOfJob;
}
