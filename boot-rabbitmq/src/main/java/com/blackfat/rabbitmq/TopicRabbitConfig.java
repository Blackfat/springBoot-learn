package com.blackfat.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/10/29-15:17
 */
@Configuration
public class TopicRabbitConfig {

    final static String message1 = "topic.message1";

    final static String message2 = "topic.message2";

    @Bean
    public Queue queueMessage1() {
        return new Queue(TopicRabbitConfig.message1);
    }

    @Bean
    public Queue queueMessage2() {
        return new Queue(TopicRabbitConfig.message2);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("exchange");
    }

    @Bean
    Binding bindingMessage1(Queue queueMessage1, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage1).to(exchange).with("topic.message");
    }

    @Bean
    Binding bindingMessage2(Queue queueMessage2, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage2).to(exchange).with("topic.#");
    }

}
