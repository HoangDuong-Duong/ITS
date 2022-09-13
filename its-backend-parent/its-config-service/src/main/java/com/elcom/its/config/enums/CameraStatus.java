package com.elcom.its.config.enums;

/**
 * @author hanh
 */
public enum CameraStatus {
    SETUP(1, "Setup"),
    STOP(2, "Stop"),
    RUNNING(3, "Running"),
    MAINTAIN(4, "Maintain"),
    UNKNOWN(5, "Unknown");

    private int code;
    private String description;

    CameraStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    public boolean isSetup() {
        return this.code == SETUP.code;
    }

    public boolean isStop() {
        return this.code == STOP.code;
    }

    public boolean isRunning() {
        return this.code == RUNNING.code;
    }

    public boolean isMaintain() {
        return this.code == MAINTAIN.code;
    }

    public static CameraStatus parse(int code) {
        CameraStatus[] validFlags = CameraStatus.values();
        for (CameraStatus validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
