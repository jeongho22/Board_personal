package com.example.dy.Service;


import com.example.dy.Domain.Article;
import com.example.dy.Domain.Comment;
import com.example.dy.Domain.User;
import com.example.dy.Domain.constant.Role;
import com.example.dy.Domain.constant.SearchType;
import com.example.dy.Dto.ArticleDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Repository.CommentRepository;
import com.example.dy.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service // 서비스 선언!(서비스 객체를 스프링부트에 생성)
public class ArticleService {


    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    public ArticleService(ArticleRepository articleRepository,UserService userService,CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }


    //    1.전체 조회
    @Transactional
    public Page<ArticleDto> index(String searchType, String searchKeyword, Pageable pageable) {
        Page<Article> articles;

        if (searchKeyword == null || searchKeyword.isBlank()) {
            articles = articleRepository.findAll(pageable);
        }
        else {
            SearchType type = SearchType.valueOf(searchType.toUpperCase());

            articles = switch (type) {
                case ALL -> articleRepository.findByTitleContainingOrContentContaining(searchKeyword, searchKeyword, pageable);
                case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable);
                case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable);
                default -> Page.empty(pageable); // 기본값으로 비어있는 페이지를 반환
            };
        }

        // Page<Article>을 Page<ArticleDto>로 변환
        return articles.map(article -> ArticleDto.fromEntity(article));
    }


    //    1-2. 건별 조회
    @Transactional
    public ArticleDto show(Long id) {

        // 1.ID로 엔티티 조회
        //   예외가 있으면 다음으로 안넘어감.
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        // 2. 클릭마다 조회수 증가
        articleRepository.incrementViewCount(id);
        log.info("단일 조회 변환 이전(Entity) 1 : {}",article);

        // 3. 엔티티를 DTO로 변환하여 반환
        return ArticleDto.fromEntity(article);
    }

    //    2. 생성
    public ArticleDto create(ArticleDto dto) {
        log.info("생성 변환 이전(Dto) 1 : {}",dto);
        // 1.현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = userService.getCurrentUser(); // 현재 로그인한 사용자 정보 가져오기

        // 2. 현재 로그인한 사용자가 없다면 예외 발생
        if (currentUser == null) {
            throw new IllegalStateException("로그인이 필요한 기능입니다.");
        }

        // 3.Dto를 사용하여 엔티티 생성
        Article article = new Article(null, dto.getTitle(), dto.getContent(), currentUser);

        log.info("생성 변환 이전(Entity) 2 : {}",article);

        // 4.엔티티 저장
        Article createdArticle = articleRepository.save(article);

        // 5.생성된 Article 객체를 DTO로 변환하여 반환
        return ArticleDto.fromEntity(createdArticle);
    }




    // 3. 수정
    @Transactional
    public ArticleDto update(Long id, ArticleDto dto) {

            // 1.현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = userService.getCurrentUser();

        log.info("로그3 : {}",currentUser);
            // 2.ID로 기존 엔티티 조회
        Article target = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));

            // 3.엔티티 수정
        Article articleToUpdate = new Article(dto.getId(), dto.getTitle(), dto.getContent(), null);

        // 4게시글 작성자와 현재 로그인한 사용자 비교 하거나 Admin 아이디 인지 확인
        if (currentUser.getRole() == Role.ADMIN || target.getUser().equals(currentUser)) {
            target.patch(articleToUpdate);  // user는 변경하지 않음

            // 5.엔티티 저장
            Article updatedArticle = articleRepository.save(target);

            // 6.엔티티를 DTO로 변환하여 반환
            return ArticleDto.fromEntity(updatedArticle);
        } else {
            throw new IllegalStateException("수정 권한이 없습니다.");
        }
    }


    //    4. 삭제
    @Transactional
    public ArticleDto delete(Long id) {
        // 1.현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = userService.getCurrentUser();

        // 2. 대상 게시글을 찾습니다. 찾을 수 없으면 예외를 발생시킵니다.
        Article target = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));

        // 3.게시글 작성자와 현재 로그인한 사용자 비교
        if (!(currentUser.getRole() == Role.ADMIN) && !target.getUser().equals(currentUser)) {
            throw new IllegalStateException("You do not have permission to delete this article.");
        }

        //6.Soft Delete 게시글하고 댓글하고 동시에 삭제 될때

        // 4. 게시글에 연관된 모든 댓글을 조회합니다.
        List<Comment> comments = commentRepository.findByArticleId(id);

        // 5. 현재 시간을 가져옵니다.
        LocalDateTime now = LocalDateTime.now();

        // 6.각 댓글의 deletedAt을 확인하고, 이미 설정된 경우 업데이트하지 않습니다.
        comments.forEach(comment -> {
            if (comment.getDeletedAt() == null) { // deletedAt이 null인 경우에만 현재 시간으로 설정
                comment.setDeletedAt(now);
                commentRepository.save(comment); // 각 댓글을 업데이트합니다.
            }
        });

        // 7.게시글의 deletedAt을 현재 시간으로 설정하고 저장합니다.
        target.setDeletedAt(now);
        articleRepository.save(target);

        // 8.삭제된 게시글 정보를 DTO로 변환하여 반환합니다.
        return ArticleDto.fromEntity(target);
    }


}

