package elcom.com.its.notify.recevice.service.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ViolationListResponse {
    private List<ViolationDTO> data;
    private String message;
    private int status;
    private int total;
}
