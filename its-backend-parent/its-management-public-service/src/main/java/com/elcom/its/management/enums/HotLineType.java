package com.elcom.its.management.enums;

/**
 * @author hanh
 */
public enum HotLineType {

    SECURITY(0, "Sự cố"),
    TRAFFIC_INSTRUCTIONS(1, "Hướng dẫn giao thông"),
    SERVICE_QUALITY(2, "Chất lượng dịch vụ ĐCT"),
    OTHERINFO(3, "Các thông tin khác"),
    UNKNOWN(4, "Unknown");

    public static HotLineType valueOf(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int code;

    private String description;

    HotLineType(int code, String description) {
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

    public static HotLineType parse(int code) {
        HotLineType[] validFlags = HotLineType.values();
        for (HotLineType validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
