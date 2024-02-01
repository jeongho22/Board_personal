package com.example.dy.Service;


import com.example.dy.Domain.Article;
import com.example.dy.Domain.Comment;
import com.example.dy.Dto.CommentDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Repository.CommentRepository;
import com.example.dy.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class CommentService {


//     필드 주입 방식 (비권장)
//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private ArticleRepository articleRepository;
//

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    // 생성자 주입 방식 (권장)
    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }


    public List<CommentDto> comments(Long articleId) {

        //  1.조회 : 댓글 목록 조회
        List<Comment> comments =commentRepository.findByArticleId(articleId); // 게시글 번호에 해당하는 comments 를 찾는다.
        log.info("댓글 조회 목록 1: {} ",comments);


        //  2. entity - > DTO로 변경하여 반환
        List<CommentDto> dtos = new ArrayList<CommentDto>();      // Comment 에서 -> Dto 변환된 Dto 담는 리스트 생성
        log.info("댓글 조회 목록 2: {} ",dtos);

        for (int i = 0; i < comments.size(); i++) {
            Comment c = comments.get(i);
            CommentDto dto = CommentDto.createCommentDto(c);      // Comment entity 를 -> dto 로 변환
            dtos.add(dto);

            log.info("댓글 조회 목록 3(Comment) : {} ", c);
            log.info("댓글 조회 목록 3(DTO) : {} ", dto);
        }
            log.info("댓글 조회 목록 4(Dtos) : {} ", dtos);

        //  3.반환
        return dtos;


    }


    @Transactional
    public CommentDto create(Long articleId, CommentDto dto) {

        log.info("1. 게시글 Id : {}", articleId);
        log.info("2. 댓글 Dto : {}", dto);

        // 1.게시글 조회 및 예외 발생 (예외발생 되면 아래 실행X) -> comment 가 달린 위치 찾기 위해
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! 대상 게시글이 없습니다."));

        // 2.댓글 엔티티 생성
        Comment comment = Comment.createComment(dto, article); //  dto, article 두개다 던져줘서 댓글 엔티티 생성

        // 3.댓글 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);

        log.info("3. 댓글 엔티티 생성 : {}",created);

        // 4.entity - > DTO로 변경하여 반환
        return CommentDto.createCommentDto(created);


    }


    @Transactional
    public CommentDto update(Long id, CommentDto dto) {

        log.info("1. 댓글 Dto : {}", dto);

        // 1.댓글 조회 및 예외 발생
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패! 대상 댓글이 없습니다."));

        log.info("2. 댓글 엔티티 : {}",comment);

        // 2.댓글 수정
        comment.patch(dto);
        log.info("3. 댓글 수정된 엔티티 : {}",comment);

        // 3.DB로 갱신
        Comment updated = commentRepository.save(comment);

        // 4.댓글 엔티티를 DTO로 변환 및 반환
        return CommentDto.createCommentDto(updated);
    }

    @Transactional
    public CommentDto delete(Long id) {
        // 댓글 조회(및 예외 발생)
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패! 대상이 없습니다."));
        log.info("댓글 엔티티 =>{}",comment);
        // 댓글 삭제
        commentRepository.delete(comment);
        // 삭제 댓글을 DTO로 반환
        return CommentDto.createCommentDto(comment);
    }

}
