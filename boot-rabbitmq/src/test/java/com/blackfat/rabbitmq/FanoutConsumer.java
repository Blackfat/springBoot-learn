package com.blackfat.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/9/25-15:01
 */
public class FanoutConsumer {

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

        String queueName = "queue.fanout.q1";
        String exchangeName = "exchange.fanout";

        channel.queueDeclare(queueName, false, false, false, null);
        // 声明交换机：指定交换机的名称和类型(广播：fanout)
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
        // 在消费者端队列绑定
        channel.queueBind(queueName, exchangeName, "");
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

        String queueName = "queue.fanout.q2";
        String exchangeName = "exchange.fanout";


        channel.queueDeclare(queueName, false, false, false, null);
        // 声明交换机：指定交换机的名称和类型(广播：fanout)
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT);
        // 在消费者端队列绑定
        channel.queueBind(queueName, exchangeName, "");
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
