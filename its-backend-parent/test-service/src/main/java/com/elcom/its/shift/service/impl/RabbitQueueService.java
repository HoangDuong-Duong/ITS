/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

/**
 *
 * @author Admin
 */
public interface RabbitQueueService {

    void addNewQueue(String queueName, String exchangeName, String routingKey);

    void addQueueToListener(String listenerId, String queueName);

    void removeQueueFromListener(String listenerId, String queueName);

    Boolean checkQueueExistOnListener(String listenerId, String queueName);
}
