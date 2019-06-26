package com.blackfat.boot2.server;

import org.springframework.stereotype.Component;

/**
 * @author wangfeiyang
 * @Description
 * @create 2019-06-26 14:10
 * @since 1.0-SNAPSHOT
 */
@Component// 根据 ImportSelector 的契约，请确保是实现为 Spring 组件
public class HttpServer implements Server {

    @Override
    public void start() {
        System.out.println("HTTP 服务器启动中...");
    }

    @Override
    public void stop() {
        System.out.println("HTTP 服务器关闭中...");
    }
}
