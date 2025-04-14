package ru.tms.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;
import ru.tms.config.KafkaConsumerConfig;
import ru.tms.dto.ReducedComment;
import ru.tms.service.NotificationService;
import ru.tms.service.NotificationServiceImpl;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CommentCommentKafkaConsumer {

    private final NotificationService notificationService;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Logger logger = Logger.getLogger(CommentCommentKafkaConsumer.class.getName());

    public CommentCommentKafkaConsumer(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
        new Thread(this::consume).start();
    }

    private void consume() {
        try (KafkaConsumer<String, ReducedComment> consumer = KafkaConsumerConfig.createConsumer()) {
            consumer.subscribe(List.of("comments-topic"));
            while (running.get()) {
                ConsumerRecords<String, ReducedComment> records = consumer.poll(Duration.ofMillis(500));
                for (ConsumerRecord<String, ReducedComment> record : records) {
                    logger.log(Level.FINE, () -> String.format("Получено сообщение: topic = %s, partition = %d," +
                                    " offset = %d, key = %s, value = %s",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value()));
                    notificationService.createNotification(record.value().targetType(),
                            record.value().targetObjectId(),
                            record.value().content());
                }
                consumer.commitSync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopConsuming() {
        running.set(false);
    }
}