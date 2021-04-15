package com.blackfat.encrypt.annotion;

import com.blackfat.encrypt.auto.EncryptAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/*
 * 启用加密Starter
 * */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EncryptAutoConfiguration.class})// 通过导入的方式实现把实例加入springIOC容器中
public @interface EnableEncrypt {
}
