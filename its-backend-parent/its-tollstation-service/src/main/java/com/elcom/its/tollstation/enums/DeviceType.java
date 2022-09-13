/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Admin
 */
public enum DeviceType {
    VMS("VMS", "biển báo VMS"),
    CAMERA("CAMERA", "Camera"),
    CONDUIT("CONDUIT", "Cống"),
    BOARD("BOARD", "Biển báo"),
    TRENCH("TRENCH", "Rãnh hở"),
    HURDLE("HURDLE", "Hàng rào"),
    ELECTRIC("ELECTRIC", "Tủ điện"),
    FURTHER("FURTHER", "Thiết bị phụ trợ"),
    BRIDGE("BRIDGE", "Cầu");
    private String code;
    private String description;

    DeviceType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Set<String> getListFmsDeviceType() {
        return new HashSet<>(Arrays.asList(
                CONDUIT.code,
                BOARD.code,
                TRENCH.code,
                HURDLE.code,
                ELECTRIC.code,
                FURTHER.code,
                BRIDGE.code
        ));
    }
}
