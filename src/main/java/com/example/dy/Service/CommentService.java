package com.example.dy.Service;


import com.example.dy.Domain.Article;
import com.example.dy.Domain.Comment;
import com.example.dy.Domain.User;
import com.example.dy.Domain.constant.Role;
import com.example.dy.Dto.CommentDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserService userService;

    // 생성자 주입 방식 (권장)
    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository,UserService userService) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userService = userService;
    }


    // 1.게시물에 달린 댓글 카운트
    @Transactional(readOnly = true)
    public Long countCommentsByArticleId(Long articleId) {
        return commentRepository.countByArticleId(articleId);
    }



    // 2. 조회 : 댓글 목록 조회
//    public List<CommentDto> comments(Long articleId) {
//        List<Comment> comments = commentRepository.findByArticleIdAndDeletedAtIsNull(articleId);
//        Map<Long, CommentDto> commentDtoMap = new HashMap<>();
//        List<CommentDto> result = new ArrayList<>();
//
//        // 댓글을 DTO로 변환하면서 Map에 저장
//        comments.forEach(comment -> {
//            CommentDto dto = CommentDto.createCommentDto(comment);
//            commentDtoMap.put(comment.getId(), dto);
//            if (comment.getParentComment() == null) {
//                result.add(dto); // 원댓글 리스트에 추가
//            }
//        });
//
//        // 대댓글을 원댓글의 리스트에 추가
//        comments.stream().filter(comment -> comment.getParentComment() != null).forEach(comment -> {
//            CommentDto parentDto = commentDtoMap.get(comment.getParentComment().getId());
//            CommentDto replyDto = commentDtoMap.get(comment.getId());
//            parentDto.addReply(replyDto);
//        });
//
//        return result;
//    }

    public List<CommentDto> comments(Long articleId) {

        List<Comment> comments = commentRepository.findByArticleIdAndDeletedAtIsNull(articleId);
        List<CommentDto> commentDtos = new ArrayList<>();
        Map<Long, CommentDto> commentDtoMap = new HashMap<>();

        // 원댓글 처리
        for (Comment comment : comments) {
            if (comment.getParentComment() == null) {
                CommentDto dto = CommentDto.createCommentDto(comment);
                commentDtoMap.put(comment.getId(), dto);
                commentDtos.add(dto); // 원댓글 리스트에 추가
            }
        }

        // 대댓글 처리
        for (Comment comment : comments) {
            if (comment.getParentComment() != null) {

                CommentDto parentDto = commentDtoMap.get(comment.getParentComment().getId());

                CommentDto replyDto = CommentDto.createCommentDto(comment); // 대댓글 DTO 변환

                parentDto.addReply(replyDto); // 원댓글의 DTO에 대댓글 추가
            }
        }

        return commentDtos;
    }



    // 3. 댓글 생성
    @Transactional
    public CommentDto create(Long articleId, CommentDto dto) {

        log.info("1. 게시글 Id : {}", articleId);
        log.info("2. 댓글 Dto : {}", dto);

        log.info("생성 변환 이전(Dto) 1 : {}",dto);

        // 1.현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = userService.getCurrentUser(); // 현재 로그인한 사용자 정보 가져오기

        // 2.게시글 조회 및 예외 발생 (예외발생 되면 아래 실행X) -> comment 가 달린 위치 찾기 위해
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 생성 실패! 대상 게시글이 없습니다."));

        // 3.댓글 엔티티 생성
        // 대댓글 처리를 위해 CommentRepository 전달
        Comment comment = Comment.createComment(dto, article, currentUser, commentRepository);

        // 4.댓글 엔티티를 DB로 저장
        Comment created = commentRepository.save(comment);

        log.info("3. 댓글 엔티티 생성 : {}",created);

        // 5.entity - > DTO로 변경하여 반환
        return CommentDto.createCommentDto(created);
    }


    // 4. 댓글 수정
    @Transactional
    public CommentDto update(Long id, CommentDto dto) {

        log.info("1. 댓글 Dto : {}", dto);

        // 0.현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = userService.getCurrentUser(); // 현재 로그인한 사용자 정보 가져오기


        // 1.댓글 조회 및 예외 발생
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 수정 실패! 대상 댓글이 없습니다."));

        log.info("2. 댓글 엔티티 : {}",comment);

        // 2.댓글 작성자와 현재 로그인한 사용자 비교
        if (!(currentUser.getRole() == Role.ADMIN) && !comment.getUser().equals(currentUser)) {
            throw new IllegalStateException("댓글 수정 실패! 자신이 작성한 댓글만 수정할 수 있습니다.");
        }

        log.info("서로 다른가? :{} {} {}",!comment.getUser().equals(currentUser),comment.getUser(),currentUser);


        // 3. 댓글이 삭제된 상태인지 확인
        if (comment.isDeleted()) {
            throw new IllegalStateException("댓글 수정 실패! 삭제된 댓글은 수정할 수 없습니다.");
        }


        // 4.댓글 수정
        comment.patch(dto);
        log.info("3. 댓글 수정된 엔티티 : {}",comment);

        // 5.DB로 갱신
        Comment updated = commentRepository.save(comment);

        // 6.댓글 엔티티를 DTO로 변환 및 반환
        return CommentDto.createCommentDto(updated);
    }


    // 5. 댓글 삭제
    @Transactional
    public CommentDto delete(Long id) {
        // 1. 현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = userService.getCurrentUser();

        // 2. 댓글 조회
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글 삭제 실패! 대상이 없습니다."));

        // 3. 댓글 작성자와 현재 로그인한 사용자 비교
        if (!(currentUser.getRole() == Role.ADMIN) && !comment.getUser().equals(currentUser)) {
            throw new IllegalStateException("댓글 수정 실패! 자신이 작성한 댓글만 수정할 수 있습니다.");
        }

        // 4.원댓글인 경우 대댓글의 존재 여부 확인
        if (comment.getParentComment() == null) {
            List<Comment> replies = commentRepository.findByParentComment(comment);
            if (replies.isEmpty()) { // 대댓글이 없으면 원댓글 바로 삭제
                commentRepository.delete(comment);
            } else { // 대댓글이 있으면 원댓글을 "true"로 표시
                comment.setDeleted(true);
                commentRepository.save(comment);
            }
            // 5.대댓글인 경우
        } else {
            commentRepository.delete(comment);

            // 부모 댓글에 남은 대댓글이 없는지 확인
            Comment parentComment = comment.getParentComment();
            List<Comment> remainingReplies = commentRepository.findByParentComment(parentComment);
            if (remainingReplies.isEmpty() && parentComment.isDeleted()) { // 모든 대댓글이 삭제되었고, 원댓글이 이미 'true' 상태인 경우
                commentRepository.delete(parentComment);
            }
        }
        return CommentDto.createCommentDto(comment);
    }

}
