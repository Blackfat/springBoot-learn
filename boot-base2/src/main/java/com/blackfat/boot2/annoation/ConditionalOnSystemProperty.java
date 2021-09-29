package com.blackfat.boot2.annoation;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-26 19:50
 * @since 1.0-SNAPSHOT
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnSystemPropertyCondition.class)
public @interface ConditionalOnSystemProperty {

    /**
     * @return System 属性名称
     */
    String name();

    /**
     * @return System 属性值
     */
    String value();
}
