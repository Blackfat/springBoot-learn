package com.blackfat.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/11/29-11:49
 */
public class KafkaTestCase {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTestCase.class);


    @Test
    public void producerTest() {
        Properties props = new Properties();
        // 连接kafka集群所需要的broker地址清单，并非需要设置全部的broker地址
        props.put("bootstrap.servers", "192.168.15.38:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        // ProductBatch可以复用内存区域的大小
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        // 缓存消息的缓冲区大小
        props.put("buffer.memory", 33554432);
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 10; i++)
            try {
                RecordMetadata recordMetadata = producer.send(new ProducerRecord<String, String>("blackfat", Integer.toString(i), Integer.toString(i))).get();
                logger.info("msg offset={} partition={} checksum={} timestamp={} topic={}",
                        recordMetadata.offset(), recordMetadata.partition(), recordMetadata.checksum(), recordMetadata.timestamp(), recordMetadata.topic());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        producer.close();
    }


    @Test
    public void consumerTest() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.15.38:9092");
        props.put("group.id", "test");
        // 消费位移的提交
        props.put("enable.auto.commit", "true");
        // 消费位移的提交周期
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        /*
         *topic下的所有有效分区平铺，例如P0, P1, P2, P3... ...
         *消费者按照字典排序，例如C0, C1, C2
         *区数除以消费者数，得到n
         *分区数对消费者数取余，得到m
         *消费者集合中，前m个消费者能够分配到n+1个分区，而剩余的消费者只能分配到n个分区。
         *
         * */
        //props.put("partition.assignment.strategy", "org.apache.kafka.clients.consumer.RangeAssignor");
        //props.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RangeAssignor.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("blackfat"));
//        TopicPartition topicPartition = new TopicPartition("blackfat", 0);
//        consumer.assign(Arrays.asList(topicPartition));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("offset = {},partition={}, key = {}, value = {}", record.offset(), record.partition(), record.key(), record.value());
            }

        }
//        System.err.println(consumer.position(topicPartition));
    }


    @Test
    public void consumerSeekTest() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "101.132.177.27:9092");
        props.put("group.id", "test");
        // 消费位移的提交
        props.put("enable.auto.commit", "true");
        // 消费位移的提交周期
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("partition.assignment.strategy", "org.apache.kafka.clients.consumer.RangeAssignor");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("my-topic"), new ConsumerRebalanceListener() {
            @Override
            // 再均衡之前和消费者停止读取消息之前
            public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

            }

            @Override
            // 再均衡之后和消费者开始读取消息之前
            public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

            }
        });

        Set<TopicPartition> assignment = new HashSet<>();
        while (assignment.size() == 0) {
            consumer.poll(1000);
            assignment = consumer.assignment();
        }

        for (TopicPartition tp : assignment) {
            // seek只能重启消费者已经分配到的分区位置
            consumer.seek(tp, 10);
        }
    }
}
