package elcom.com.its.notify.recevice.service.model.dto;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

public class Wards {
    private Long id;
    private String name;
    private String prefix;
    private Long provinceId;
    private Long districtId;

    public Wards(){}
    public Wards(Long id, String name, String prefix,Long districtId, Long provinceId){
        this.id = id; this.name = name; this.prefix = prefix; this.provinceId = provinceId; this.districtId = districtId;
    }
    public Wards(Long id){this.id = id;}

    public Long getId() { return id; }

    public void setId(Long id){this.id = id;}

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public String getPrefix(){return prefix;}

    public void setPrefix(String prefix){this.prefix = prefix;}

    public Long getProvinceId() { return provinceId; }

    public void setProvinceId(Long provinceId){this.provinceId = provinceId;}

    public Long getDistrictId() { return districtId; }

    public void setDistrictId(Long districtId){this.districtId = districtId;}
}
