package com.blackfat.boot2.configuratin.processor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-05-08 14:14
 * @since 1.0-SNAPSHOT
 */
@EnableConfigurationProperties(value = CustomProperties.class)
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {

    /**
     * title
     */
    private String title;

    /**
     * content
     */
    private String content;

    private Integer number = 9000;

    private Boolean enbale;
}
