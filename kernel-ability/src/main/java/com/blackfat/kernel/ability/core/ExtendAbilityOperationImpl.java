package com.blackfat.kernel.ability.core;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ExtendAbilityOperationImpl {
    /**
     *
     * 实现编码
     */
    @AliasFor("value")
    String operationCode() default "";

    @AliasFor("operationCode")
    String value() default "";

    /**
     * 实现名称
     *
     */
    String name();

    /**
     * 描述
     *
     */
    String desc() default "";
}

