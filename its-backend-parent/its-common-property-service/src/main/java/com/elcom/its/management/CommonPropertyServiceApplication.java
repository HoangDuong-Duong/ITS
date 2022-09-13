package com.elcom.its.management;

//import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
//@EnableScheduling
@EnableAsync
public class CommonPropertyServiceApplication {
    
    public static void main(String[] args) {
        // Fix lỗi "UDP failed setting ip_ttl | Method not implemented" khi start app trên Windows
        System.setProperty("java.net.preferIPv4Stack", "true");
        
        SpringApplication.run(CommonPropertyServiceApplication.class, args);
//        new SpringApplicationBuilder(ManagementServiceApplication.class).web(WebApplicationType.NONE).run(args);
    }
}
