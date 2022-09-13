package elcom.com.its.notify.recevice.service.enums;

public enum EventType {
    VIOLATION(0, "Sự kiện vi phạm giao thông"),
    SECURITY(1, "Sự kiện an ninh"),
    MANUAL_INPUT(2, "Sự kiện nhập thủ công"),
    UNKNOWN(3, "Sự kiện chưa được định nghĩa");

    private int code;
    private String description;

    public int code() {
        return code;
    }
    public String description() {
        return description;
    }

    EventType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static EventType of(String type){
        switch (type) {
            case "VIOLATION":
                return EventType.VIOLATION;
            case "SECURITY":
                return EventType.SECURITY;
            case "MANUAL_INPUT":
                return EventType.MANUAL_INPUT;
            default:
                return EventType.UNKNOWN;
        }
    }

    public static EventType of(int code) {
        EventType[] validFlags = EventType.values();
        for (EventType validFlag : validFlags) {
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

    public boolean isViolation() {
        return code == VIOLATION.code();
    }

    public boolean isSecurity() {
        return code == SECURITY.code();
    }

    public boolean isManualInput() {
        return code == MANUAL_INPUT.code();
    }

    public boolean isUnknown() {
        return code == UNKNOWN.code();
    }
}
