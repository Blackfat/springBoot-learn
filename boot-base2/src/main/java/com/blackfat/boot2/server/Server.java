package com.blackfat.boot2.server;

/**
 * @author wangfeiyang
 * @Description
 * @create 2019-06-26 14:10
 * @since 1.0-SNAPSHOT
 */
public interface Server {

    /**
     * 启动服务器
     */
    void start();

    /**
     * 关闭服务器
     */
    void stop();

    /**
     * 服务器类型
     */
    enum Type {

        HTTP, // HTTP 服务器
        FTP   // FTP  服务器
    }
}
