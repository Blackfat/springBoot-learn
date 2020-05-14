package com.blackfat.debug.apidesign.apiversion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-05-08 16:22
 * @since 1.0-SNAPSHOT
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface APIVersion {
    String[] value();
}
