package com.blackfat.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/9/26-9:17
 */
public class RpcQueue {

    @Test
    public void client() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("101.132.177.27");
        factory.setPort(AMQP.PROTOCOL.PORT);
        factory.setUsername("blackfat");
        factory.setPassword("123456");

        // socket连接，封装了socket协议部分逻辑
        Connection connection = factory.newConnection();
        // rabbitmq对外api的入口
        Channel channel = connection.createChannel();

        //响应结果队列
        String replyQueue = channel.queueDeclare().getQueue();
        final String correlationId = UUID.randomUUID().toString();

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(correlationId)) {
                    String message = new String(body, "UTF-8");
                    System.out.println("已接收到服务器的响应结果：" + message);
                }

            }
        };

        channel.basicConsume(replyQueue, true, consumer);

        String rpcQueue = "rpcQueue";
        String message = "hello rabbitmq";
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().correlationId(correlationId).replyTo(replyQueue).build();
        // 直接发送给队列，不需要指定exchange ,默认为空
        channel.basicPublish("", rpcQueue, properties, message.getBytes("utf-8"));
        System.out.println("已发出请求请求消息：" + message);
        Thread.sleep(100000);

    }


    @Test
    public void server() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("101.132.177.27");
        factory.setPort(AMQP.PROTOCOL.PORT);
        factory.setUsername("blackfat");
        factory.setPassword("123456");

        // socket连接，封装了socket协议部分逻辑
        Connection connection = factory.newConnection();
        // rabbitmq对外api的入口
        final Channel channel = connection.createChannel();
        String rpcQueue = "rpcQueue";
        channel.queueDeclare(rpcQueue, false, false, false, null);
        channel.basicQos(1);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("服务端：已接收到请求消息：" + message);
                // 服务器端接收到消息并处理消息
                String response = "{'code': 200, 'data': '" + message + "'}";

                // 将消息发布到reply_to响应队列中
                AMQP.BasicProperties replyProperties = new AMQP.BasicProperties.Builder().
                        correlationId(properties.getCorrelationId()).build();
                String replyTo = properties.getReplyTo();
                channel.basicPublish("", replyTo, replyProperties, response.getBytes("UTF-8"));
                System.out.println("服务端：请求已处理完毕，响应结果" + response + "已发送到响应队列中");
                // 手动应答
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        // 手动应答模式
        channel.basicConsume(rpcQueue, false, consumer);

        System.out.println("服务端：已订阅请求队列(rpcQueue), 开始等待接收请求消息...");
        Thread.sleep(100000);


    }
}
