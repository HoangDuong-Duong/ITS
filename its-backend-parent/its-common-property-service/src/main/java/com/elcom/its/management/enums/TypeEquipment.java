package com.elcom.its.management.enums;

/**
 * @author hanh
 */
public enum TypeEquipment {
    LIGHTNING_CONDUCTOR(0, "Chống sét"),
    SOLAR_BATTERY(1, "Pin mặt trời"),
    UPS(2, "Bộ lưu điện"),
    SCREEN(3, "Tường màn hình"),
    UNKNOW(4, "Chưa xác định")
    ;

    private int code;
    private String description;

    TypeEquipment(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TypeEquipment of(String type){
        switch (type) {
            case "LIGHTNING_CONDUCTOR":
                return TypeEquipment.LIGHTNING_CONDUCTOR;
            case "SOLAR_BATTERY":
                return TypeEquipment.SOLAR_BATTERY;
            case "UPS":
                return TypeEquipment.UPS;
            case "SCREEN":
                return TypeEquipment.SCREEN;
            default: return TypeEquipment.UNKNOW;
        }
    }

    public int code() {
        return code;
    }
    public String description() {
        return description;
    }

    public static TypeEquipment of(int code) {
        TypeEquipment[] validFlags = TypeEquipment.values();
        for (TypeEquipment validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }

        return UNKNOW;
    }


}
