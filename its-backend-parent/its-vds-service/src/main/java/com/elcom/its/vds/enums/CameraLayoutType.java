/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.enums;

/**
 *
 * @author Admin
 */
public enum CameraLayoutType {
    LAYOUT_XPGT(1, "Layout giám sát giao thông"),
    LAYOUT_GSAN(2, "Layout giám sát an ninh"),
    UNKNOWN(0, "Unknown");

    private final int layoutType;
    private final String description;

    CameraLayoutType(int layoutType, String description) {
        this.layoutType = layoutType;
        this.description = description;
    }

    public int layoutType() {
        return layoutType;
    }

    public String description() {
        return description;
    }

    public boolean isXPGTType() {
        return this.layoutType == LAYOUT_XPGT.layoutType;
    }

    public boolean isGSANType() {
        return this.layoutType == LAYOUT_GSAN.layoutType;
    }

    public static CameraLayoutType parse(int layoutType) {
        CameraLayoutType[] validFlags = CameraLayoutType.values();
        for (CameraLayoutType validFlag : validFlags) {
            if (validFlag.layoutType() == layoutType) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
