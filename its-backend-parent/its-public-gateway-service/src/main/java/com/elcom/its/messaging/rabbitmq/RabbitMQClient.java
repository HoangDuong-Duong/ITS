/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.messaging.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class RabbitMQClient {

    private final Logger logger = LoggerFactory.getLogger(RabbitMQClient.class);

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    @Qualifier("directAutoDeleteQueue")
    private Queue directAutoDeleteQueue;

    public String callRpcService(String exchangeName, String queueName, String key, String msg) {
        try {
            logger.info("callRpcService - exchangeName: {}, queueName: {}, key : {}, msg: {}",
                    exchangeName, queueName, key, msg);
//            Queue
//            Queue queue = new Queue(queueName);
//            addQueue(queue);
//            Exchange
//            DirectExchange exchange = new DirectExchange(exchangeName);
//            addExchange(exchange);
//            Binding
//            addBinding(queue, exchange, key);

            //Send msg
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            Object obj = rabbitTemplate.convertSendAndReceive(exchangeName, key, message);
            return (String) obj;
        } catch (Exception ex) {
            logger.error("callRpcService Exception >>> " + ex.toString());
            ex.printStackTrace();
        }
        return null;
    }

    public boolean callPublishService(String exchangeName, String key, String msg) {
        logger.info("callPublishService - exchangeName: {}, key : {}", exchangeName, key);
        //Exchange
        //DirectExchange exchange = new DirectExchange(exchangeName);
        //addExchange(exchange);
        //Binding
        //addBinding(directAutoDeleteQueue, exchange, key);

        try {
            //Send msg
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            rabbitTemplate.convertAndSend(exchangeName, key, message);
        } catch (Exception ex) {
            logger.error("callPublishService Exception >>> " + ex.toString());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean callWorkerService(String queueName, String msg) {
        logger.info("callWorkerService - queueName : {}", queueName);
        //Queue
        Queue queue = new Queue(queueName);
        addQueue(queue);

        try {
            //Send msg
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            rabbitTemplate.convertAndSend(queueName, message);
        } catch (Exception ex) {
            logger.error("callWorkerService Exception >>> " + ex.toString());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private String addQueue(Queue queue) {
        return amqpAdmin.declareQueue(queue);
    }

    private void addExchange(AbstractExchange exchange) {
        amqpAdmin.declareExchange(exchange);
    }

    private void addBinding(Queue queue, DirectExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        amqpAdmin.declareBinding(binding);
    }
}
