package com.blackfat.boot2.annoation;

import com.blackfat.boot2.server.FTPServer;
import com.blackfat.boot2.server.HttpServer;
import com.blackfat.boot2.server.Server;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author wangfeiyang
 * @Description 服务器 {@link ImportSelector} 实现
 * @create 2019-06-26 14:13
 * @since 1.0-SNAPSHOT
 */
public class ServerImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        // 读取 EnableServer 中的所有的属性方法，本例中仅有 type() 属性方法
        // 其中 key 为 属性方法的名称，value 为属性方法返回对象
        Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(EnableServer.class.getName());
        // 获取名为"type" 的属相方法，并且强制转化成 Server.Type 类型
        Server.Type type = (Server.Type) annotationAttributes.get("type");
        // 导入的类名称数组
        String[] importClassNames = new String[0];
        switch (type) {
            case HTTP: // 当设置 HTTP 服务器类型时，返回 HttpServer 组件
                importClassNames = new String[]{HttpServer.class.getName()};
                break;
            case FTP: //  当设置 FTP  服务器类型时，返回 FTPServer  组件
                importClassNames = new String[]{FTPServer.class.getName()};
                break;
        }
        return importClassNames;
    }
}
