package com.blackfat.dao.security.annotation;

import java.lang.annotation.*;

/**
 * @author wangfeiyang
 * @Description
 * @create 2019-07-28 21:38
 * @since 1.0-SNAPSHOT
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DaoSecurityDecrypt {
}
