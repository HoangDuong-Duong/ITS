/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
@SuperBuilder
@Setter
@Getter
@RequiredArgsConstructor
@ToString
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category implements Serializable {
    private Long id;

    private String catType;

    private String catName;

    private String code;

    private String description;

    private String value;

    private String name;

    private Short orderInType;
}
