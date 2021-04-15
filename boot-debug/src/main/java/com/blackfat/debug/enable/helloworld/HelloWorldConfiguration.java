package com.blackfat.debug.enable.helloworld;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-05-18 06:10
 * @since 1.0-SNAPSHOT
 */
@Configuration
public class HelloWorldConfiguration {


    @Bean
    public String helloWorld() {
        return "Hello,World";
    }


}
