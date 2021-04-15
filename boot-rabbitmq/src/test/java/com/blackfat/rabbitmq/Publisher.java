package com.blackfat.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/9/25-9:43
 */
public class Publisher {


    @Test
    public void testBasicPublish() throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("101.132.177.27");
        factory.setPort(AMQP.PROTOCOL.PORT);
        factory.setUsername("blackfat");
        factory.setPassword("123456");

        // socket连接，封装了socket协议部分逻辑
        Connection connection = factory.newConnection();
        // rabbitmq对外api的入口
        Channel channel = connection.createChannel();

        String queueName = "queue.work";
        String routingKey = "task";
        String exchangeName = "amqp.rabbitmq.work";

        // 声明交换机
        /*
         * product不是通过将消息直接发送给queue，而是先发送给exchange。一个exchange可以绑定多个queue，
         * product在发送消息的时候会传递一个routingKey,exchange会根据这个routingkey按照特定的路由算法，
         * 将消息传递给指定的queue
         * Direct
         * 直接交换器，工作方式类似于单播，Exchange会将消息发送完全匹配ROUTING_KEY的Queue
         * fanout
         * 广播模式交换器，不管消息的ROUTING_KEY设置为什么，Exchange都会将消息转发给所有绑定的Queue。
         * topic
         *主题交换器，工作方式类似于组播，Exchange会将消息转发和ROUTING_KEY匹配模式相同的所有队列，比如，ROUTING_KEY为user.stock的Message会转发给绑定匹配模式为 * .stock,user.stock， * . * 和#.user.stock.#的队列。（ * 表是匹配一个任意词组，#表示匹配0个或多个词组）
         * headers
         * 消息体的header匹配（ignore）
         * */
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        // 声明队列
        /*
         *消息队列可以设置持久化，临时或者自动删除
         *设置为持久化的队列，queue中的消息会在server本地硬盘存储一份，防止系统crash，数据丢失
         *设置为临时队列，queue中的数据在系统重启之后就会丢失
         *设置为自动删除的队列，当不存在用户连接到server，队列中的数据会被自动删除Exchange
         * */
        channel.queueDeclare(queueName, false, false, false, null);

        channel.queueBind(queueName, exchangeName, routingKey);

        // 循环发布多条消息
        for (int i = 0; i < 10; i++) {
            String message = "Hello RabbitMQ " + i;
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
        }

        // 关闭资源
        channel.close();
        connection.close();


    }


    @Test
    public void testBasicPublish1() throws IOException, TimeoutException {

        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("101.132.177.27");
        factory.setPort(AMQP.PROTOCOL.PORT);
        factory.setUsername("blackfat");
        factory.setPassword("123456");

        // socket连接，封装了socket协议部分逻辑
        Connection connection = factory.newConnection();
        // rabbitmq对外api的入口
        Channel channel = connection.createChannel();

        String exchangeName = "exchange.fanout";

        /*
         *广播模式交换器在发布消息时可以只先指定交换机的名称，交换机的声明的代码可以放到消费者端进行声明，队列的声明也放在消费者端来声明。
         */

        // 循环发布多条消息
        for (int i = 0; i < 10; i++) {
            String message = "Hello RabbitMQ " + i;
            channel.basicPublish(exchangeName, "", null, message.getBytes("UTF-8"));
        }


        // 关闭资源
        channel.close();
        connection.close();

    }


    @Test
    public void testBasicPublish2() throws IOException, TimeoutException {
        // 连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("101.132.177.27");
        factory.setPort(AMQP.PROTOCOL.PORT);
        factory.setUsername("blackfat");
        factory.setPassword("123456");

        // socket连接，封装了socket协议部分逻辑
        Connection connection = factory.newConnection();
        // rabbitmq对外api的入口
        Channel channel = connection.createChannel();

        /*
         *topic交换机的routingKey可以进行模糊匹配，可以使用*号和#号进行模糊匹配。
         * 其中*号可以代表一个单词，#号可以代表0个或更多的单词，上限是255个字节
         * */
        String exchangeName = "exchange.topic.x";
        String[] routingKeys = {"quick.orange.rabbit", "lazy.orange.elephant", "mq.erlang.rabbit", "lazy.brown.fox", "lazy."};


        for (String routingKey : routingKeys) {
            String message = "Hello RabbitMQ - " + routingKey;

            channel.basicPublish(exchangeName, routingKey, null, message.getBytes("UTF-8"));
        }

        // 关闭资源
        channel.close();
        connection.close();


    }


}
