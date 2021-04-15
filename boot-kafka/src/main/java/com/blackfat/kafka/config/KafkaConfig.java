package com.blackfat.kafka.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/11/29-11:38
 */
@Configuration
public class KafkaConfig {

    @Bean
    public Producer<String, String> build() {
        Properties properties = new Properties();

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);

        return producer;
    }
}
