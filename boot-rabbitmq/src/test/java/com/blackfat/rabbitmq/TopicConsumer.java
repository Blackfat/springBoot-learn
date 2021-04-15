package com.blackfat.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/9/25-15:19
 */
public class TopicConsumer {

    @Test
    public void testBasicConsumer1() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("101.132.177.27");
        factory.setPort(AMQP.PROTOCOL.PORT);
        factory.setUsername("blackfat");
        factory.setPassword("123456");

        // socket连接，封装了socket协议部分逻辑
        Connection connection = factory.newConnection();
        // rabbitmq对外api的入口
        final Channel channel = connection.createChannel();
        channel.basicQos(1);


        String queueName = "queue.topic.q1";
        String exchangeName = "exchange.topic.x";

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(queueName, false, false, false, null);

        String[] routingKeys = {"*.orange.*"};
        for (int i = 0; i < routingKeys.length; i++) {
            channel.queueBind(queueName, exchangeName, routingKeys[i]);
        }

        System.out.println("Consumer Wating Receive Message");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [C] Received '" + message + "', 处理业务中...");
            }
        };

        channel.basicConsume(queueName, true, consumer);

        Thread.sleep(1000000);


    }


    @Test
    public void testBasicConsumer2() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("101.132.177.27");
        factory.setPort(AMQP.PROTOCOL.PORT);
        factory.setUsername("blackfat");
        factory.setPassword("123456");

        // socket连接，封装了socket协议部分逻辑
        Connection connection = factory.newConnection();
        // rabbitmq对外api的入口
        final Channel channel = connection.createChannel();
        channel.basicQos(1);


        String queueName = "queue.topic.q2";
        String exchangeName = "exchange.topic.x";

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(queueName, false, false, false, null);

        String[] routingKeys = {"*.*.rabbit", "lazy.#"};
        for (int i = 0; i < routingKeys.length; i++) {
            channel.queueBind(queueName, exchangeName, routingKeys[i]);
        }

        System.out.println("Consumer Wating Receive Message");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [C] Received '" + message + "', 处理业务中...");
            }
        };

        channel.basicConsume(queueName, true, consumer);

        Thread.sleep(1000000);


    }
}
