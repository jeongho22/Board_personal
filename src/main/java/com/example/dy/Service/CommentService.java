package com.example.dy.Service;


import com.example.dy.Domain.Article;
import com.example.dy.Domain.Comment;
import com.example.dy.Dto.CommentDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public List<CommentDto> comments(Long articleId) {
//        //조회 : 댓글 목록 조회
//
//        List<Comment> comments =commentRepository.findByArticleId(articleId); // findByArticleId 메서드 comments 갯수
//
//        // 변환: 엔티티 -> DTO
//        List<CommentDto> dtos = new ArrayList<CommentDto>();
//        for (int i = 0; i < comments.size(); i++) {                           // comments 갯수 만큼 반복문 0,1,2,3...
//            Comment c = comments.get(i);
//            CommentDto dto = CommentDto.createCommentDto(c);
//            dtos.add(dto);
//        }
//        // 반환
//        return dtos;

        return commentRepository.findByArticleId(articleId)
                .stream()                                           // 하나씩 꺼내온다... 반복문 안써도됨....
                .map(comment -> CommentDto.createCommentDto(comment))
                                                                    // 1.꺼내진 comment를 -> commentDto에서 함수 createCommentDto 사용 반환
                .collect(Collectors.toList());                      // 2.입력으로 들어온 comment 를 다시 CommentDto.createCommentDto(comment) 여기에 넣음
                                                                    // 3.List로 묶음
    }

    @Transactional
    public CommentDto create(Long articleId, CommentDto dto) {

        log.info("입력값 =>{}",articleId);
        log.info("입력값 =>{}",dto);
        // 게시글 조회 및 예외 발생
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! 대상 게시글이 없습니다."));
        // 댓글 엔티티 생성
        Comment comment = Comment.createComment(dto, article);
        // 댓글 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);
        // DTO로 변경하여 반환

        return CommentDto.createCommentDto(created);

    }
// 위처럼 반복적인 코드를 aop로 가능
    @Transactional
    public CommentDto update(Long id, CommentDto dto) {
        // 댓글 조회 및 예외 발생
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패! 대상 댓글이 없습니다."));
        // 댓글 수정
        target.patch(dto);
        // DB로 갱신
        Comment updated = commentRepository.save(target);
        // 댓글 엔티티를 DTO로 변환 및 반환
        return CommentDto.createCommentDto(updated);
    }

    @Transactional
    public CommentDto delete(Long id) {
        // 댓글 조회(및 예외 발생)
        Comment target = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패! 대상이 없습니다."));
        // 댓글 삭제
        commentRepository.delete(target);
        // 삭제 댓글을 DTO로 반환
        return CommentDto.createCommentDto(target);
    }
}
