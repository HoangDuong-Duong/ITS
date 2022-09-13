package com.elcom.its.tollstation.enums;

/**
 * @author hanh
 */
public enum EventStatus {
    WAIT(0, "Đang chờ"),
    NOT_SEEN(1, "Chưa xem"),
    VERIFICATION(2, "Xác minh sự kiện"),
    PROCESSING(3, "Đang xử lý"),
    PROCESSED(4, "Đã xử lý"),
    INCORRECT(5, "Báo sai, bỏ qua"),
    UNKNOW(6,"Không xác định")
    ;

    private int code;
    private String description;

    EventStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static EventStatus of(String type){
        switch (type) {
            case "WAIT":
                return EventStatus.WAIT;
            case "NOT_SEEN":
                return EventStatus.NOT_SEEN;
            case "VERIFICATION":
                return EventStatus.VERIFICATION;
            case "PROCESSING":
                return EventStatus.PROCESSING;
            case "PROCESSED":
                return EventStatus.PROCESSED;
            case "INCORRECT":
                return EventStatus.INCORRECT;
            default: return EventStatus.UNKNOW;
        }
    }

    public int code() {
        return code;
    }
    public String description() {
        return description;
    }

    public boolean isWaitToProcess() {
        return code == WAIT.code();
    }
    
    

    public static EventStatus of(int code) {
        EventStatus[] validFlags = EventStatus.values();
        for (EventStatus validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }

        return UNKNOW;
    }


}
