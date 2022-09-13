/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.enums.DataStatus;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Basic;
import javax.persistence.Column;

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
