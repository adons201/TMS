package ru.tms.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;
import ru.tms.config.KafkaProducerConfig;
import ru.tms.dto.Comment;
import ru.tms.dto.ReducedComment;
import ru.tms.mappers.CommentMapper;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CommentCommentKafkaProducerImpl implements CommentKafkaProducer {

    private final Logger logger = Logger.getLogger(CommentCommentKafkaProducerImpl.class.getName());
    private final CommentMapper commentMapper;
    private static final String TOPIC = "comments-topic";

    public CommentCommentKafkaProducerImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public void sendMessage(Comment comment) {
        try (KafkaProducer<String, ReducedComment> producer = KafkaProducerConfig.createProducer()) {
            ReducedComment reducedComment = this.commentMapper.toReduced(comment);
            ProducerRecord<String, ReducedComment> record = new ProducerRecord<>(TOPIC,
                    reducedComment.id().toString(),
                    reducedComment);
            Future<RecordMetadata> future = producer.send(record);
            logger.log(Level.FINE, "Сообщение отправлено на тему %s", future.get().topic());
        } catch (ExecutionException | InterruptedException e) {
            logger.log(Level.FINE, "Сообщение не отправлено. Ошибка: %s", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}