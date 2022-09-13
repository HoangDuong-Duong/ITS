/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

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
    private AmqpTemplate amqpTemplate;

    @Autowired
    @Qualifier("directAutoDeleteQueue")
    private Queue directAutoDeleteQueue;

    @Autowired
    private RabbitListenerEndpointRegistry registry;

    public String callRpcService(String exchangeName, String queueName, String key, String msg) {
        logger.info("callRpcService - exchangeName: {}, queueName: {}, key : {} , msg {}",
                exchangeName, queueName, key,msg);
        //Queue
        Queue queue = new Queue(queueName);
        addQueue(queue);

        DirectExchange exchange = new DirectExchange(exchangeName);
        addExchange(exchange);

        addBinding(queue, exchange, key);

        //Send msg
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        Message message;
        try {
            message = new Message(msg.getBytes("UTF-8"), messageProperties);
            return (String) amqpTemplate.convertSendAndReceive(exchangeName, key, message);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.toString());
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

        //Send msg
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        try {
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            amqpTemplate.convertAndSend(exchangeName, key, message);
        } catch (AmqpException ex) {
            ex.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.toString());
        }
        return true;
    }

    public boolean callWorkerService(String queueName, String msg) {
        logger.info("callWorkerService - queueName : {}", queueName);
        //Queue
        //Queue queue = new Queue(queueName);
        //addQueue(queue);

        //Send msg
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        try {
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            amqpTemplate.convertAndSend(queueName, message);
        } catch (AmqpException ex) {
            ex.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.toString());
        }
        return true;
    }

    public String createQueue(String queueName) {
        try {
            Properties properties = amqpAdmin.getQueueProperties(queueName);
            String result = null;
            if (properties == null) {
                result = amqpAdmin.declareQueue(new Queue(queueName));
                SimpleMessageListenerContainer listener = (SimpleMessageListenerContainer) registry.getListenerContainer("process");
                if(listener != null){
                    listener.addQueueNames(queueName);
                }
            }
            return result;
        } catch(Exception ex){
            logger.error(ex.toString());
            return null;
        }
    }

    public boolean deleteQueue(String queueName) {
        boolean result = amqpAdmin.deleteQueue(queueName);
        SimpleMessageListenerContainer listener = (SimpleMessageListenerContainer) registry.getListenerContainer("process");
        if(listener != null){
            listener.removeQueueNames(queueName);
        }
        return result;
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
