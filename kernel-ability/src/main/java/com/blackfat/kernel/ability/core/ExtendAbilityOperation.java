package com.blackfat.kernel.ability.core;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtendAbilityOperation {
    /**
     * 扩展名称
     *
     */
    String name();
}
