package com.blackfat.debug.enable.helloworld;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-05-18 06:15
 * @since 1.0-SNAPSHOT
 */
@Configuration
@EnableHelloWorld
public class HelloWorldBootStrap {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(HelloWorldConfiguration.class);
        context.refresh();
        String helloWorld = context.getBean("helloWorld", String.class);
        System.out.println(helloWorld);
        context.close();
    }
}
