package com.blackfat.boot2.config;

import com.blackfat.boot2.annoation.ConditionalOnSystemProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-26 20:40
 * @since 1.0-SNAPSHOT
 */
@Configuration
public class ConditionalMessageConfiguration {

    @ConditionalOnSystemProperty(name = "language", value = "Chinese")
    @Bean("message") // Bean 名称 "message" 的中文消息
    public String chineseMessage() {
        return "你好，世界";
    }

    @ConditionalOnSystemProperty(name = "language", value = "English")
    @Bean("message") // Bean 名称 "message" 的英文消息
    public String englishMessage() {
        return "Hello,World";
    }
}
