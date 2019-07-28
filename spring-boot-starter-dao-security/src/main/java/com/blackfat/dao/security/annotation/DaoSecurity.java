package com.blackfat.dao.security.annotation;

import java.lang.annotation.*;

/**
 * @author wangfeiyang
 * @Description
 * @create 2019-07-28 21:37
 * @since 1.0-SNAPSHOT
 */

@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DaoSecurity {
    /**
     * 需要加密解密的类
     * @return
     */
    Class value();

    /**
     * 需要加密的字段
     * @return
     */
    DaoSecurityParam[] params() default {};

    /**
     * 密钥所在的字段
     * @return
     */
    String keyName();
}
