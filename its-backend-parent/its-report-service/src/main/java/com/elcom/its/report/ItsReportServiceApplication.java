package com.elcom.its.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableCaching
public class ItsReportServiceApplication {
    private Logger logger = LoggerFactory.getLogger(ItsReportServiceApplication.class);

    public static void main(String[] args) {
//         Fix lỗi "UDP failed setting ip_ttl | Method not implemented" khi start app trên Windows
        System.setProperty("java.net.preferIPv4Stack", "true");
        SpringApplication.run(ItsReportServiceApplication.class, args);

//        new SpringApplicationBuilder(ItsReportServiceApplication.class).web(WebApplicationType.NONE).run(args);
    }

}
