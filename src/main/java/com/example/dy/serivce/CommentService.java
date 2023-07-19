

package com.example.dy.service;

import com.example.dy.entity.Comment;
import com.example.dy.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

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
    public void deleteById(Integer id, String username) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAuthorized = false;

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ADMIN") ||
                    (authority.getAuthority().equals("USER") && comment.getAuthor().equals(username))) {
                isAuthorized = true;
                commentRepository.deleteById(id);
                System.out.println("댓글이 삭제되었습니다.");
                break;
            }
        }

        if (!isAuthorized) {
            throw new RuntimeException("댓글을 삭제할 권한이 없습니다.");
        }
    }


}