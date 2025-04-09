package ru.tms.api;

import org.springframework.web.bind.annotation.*;
import ru.tms.dto.CommentDto;
import ru.tms.mappers.CommentMapper;
import ru.tms.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/tms_comment")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/comment/{commentId}")
    public CommentDto getComment(@PathVariable Long commentId){
        return commentMapper.toDto(commentService.getCommentById(commentId));
    }

    @GetMapping("/comments/{targetType}/{targetObjectId}")
    public List<CommentDto> getComments(@PathVariable("targetType") String targetType,
                                        @PathVariable("targetObjectId") Long targetObjectId) {
        return commentService.getAllComments(targetType, targetObjectId);
    }

    @PostMapping("/comment")
    public CommentDto createComment (@RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }

    @PutMapping("/comment/{commentId}")
    public CommentDto createComment (@RequestBody CommentDto commentDto, @PathVariable Long commentId) {
        return commentService.updateComment(commentDto, commentId);
    }

    @DeleteMapping("/comment/{commentId}")
    public void deleteComment (@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
