package com.blackfat.encrypt.annotion;

import java.lang.annotation.*;


/**
 * 加密注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypt {
}
