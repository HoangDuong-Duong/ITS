package elcom.com.its.notify.recevice.service.enums;

public enum RecordDeleteStatus {
    NOT_DELETED(0, "Chưa xóa"),
    DELETED(1, "Đã xóa"),
    UNKNOWN(9, "Không xác định");

    private int code;
    private String description;

    RecordDeleteStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    public boolean isNotDeleted() {
        return code == NOT_DELETED.code();
    }
    public boolean isDeleted() {
        return code == DELETED.code();
    }

    public static RecordDeleteStatus of(int code) {
        RecordDeleteStatus[] validFlags = RecordDeleteStatus.values();
        for (RecordDeleteStatus validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }

        return UNKNOWN;
    }
}
