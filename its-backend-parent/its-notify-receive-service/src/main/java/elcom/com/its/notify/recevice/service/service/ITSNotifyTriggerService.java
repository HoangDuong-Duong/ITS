/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.service;

import elcom.com.its.notify.recevice.service.model.dto.NotifyTrigger;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ITSNotifyTriggerService {

    NotifyTrigger save(NotifyTrigger notifyTrigger);

    NotifyTrigger update(NotifyTrigger notifyTrigger);

    boolean remove(String notifyTriggerId);
    
    boolean removeList(List<String> notifyTriggerIds);
}
