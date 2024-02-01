package com.example.dy.Service;


import com.example.dy.Domain.Article;
import com.example.dy.Domain.constant.SearchType;
import com.example.dy.Dto.ArticleDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;



@Slf4j
@Service // 서비스 선언!(서비스 객체를 스프링부트에 생성)
public class ArticleService {

//    @Autowired // 필드 주입 방식 DI
//    private ArticleRepository articleRepository;


    private final ArticleRepository articleRepository;
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
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
        // 1.Dto를 사용하여 엔티티 생성
        Article article = new Article(null, dto.getTitle(), dto.getContent());
        log.info("생성 변환 이전(Entity) 2 : {}",article);
        // 2.엔티티 저장
        Article createArticle = articleRepository.save(article);
        // 3.엔티티를 DTO로 변환하여 반환
        return ArticleDto.fromEntity(createArticle);
    }





    //    3. 수정
    @Transactional
    public ArticleDto update(Long id, ArticleDto dto) {
        // 1.ID로 기존 엔티티 조회
        //   예외가 있으면 다음으로 안넘어감.
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        log.info("수정 이전(Entity) 0 : {}",article);

        // 2.엔티티 수정
        Article articleToUpdate = new Article(dto.getId(), dto.getTitle(), dto.getContent());
        log.info("수정 변환 이전(Entity) 1 : {}",articleToUpdate);

        article.patch(articleToUpdate);

        log.info("수정 변환 이전(Entity) 2 : {}",article);


        // 3.엔티티 저장
        Article updatedArticle = articleRepository.save(article);

        // 4.엔티티를 DTO로 변환하여 반환
        return ArticleDto.fromEntity(updatedArticle);
    }





    //    4. 삭제
    public ArticleDto delete(Long id) {

        // 1.대상 찾기 없으면 에러발생
        Article target = articleRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        log.info("게시글 삭제 대상(Entity) 1: {}", target);

        // 2. 대상을 삭제한다 !
        articleRepository.delete(target);

        // 3. 대상 변환 삭제 반환
        return ArticleDto.fromEntity(target);
    }


}

