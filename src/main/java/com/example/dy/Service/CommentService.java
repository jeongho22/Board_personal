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

import javax.persistence.EntityManager;
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

        //  1.조회 : 댓글 목록 조회
        List<Comment> comments =commentRepository.findByArticleId(articleId); // 게시글 번호에 해당하는 comments 를 찾는다.


        //  2.변환: comment 엔티티 -> DTO
        List<CommentDto> dtos = new ArrayList<CommentDto>();                // dtos로 비어있는 dto 리스트를 하나 만들고
        log.info("테스트2 =>{}",dtos);
        for (int i = 0; i < comments.size(); i++) {
            Comment look = comments.get(i);                          //변환            // entity를
            CommentDto dto = CommentDto.createCommentDto(look);      //변환            // 여기에 dto로 변환
            dtos.add(dto);

            log.info(String.valueOf(look));
            log.info(String.valueOf(dto));
        }

        //  3.반환
        return dtos;


//        //조회 : 댓글 목록 조회
//        return commentRepository.findByArticleId(articleId)
//                .stream()                                           // 하나씩 꺼내온다... 반복문 안써도됨....
//                .map(comment -> CommentDto.createCommentDto(comment))
//                                                                    // 1.꺼내진 comment를 -> commentDto에서 함수 createCommentDto 사용 반환
//                .collect(Collectors.toList());                      // 2.입력으로 들어온 comment 를 다시 CommentDto.createCommentDto(comment) 여기에 넣음
//                                                                    // 3.List로 묶음
    }




    @Transactional
    public CommentDto create(Long articleId, CommentDto dto) {

        log.info("게시글 id 입력값 =>{}",articleId);
        log.info("댓글 dto 입력값 =>{}",dto);
        // 게시글 조회 및 예외 발생
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! 대상 게시글이 없습니다."));
        // 예외발생되면 아래 실행X

        // 댓글 엔티티 생성
        Comment comment = Comment.createComment(dto, article); //  dto, article 두개다 던져줘서 댓글 엔티티 생성
        log.info("댓글 엔티티 생성=>{}",comment);

        // 댓글 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);

        // entity - > DTO로 변경하여 반환
        return CommentDto.createCommentDto(created);


    }
// 위처럼 반복적인 코드를 aop로 가능
    @Transactional
    public CommentDto update(Long id, CommentDto dto) {
        // 1.댓글 조회 및 예외 발생
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패! 대상 댓글이 없습니다."));
        log.info("댓글 엔티티 =>{}",comment);

        // 2.댓글 수정
        comment.patch(dto);
        log.info("댓글 수정된 엔티티 =>{}",comment);
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
