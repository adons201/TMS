package ru.tms.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.tms.dto.ReducedComment;

import java.util.Properties;

public class KafkaProducerConfig {

    public static KafkaProducer<String, ReducedComment> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092,localhost:39092,localhost:49092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 1000);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        return new KafkaProducer<>(props);
    }
}
