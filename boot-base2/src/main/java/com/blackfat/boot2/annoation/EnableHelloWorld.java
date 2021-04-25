package com.blackfat.boot2.annoation;

import com.blackfat.boot2.config.HelloWorldConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-25 14:42
 * @since 1.0-SNAPSHOT
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HelloWorldConfiguration.class)
public @interface EnableHelloWorld {
}
