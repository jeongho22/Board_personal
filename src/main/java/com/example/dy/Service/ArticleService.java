package com.example.dy.Service;


import com.example.dy.Domain.Article;
import com.example.dy.Dto.ArticleFormDto;
import com.example.dy.Repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import java.util.stream.Collectors;


@Slf4j
@Service // 서비스 선언!(서비스 객체를 스프링부트에 생성)
public class ArticleService {

    @Autowired //DI
    private ArticleRepository articleRepository;

    public List<Article> index() {
        // ** List findAll() 하는 기능은 CrudRepository 에는 없음.
        return articleRepository.findAll(); // 앞쪽에서 List로 받아서 메서드생성
    }



    @Transactional
    public Article show(Long id) {
        Article article = articleRepository.findById(id).orElse(null);

        //id를 가진 Article 객체를 데이터베이스에서 찾아서 반환하되, 만약 그런 객체가 없으면 null을 반환
        if (article != null) {
            log.info(article.toString());
            articleRepository.incrementViewCount(id);
            log.info(article.toString());
            return articleRepository.save(article);
        }
        return null;


    }

//    @Transactional
//    public Article show(Long id) {
//        Optional<Article> articleOptional = articleRepository.findById(id);
//
//        if (articleOptional.isPresent()) {
//            Article article = articleOptional.get();
//            article.increaseViewCount();
//            return articleRepository.save(article);
//        }
//        return null;
//    } // java 8버전에서는 optional 이런식으로도 바꾸기가능
        // Optional 객체 자체는 절대 null이 아닙니다. 대신, Optional 안에 값이 있는지 없는지를 확인해야 합니다.
        // Optional.isPresent() 메서드를 사용하여 Optional 객체 안에 값이 있는지 확인할 수 있습니다.


    public Article create(ArticleFormDto dto) {
        Article article =dto.toEntity(); // dto 변환
        if(article.getId() !=null){ // 아이디가 null 이 아니라면 null 반환해라... 이건 수정이아니라 생성 메서드기때문에
            return null;
        }
        return articleRepository.save(article);
    }


    public Article update(Long id, ArticleFormDto dto) {
        //1. DTO -> 엔티티 변환하지만, view는 제외
        Article article = dto.toEntity();

        //2. 타겟 조회
        Article target = articleRepository.findById(id).orElse(null); // 저장된 id 가 있는가 없는가

        //3. 잘못된 요청 처리
        if (target == null || id.equals(article.getId())) {
            log.info("잘못된 요청! id: {}, article: {}", id, article.toString());
            return null;
        }

        //4. 현재 'view' 값 저장
        long currentViews = target.getView(); // getter 임

        //5. 업데이트 (view 제외)
        target.patch(article);

        //6. 'view' 값을 복원
        target.setView(currentViews); // setter 임

        //7. 저장
        return articleRepository.save(target);
    }



    public Article delete(Long id) {
        // 대상 찾기
        Article target = articleRepository.findById(id).orElse(null);
        // 잘못된 요청 처리
        if (target == null) {
            return null;
        }
        // 대상 삭제
        articleRepository.delete(target);
        return target;
    }



    @Transactional
    public List<Article> createArticles(List<ArticleFormDto> dtos) {
        // dto 묶음을 entity 묶음으로 변환
        List<Article> articleList = dtos.stream()
//                .map(dto -> dto.toEntity()) 같은거
                .map(ArticleFormDto::toEntity)
                .collect(Collectors.toList()); // 리스트로 반환

        // entity 묶음을 DB로 저장
        articleRepository.saveAll(articleList); // 한줄로 가능 성능이 한번에 저장하기때문에 조금 더 뛰어남... 아래는 계속 반복하면서 호출해야함...
//        articleList.stream()
//                .forEach(article -> articleRepository.save(article));
        // 강제 예외 발생
        articleRepository.findById(-1L).orElseThrow(
                () -> new IllegalArgumentException("결제 실패!")
        );
        // 결과값 반환
        return articleList;
    }
}