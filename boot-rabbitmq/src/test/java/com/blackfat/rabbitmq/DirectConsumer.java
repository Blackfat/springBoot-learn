package com.blackfat.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/9/25-10:41
 */
public class DirectConsumer {


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
        // 设置每次从队列获取消息的数量
        /*
        *  在默认情况下，rabbitmq会逐个发送消息到序列中的消费者（而不考虑每个任务的时长等等，且是提前一次性分配，并非一个一个的分配）
        *平均每个消费者获得相同数量的消息。这种方式分发消息机制称为Round-Robin（轮询）。
        * 通过使用channel.basicQos(1)告诉消费者一次只能从队列中预先获取一条消息，处理完了在获取下一条。使用公平转发这种方式支持动态添加消费者。
        * */
        channel.basicQos(1);


        String  queueName = "queue.work";
        channel.queueDeclare(queueName, false, false, false, null);
        System.out.println("Consumer Wating Receive Message");


        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    doWork(message);
                    // 手动应答
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        // 订阅消息, false: 表示手动应答,需要手动调用basicAck()来应答
        channel.basicConsume(queueName, false, consumer);

        // 睡眠是为了不让程序立即结束，这样还有机会获取第二条消息
        Thread.sleep(1000000);

    }


    private void doWork(String message) throws Exception{
        System.out.println(" [C] Received '" + message + "', 处理业务中...");
        // 模仿消费者处理业务的时间，也让其他消费者有机会获取到消息，实际开发中不需要，这里只是模拟
        Thread.sleep(1000);
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
        // 设置每次从队列获取消息的数量
        channel.basicQos(1);


        String  queueName = "queue.work";
        channel.queueDeclare(queueName, false, false, false, null);
        System.out.println("Consumer Wating Receive Message");


        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    doWork(message);
                    // 手动应答
                    channel.basicAck(envelope.getDeliveryTag(), false);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        // 订阅消息, false: 表示手动应答,需要手动调用basicAck()来应答
        channel.basicConsume(queueName, false, consumer);

        // 睡眠是为了不让程序立即结束，这样还有机会获取第二条消息
        Thread.sleep(1000000);

    }



}
