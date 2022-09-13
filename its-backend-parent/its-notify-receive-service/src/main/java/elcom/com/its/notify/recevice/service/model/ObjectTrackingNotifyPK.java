package elcom.com.its.notify.recevice.service.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Admin
 */
public class ObjectTrackingNotifyPK implements Serializable {

    private String id;
    private Date createdDate;

    public ObjectTrackingNotifyPK() {
    }

    public ObjectTrackingNotifyPK(String id, Date createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        hash += (createdDate != null ? createdDate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjectTrackingNotifyPK)) {
            return false;
        }
        ObjectTrackingNotifyPK other = (ObjectTrackingNotifyPK) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        if ((this.createdDate == null && other.createdDate != null) || (this.createdDate != null && !this.createdDate.equals(other.createdDate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.notify.recieve.model.ObjectTrackingNotifyPK[ id=" + id + ", createdDate=" + createdDate + " ]";
    }

}
