package com.example.dy.controller;

import com.example.dy.dto.CommentDto;
import com.example.dy.entity.Comment;
import com.example.dy.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public Comment getCommentById(@PathVariable Integer id) {
        return commentService.getCommentById(id);
    }

    @PostMapping
    public Comment createComment(@RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Integer id, @RequestBody Comment comment) {
        return commentService.updateComment(id, comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
    }


}
