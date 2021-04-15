package com.blackfat.encrypt.auto;

import com.blackfat.encrypt.advice.EncryptRequestBodyAdvice;
import com.blackfat.encrypt.advice.EncryptResponseBodyAdvice;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author wangfeiyang
 * @desc 加解密自动配置
 * @create 2018/11/26-14:48
 */
@Configuration
@Component
@EnableAutoConfiguration
//提供一种方便的方式来将带有@ConfigurationProperties注解的类注入为Spring容器的Bean
@EnableConfigurationProperties(EncryptProperties.class)
public class EncryptAutoConfiguration {


    /**
     * 配置请求解密
     *
     * @return
     */
    @Bean
    public EncryptResponseBodyAdvice encryptResponseBodyAdvice() {
        return new EncryptResponseBodyAdvice();
    }

    /**
     * 配置请求加密
     *
     * @return
     */
    @Bean
    public EncryptRequestBodyAdvice encryptRequestBodyAdvice() {
        return new EncryptRequestBodyAdvice();
    }

}
