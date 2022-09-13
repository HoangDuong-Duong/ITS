package elcom.com.its.notify.recevice.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ItsNotifyReceiveServiceApplication {

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        SpringApplication.run(ItsNotifyReceiveServiceApplication.class, args);
//        new SpringApplicationBuilder(ItsNotifyReceiveServiceApplication.class).web(WebApplicationType.NONE).run(args);
    }

}
