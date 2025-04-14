package ru.tms.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.tms.dto.ReducedComment;

import java.util.Properties;

public class KafkaConsumerConfig {

    public static KafkaConsumer<String, ReducedComment> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:39092,localhost:49092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "tms");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.tms.dto");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return new KafkaConsumer<>(props);
    }
}