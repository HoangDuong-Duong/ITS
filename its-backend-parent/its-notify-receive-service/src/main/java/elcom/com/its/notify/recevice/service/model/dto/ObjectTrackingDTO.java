package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Admin
 */
public class ObjectTrackingDTO implements Serializable {

    private String model;
    private String id;
    private String infoObject;
    private String reason;
    private String alertChannel;
    private String alertReceiver;
    private String queue;
    private String description;
    private Date createDate;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private String createBy;
    private String note;
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date modifiedDate;
    private String modifiedBy;
    private String objectType;
    private String objectImages;
    private String indentification;

    public ObjectTrackingDTO() {
    }

    public ObjectTrackingDTO(String id) {
        this.id = id;
    }

    public ObjectTrackingDTO(String id, String queue) {
        this.id = id;
        this.queue = queue;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfoObject() {
        return infoObject;
    }

    public void setInfoObject(String infoObject) {
        this.infoObject = infoObject;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAlertChannel() {
        return alertChannel;
    }

    public void setAlertChannel(String alertChannel) {
        this.alertChannel = alertChannel;
    }

    public String getAlertReceiver() {
        return alertReceiver;
    }

    public void setAlertReceiver(String alertReceiver) {
        this.alertReceiver = alertReceiver;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getObjectImages() {
        return objectImages;
    }

    public void setObjectImages(String objectImages) {
        this.objectImages = objectImages;
    }

    public String getIndentification() {
        return indentification;
    }

    public void setIndentification(String indentification) {
        this.indentification = indentification;
    }

    @Transient
    @JsonIgnore
    private List<UserReceiverDTO> users;

    @Transient
    @JsonIgnore
    private List<GroupReceiverDTO> groups;

    public List<UserReceiverDTO> getUsers() {
        if (alertReceiver != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mapper.setDateFormat(df);
                List<ReceiverDTO> receiverList = mapper.readValue(alertReceiver, new TypeReference<List<ReceiverDTO>>() {
                });
                List<UserReceiverDTO> userList = null;
                if (receiverList != null && !receiverList.isEmpty()) {
                    userList = new ArrayList<>();
                    for (ReceiverDTO dto : receiverList) {
                        if ("USER".equals(dto.getType())) {
                            userList.add(new UserReceiverDTO(dto));
                        }
                    }
                }
                return userList;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return users;
    }

    public void setUsers(List<UserReceiverDTO> users) {
        this.users = users;
    }

    public List<GroupReceiverDTO> getGroups() {
        if (alertReceiver != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mapper.setDateFormat(df);
                List<ReceiverDTO> receiverList = mapper.readValue(alertReceiver, new TypeReference<List<ReceiverDTO>>() {
                });
                List<GroupReceiverDTO> groupList = null;
                if (receiverList != null && !receiverList.isEmpty()) {
                    groupList = new ArrayList<>();
                    for (ReceiverDTO dto : receiverList) {
                        if ("GROUP".equals(dto.getType())) {
                            groupList.add(new GroupReceiverDTO(dto));
                        }
                    }
                }
                return groupList;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        return groups;
    }

    public void setGroups(List<GroupReceiverDTO> groups) {
        this.groups = groups;
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
        if (!(object instanceof ObjectTrackingDTO)) {
            return false;
        }
        ObjectTrackingDTO other = (ObjectTrackingDTO) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.elcom.itscore.data.model.ObjectTracking[ id=" + id + " ]";
    }

}

