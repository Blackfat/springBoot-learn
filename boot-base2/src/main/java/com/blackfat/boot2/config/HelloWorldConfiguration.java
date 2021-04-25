package com.blackfat.boot2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-25 14:40
 * @since 1.0-SNAPSHOT
 */
@Configuration
public class HelloWorldConfiguration {

    @Bean
    public String helloWorld() { // 创建名为"helloWorld" String 类型的Bean
        return "Hello,World";
    }
}
