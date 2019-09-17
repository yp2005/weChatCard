package com.weChatCard;

import com.weChatCard.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.weChatCard.entities")
@EnableJpaRepositories(basePackages = "com.weChatCard.repositories")
@EnableAutoConfiguration
public class WeChatCardApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(WeChatCardApplication.class, args);
        SpringUtil.setApplicationContext(applicationContext);
    }
}
