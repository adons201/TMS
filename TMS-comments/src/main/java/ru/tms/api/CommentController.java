package ru.tms.api;

import org.springframework.web.bind.annotation.*;
import ru.tms.dto.Comment;
import ru.tms.mappers.CommentMapper;
import ru.tms.producer.CommentCommentKafkaProducerImpl;
import ru.tms.services.CommentService;
import ru.tms.services.CommentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/tms_comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final CommentCommentKafkaProducerImpl commentKafkaProducer;

    public CommentController(CommentServiceImpl commentService, CommentMapper commentMapper,
                             CommentCommentKafkaProducerImpl commentKafkaProducer) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.commentKafkaProducer = commentKafkaProducer;
    }

    @GetMapping("/comment/{commentId}")
    public Comment getComment(@PathVariable Long commentId){
        return commentMapper.toDto(commentService.getCommentById(commentId));
    }

    @GetMapping("/comments/{targetType}/{targetObjectId}")
    public List<Comment> getComments(@PathVariable("targetType") String targetType,
                                     @PathVariable("targetObjectId") Long targetObjectId) {
        return commentService.getAllComments(targetType, targetObjectId);
    }

    @PostMapping("/comment")
    public Comment createComment (@RequestBody Comment comment) {
        Comment createdComment = commentService.createComment(comment);
        commentKafkaProducer.sendMessage(createdComment);
        return createdComment;
    }

    @PutMapping("/comment/{commentId}")
    public Comment updateComment(@RequestBody Comment comment, @PathVariable Long commentId) {
        return commentService.updateComment(comment, commentId);
    }

    @DeleteMapping("/comment/{commentId}")
    public void deleteComment (@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
