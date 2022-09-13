

package com.elcom.its.vds.model.dto;



import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author thainguyen
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
 public class ABACResponseDTO implements Serializable {
    private Boolean status;
    private Boolean admin;
    private String type; // ALLOW DENY
    private String description;

    public ABACResponseDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("status")) {
                this.status = (Boolean) map.get("status");
            }
            if (map.containsKey("type")) {
                this.type = (String) map.get("type");
            }
            if (map.containsKey("description")) {
                this.description = (String) map.get("description");
            }
        }
    }
    @Override
    public String toString() {
        return "ABACResponseDTO{" + "status=" + status + ", type=" + type + ", description=" + description + '}';
    }
}
