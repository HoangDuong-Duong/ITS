package elcom.com.its.notify.recevice.service.model.dto;

public class GroupReceiverDTO {
    private String id;
    private String name;
    private String code;
    private String type = "GROUP";

    public GroupReceiverDTO() {
    }

    public GroupReceiverDTO(ReceiverDTO dto) {
        if (dto != null) {
            this.id = dto.getId();
            this.name = dto.getName();
            this.type = dto.getType();
            this.code = dto.getCode();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        if (type == null) {
            return "GROUP";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
