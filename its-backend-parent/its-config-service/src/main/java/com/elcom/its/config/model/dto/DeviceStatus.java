package com.elcom.its.config.model.dto;

/**
 * @author ducduongn
 */
public enum DeviceStatus {
    SETUP(0, "Setup"),
    STOP(1, "Stop"),
    RUNNING(2, "Running"),
    MAINTAIN(3, "Maintain"),
    UNKNOWN(4, "Unknown"),;

    public static DeviceStatus valueOf(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int code;

    private String description;

    DeviceStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    public boolean isUnknown() {
        return code == UNKNOWN.code();
    }

    public static DeviceStatus parse(int code) {
        DeviceStatus[] validFlags = DeviceStatus.values();
        for (DeviceStatus validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
