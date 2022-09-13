package elcom.com.its.notify.recevice.service.model.dto;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

public class Province implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String code;

    private String name;

    public Province() {
    }

    public Province(Long id) {
        this.id = id;
    }

    public Province(Long id, String code, String name){this.id = id; this.code = code; this.name = name;}

    public Long getId() { return id; }

    public void setId(Long id){this.id = id;}

    public String getCode(){return code;}

    public void setCode(String code){this.code = code;}

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

}
