/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.model.dto;

import elcom.com.its.notify.recevice.service.enums.DataStatus;
import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class Category implements Serializable {

    private DataStatus status = DataStatus.ENABLE;

    private static final long serialVersionUID = 1L;
    private Long id;
    private String catType;
    private String catName;
    private String code;
    private String description;
    private String value;
    private String name;
    private short orderInType;

    public Category() {
    }

    public Category(Long id) {
        this.id = id;
    }

    public Category(Long id, String catType, String catName, String code, String name, short orderInType) {
        this.id = id;
        this.catType = catType;
        this.catName = catName;
        this.code = code;
        this.name = name;
        this.orderInType = orderInType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatType() {
        return catType;
    }

    public void setCatType(String catType) {
        this.catType = catType;
    }

    public String getCatName() { return catName; }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getOrderInType() {
        return orderInType;
    }

    public void setOrderInType(short orderInType) {
        this.orderInType = orderInType;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.itscore.dto.category[ id=" + id + " ]";
    }

    public DataStatus getStatus() {
        return status;
    }

    public void setStatus(DataStatus status) {
        this.status = status;
    }

}
