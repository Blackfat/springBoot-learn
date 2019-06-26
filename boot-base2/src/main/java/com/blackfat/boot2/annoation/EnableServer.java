package com.blackfat.boot2.annoation;

import com.blackfat.boot2.server.Server;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author wangfeiyang
 * @Description
 * @create 2019-06-26 14:08
 * @since 1.0-SNAPSHOT
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ServerImportSelector.class)
public @interface EnableServer {

    /**
     * 设置服务器类型
     * @return non-null
     */
    Server.Type type();
}
