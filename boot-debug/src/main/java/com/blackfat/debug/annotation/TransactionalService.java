package com.blackfat.debug.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-22 10:29
 * @since 1.0-SNAPSHOT
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional
public @interface TransactionalService {

    @AliasFor(attribute = "value")
    String name() default "";


    @AliasFor("name")
    String value() default "";
}
