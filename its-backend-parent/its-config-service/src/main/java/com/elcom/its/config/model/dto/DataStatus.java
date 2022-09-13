package com.elcom.its.config.model.dto;

public enum DataStatus {
    DISABLE(0, "Disable"),
    ENABLE(1, "Enable"),
    DELETE(2, "Delete"),
    UNKNOWN(3, "Unknown"),;

    public static DataStatus valueOf(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int code;

    private String description;

    DataStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    public boolean isDisable() {
        return code == DISABLE.code();
    }

    public boolean isEnable() {
        return code == ENABLE.code();
    }

    public boolean isDelete() {
        return code == DELETE.code();
    }

    public boolean isUnknown() {
        return code == UNKNOWN.code();
    }

    public static DataStatus of(int code) {
        DataStatus[] validFlags = DataStatus.values();
        for (DataStatus validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
