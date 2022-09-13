package com.elcom.its.management.enums;

public enum PropertyType {
    BRIDGE(0, "Cầu"),
    DRAIN(1, "Cống"),
    TRAFFIC_SIGNS(2, "Biển báo"),
    TRENCH(3, "Rãnh hở"),
    FENCE(4, "Hàng rào"),
    ELECTRICAL_CABINETS(5, "Tủ điện"),
    AUXILIARY_EQUIPMENT(6, "Thiết bị"),
    UNKNOWN(7, "Chưa xác định");

    private int code;
    private String description;


    public int code() {
        return code;
    }
    public String description() {
        return description;
    }

    PropertyType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PropertyType of(String type){
        switch (type) {
            case "BRIDGE":
                return PropertyType.BRIDGE;
            case "DRAIN":
                return PropertyType.DRAIN;
            case "TRAFFIC_SIGNS":
                return PropertyType.TRAFFIC_SIGNS;
            case "FENCE":
                return PropertyType.FENCE;
            case "TRENCH":
                return PropertyType.TRENCH;
            case "ELECTRICAL_CABINETS":
                return PropertyType.ELECTRICAL_CABINETS;
            case "AUXILIARY_EQUIPMENT":
                return PropertyType.AUXILIARY_EQUIPMENT;
            default:
                return PropertyType.UNKNOWN;
        }
    }

    public static PropertyType of(int code) {
        PropertyType[] validFlags = PropertyType.values();
        for (PropertyType validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUnknown() {
        return code == UNKNOWN.code();
    }

}
