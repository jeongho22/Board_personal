package com.example.dy.Service;


import com.example.dy.Domain.Article;
import com.example.dy.Domain.constant.SearchType;
import com.example.dy.Dto.ArticleFormDto;
import com.example.dy.Repository.ArticleRepository;
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

    @Autowired //DI
    private ArticleRepository articleRepository;


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
    public Article show(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));

        //id를 가진 Article 객체를 데이터베이스에서 찾아서 반환하되, 만약 그런 객체가 없으면 null을 반환
        if (article != null) {
            log.info(article.toString());
            articleRepository.incrementViewCount(id);
            log.info(article.toString());
            return articleRepository.save(article);
        }
        return null;
    }


    //    2. 생성

    public Article create(ArticleFormDto dto) {

        log.info("생성 과정1 : {}", dto);

        //1. dto를 -> entity 변환
        Article article =dto.toEntity();
        log.info("생성 과정2 : {}", article);

        //2. 기존에 getId() 가 있다면 null 반환
        if (article.getId()!=null){
            return null;
        }

        return articleRepository.save(article);
    }






    //    3. 수정
    public Article update(Long id, ArticleFormDto dto) {

        log.info("업데이트 과정0 : {}", dto);


        //1. DTO -> 엔티티 변환하지만, view는 제외하고 변환... 하지만 view역시 entity에 있어서 view 값은 0으로 반환
        Article article = dto.toEntity();
        log.info("업데이트 과정1 : {}", article);

        // 2.데이터 베이스 에서 타겟 조회
        Article target = articleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        log.info("업데이트 과정2 : {}", target);

        // 3.기존 데이터 베이스 타겟에서  변경된 게시글 업데이트
        target.patch(article);
        log.info("업데이트 과정3 : {}", target);

        // 4.저장
        return articleRepository.save(target);
    }




    //    4. 삭제
    public Article delete(Long id) {
        // 1.대상 찾기
        Article target = articleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        log.info("삭제 대상 : {}", target);

        //2. 대상을 삭제한다 !
        if (target!=null) {                                // null 이 아니라면 즉, 비어있지않다면 삭제
            articleRepository.delete(target);
        }

        //3. 반환
        return target;
    }



//    @Transactional
//    public List<Article> createArticles(List<ArticleFormDto> dtos) {
//        // dto 묶음을 entity 묶음으로 변환
//        List<Article> articleList = dtos.stream()
////                .map(dto -> dto.toEntity()) 같은거
//                .map(ArticleFormDto::toEntity)
//                .collect(Collectors.toList()); // 리스트로 반환
//
//        // entity 묶음을 DB로 저장
//        articleRepository.saveAll(articleList); // 한줄로 가능 성능이 한번에 저장하기때문에 조금 더 뛰어남... 아래는 계속 반복하면서 호출해야함...
////        articleList.stream()
////                .forEach(article -> articleRepository.save(article));
//        // 강제 예외 발생
//        articleRepository.findById(-1L).orElseThrow(
//                () -> new IllegalArgumentException("결제 실패!")
//        );
//        // 결과값 반환
//        return articleList;
//    }

}

