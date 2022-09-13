package elcom.com.its.notify.recevice.service.model.dto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
public class District {
    private Long provinceId;
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String prefix;

    public District() {
    }

    public District(Long id, String name, String prefix, Long provinceId) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.provinceId = provinceId;
    }

    public District(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

   
}
