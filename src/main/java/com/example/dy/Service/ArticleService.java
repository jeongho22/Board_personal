package com.example.dy.Service;


import com.example.dy.Domain.Article;
import com.example.dy.Domain.User;
import com.example.dy.Domain.constant.Role;
import com.example.dy.Domain.constant.SearchType;
import com.example.dy.Dto.ArticleDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;



@Slf4j
@Service // 서비스 선언!(서비스 객체를 스프링부트에 생성)
public class ArticleService {


    private final ArticleRepository articleRepository;
    private final UserRepository userRepository; // UserRepository 주입
    private final UserService userService;
    public ArticleService(ArticleRepository articleRepository,UserRepository userRepository,UserService userService) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    //    1.전체 조회
    @Transactional
    public Page<Article> index(String searchType, String searchKeyword, Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return articleRepository.findAll(pageable);
        }

        SearchType type = SearchType.valueOf(searchType.toUpperCase());
        // searchType 변수에 저장된 문자열을 모두 대문자로 변환합니다.
        // 이는 SearchType 열거형(enum)이 대문자로 정의된 경우에 일치시키기 위한 것입니다.

        return switch (type) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword, pageable);
        };
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
        // 1현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = userService.getCurrentUser(); // 현재 로그인한 사용자 정보 가져오기

        // 2.Dto를 사용하여 엔티티 생성
        Article article = new Article(null, dto.getTitle(), dto.getContent(), currentUser);

        log.info("생성 변환 이전(Entity) 2 : {}",article);

        // 3.엔티티 저장
        Article createdArticle = articleRepository.save(article);

        // 4.생성된 Article 객체를 DTO로 변환하여 반환
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
    public ArticleDto delete(Long id) {

        // 1.현재 로그인한 사용자의 인증 정보 가져오기
        User currentUser = userService.getCurrentUser(); // 현재 로그인한 사용자 정보 가져오기

        // 2.대상 찾기 없으면 에러발생
        Article target = articleRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));

        // 3.게시글 작성자와 현재 로그인한 사용자 비교
        if (!(currentUser.getRole() == Role.ADMIN) && !target.getUser().equals(currentUser)) {
            throw new IllegalStateException("댓글 수정 실패! 자신이 작성한 댓글만 수정할 수 있습니다.");
        }
        log.info("게시글 삭제 대상(Entity) 1: {}", target);

        // 4. 대상을 삭제한다 !
        articleRepository.delete(target);

        // 5. 대상 변환 삭제 반환
        return ArticleDto.fromEntity(target);
    }

}

