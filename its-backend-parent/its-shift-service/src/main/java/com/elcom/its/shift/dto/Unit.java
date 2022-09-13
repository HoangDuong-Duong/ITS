/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.dto;

/**
 *
 * @author Admin
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Admin
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit implements Serializable {
    private String uuid;
    private String code;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private String lisOfStage;
    private Object listOfJob;
}
