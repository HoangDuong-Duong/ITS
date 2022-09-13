/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.model.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Admin
 */
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Data
public class NotifyRequestDTO {

    protected int type;

    protected String title;

    protected String content;

    protected String url;

    protected String objectType;

    protected String objectUuid;

    protected String userId;

    protected String siteId;

}
