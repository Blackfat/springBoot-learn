package com.blackfat.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * @author wangfeiyang
 * @desc
 * @create 2018/11/29-11:49
 */
public class KafkaTestCase {
    private static final Logger logger = LoggerFactory.getLogger(KafkaTestCase.class);


    @Test
    public void producerTest(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "101.132.177.27:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for(int i = 0; i < 100; i++)
            try {
                RecordMetadata recordMetadata = producer.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i), Integer.toString(i))).get();
                logger.info("msg offset={} partition={} checksum={} timestamp={} topic={}",
                        recordMetadata.offset(),recordMetadata.partition(),recordMetadata.checksum(),recordMetadata.timestamp(),recordMetadata.topic());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        producer.close();
    }


    @Test
    public void consumerTest(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "101.132.177.27:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("my-topic"));
        List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(10);
            for (ConsumerRecord<String, String> record : records){
                logger.info("offset = {},partition={}, key = {}, value = {}", record.offset(),record.partition(), record.key(), record.value());
                buffer.add(record);
            }
            if(buffer.size() > 10){
                logger.info("写入数据库成功，size={}",buffer.size());
                buffer.clear();
            }

        }
    }
}
