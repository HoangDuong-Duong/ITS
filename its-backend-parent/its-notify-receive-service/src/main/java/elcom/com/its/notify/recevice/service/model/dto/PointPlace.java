package elcom.com.its.notify.recevice.service.model.dto;

import lombok.Data;

@Data
public class PointPlace {
    private double longitude;
    private double latitude;
    private String placeId;
    private String directionCode;
}
