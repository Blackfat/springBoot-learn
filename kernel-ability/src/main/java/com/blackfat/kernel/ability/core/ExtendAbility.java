package com.blackfat.kernel.ability.core;


import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ExtendAbility {
    /**
     * 扩展名称
     *
     */
    String name();

    /**
     * 扩展的业务模块
     */
    String model();

    /**
     *
     * 流程名
     */
    String process();
}
