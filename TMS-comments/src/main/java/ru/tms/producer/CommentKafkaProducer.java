package ru.tms.producer;

import ru.tms.dto.Comment;

public interface CommentKafkaProducer {
    void sendMessage(Comment comment);
}
