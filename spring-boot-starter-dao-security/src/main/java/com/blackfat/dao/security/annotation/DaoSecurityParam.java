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
public @interface DaoSecurityParam {
    /**
     * 字段名
     * @return
     */
    String value();

    /**
     * maxLength为String类型最大支持的长度
     * @return
     */
    int maxLength() default 0;

    /**
     * 字段生成的消息摘要字段名
     * @return
     */
    String searchName() default "";
}

