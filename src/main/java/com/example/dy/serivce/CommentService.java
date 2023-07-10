package com.example.dy.service;

import com.example.dy.dto.CommentDto;
import com.example.dy.entity.Board;
import com.example.dy.entity.Comment;
import com.example.dy.repository.BoardRepository;
import com.example.dy.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BoardRepository boardRepository;


    public Comment createComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setAuthor(commentDto.getAuthor());
        comment.setContent(commentDto.getContent());

        Board board = boardRepository.findById(commentDto.getBoardId()).orElseThrow(
                () -> new EntityNotFoundException("Board not found with id " + commentDto.getBoardId())
        );

        comment.setBoard(board);
        return commentRepository.save(comment);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment updateComment(Integer id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment != null) {
            comment.setAuthor(commentDetails.getAuthor());
            comment.setContent(commentDetails.getContent());
            return commentRepository.save(comment);
        }
        return null;
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }



}
