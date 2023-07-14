package com.example.dy.service;


import com.example.dy.dto.CommentDto;
import com.example.dy.entity.Board;
import com.example.dy.entity.Comment;
import com.example.dy.repository.BoardRepository;
import com.example.dy.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service // 스프링에서 제공하는 어노테이션으로, 이 클래스가 서비스 클래스임을 나타냅니다. 이 어노테이션을 통해 비즈니스 로직을 처리하는 클래스임을 명시하고, 스프링의 자동 와이어링 기능을 활용합니다.
public class CommentService {
    @Autowired // 스프링의 의존성 주입을 위한 어노테이션입니다. 이를 통해 CommentRepository 객체를 자동으로 주입받습니다.
    private CommentRepository commentRepository;

    @Autowired // 스프링의 의존성 주입을 위한 어노테이션입니다. 이를 통해 BoardRepository 객체를 자동으로 주입받습니다.
    private BoardRepository boardRepository;

    // 모든 댓글을 페이지네이션과 함께 가져오는 메서드입니다.
    public Page<Comment> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    // 주어진 ID에 해당하는 댓글을 반환하는 메서드입니다. 해당 ID의 댓글이 없을 경우 null을 반환합니다.
    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id).orElse(null);
    }

    // 주어진 내용을 포함하는 댓글을 페이지네이션과 함께 가져오는 메서드입니다.
    public Page<Comment> getCommentsByContent(String content, Pageable pageable) {
        return commentRepository.findByContentContaining(content, pageable);
    }

    // 주어진 작성자의 댓글을 페이지네이션과 함께 가져오는 메서드입니다.
    public Page<Comment> getCommentsByAuthor(String author, Pageable pageable) {
        return commentRepository.findByAuthorContaining(author, pageable);
    }

    // 새 댓글을 생성하는 메서드입니다. 댓글 DTO를 입력으로 받아 이를 통해 댓글 엔티티를 생성하고 저장합니다.
    public Comment createComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setAuthor(commentDto.getAuthor());
        comment.setContent(commentDto.getContent());

        // 댓글이 속해야 할 게시글을 찾아, 없을 경우 예외를 발생시킵니다.
        Board board = boardRepository.findById(commentDto.getBoardId()).orElseThrow(
                () -> new EntityNotFoundException("Board not found with id " + commentDto.getBoardId())
        );

        comment.setBoard(board);
        return commentRepository.save(comment); // 댓글을 저장하고, 저장된 댓글을 반환합니다.
    }

    // 주어진 ID에 해당하는 댓글을 수정하는 메서드입니다.
    public Comment updateComment(Integer id, Comment commentDetails) {
        Comment comment = commentRepository.findById(id).orElse(null);

        if (comment != null) { // 만약 댓글이 존재한다면,
            comment.setAuthor(commentDetails.getAuthor()); // 댓글의 작성자를 수정하고,
            comment.setContent(commentDetails.getContent()); // 댓글의 내용을 수정한 후,
            return commentRepository.save(comment); // 수정된 댓글을 저장하고 반환합니다.
        }
        return null; // 해당 ID의 댓글이 없을 경우 null을 반환합니다.
    }

    // 주어진 ID에 해당하는 댓글을 삭제하는 메서드입니다.
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }


}
