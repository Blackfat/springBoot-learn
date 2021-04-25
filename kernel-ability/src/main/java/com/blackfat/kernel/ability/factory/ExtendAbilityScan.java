package com.blackfat.kernel.ability.factory;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AbilityScannerRegistrar.class)
public @interface ExtendAbilityScan {
    /**
     * 默认检查包
     *
     */
    String[] basePackages() default {};
}

