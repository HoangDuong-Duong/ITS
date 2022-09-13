//package elcom.com.its.notify.recevice.service.kafka;
//
//import elcom.com.its.notify.recevice.service.service.QueueNameService;
//import org.apache.kafka.clients.admin.AdminClientConfig;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.KafkaListenerContainerFactory;
//import org.springframework.kafka.config.TopicBuilder;
//import org.springframework.kafka.core.*;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Configuration
//public class KafkaConfig {
//    @Value("${kafka.bootstrap.servers}")
//    private String bootstrapServers;
//
//    @Value("${kafka.consumergroup}")
//    private String consumerGroup;
//
//    @Value("${dev.group}")
//    private boolean check;
//
//    @Value("${kafka.partition}")
//    private int partition;
//
//    @Autowired
//    private QueueNameService queueNameService;
//
//    public List<String> getControlQueueName() {
//        return queueNameService.get();
//    }
//
//    @Bean
//    public Map<String, Object> producerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        // list of host:port pairs used for establishing the initial connections to the Kakfa cluster
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return props;
//    }
//
//    @Bean
//    public Map<String, Object> consumerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        if (check) {
//            props.put(ConsumerConfig.GROUP_ID_CONFIG, new Date() + "dev");
//        } else {
//            props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
//        }
//        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
//        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
//        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 15000);
//        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 15000);
//        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 14000);
//
//        return props;
//    }
//
//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigs());
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    @Bean
//    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
//        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
//        threadPoolTaskScheduler.setPoolSize(10);
//        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
//        return threadPoolTaskScheduler;
//    }
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new StringDeserializer());
//    }
//
//    @Bean
//    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        factory.setReplyTemplate(kafkaTemplate());
//        return factory;
//    }
//
//    @Bean
//    public KafkaAdmin kafkaAdmin()
//    {
//        Map<String, Object> configs = new HashMap<>();
//        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        return new KafkaAdmin(configs);
//    }
//}
