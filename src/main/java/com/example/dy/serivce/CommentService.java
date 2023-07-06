
package com.example.dy.service;

import com.example.dy.entity.Comment;
import com.example.dy.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;


    //댓글 저장


    public Comment save(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now()); // 이 시간 부분 추가
        return commentRepository.save(comment);
    }


    //댓글 찾기
    public Comment findById(Integer id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));
    }

    //댓글 삭제
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }



}