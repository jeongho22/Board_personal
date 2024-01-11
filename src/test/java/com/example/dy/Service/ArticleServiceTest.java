package com.example.dy.Service;

import com.example.dy.Domain.Article;
import com.example.dy.Dto.ArticleFormDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest  // 해당 클래스는 스프링 부트와 연동되어 테스팅된다
class ArticleServiceTest {

    @Autowired ArticleService articleService;
//    @Test
//    void index() {
//        // 예상 시나리오
//
//        Article a = new Article(14L, "김정호", "김정호");
//        Article b = new Article(40L, "생성", "생성");
//        Article c = new Article(47L, "공부중입니다", "240104");
//        List<Article> expected = new ArrayList<Article>(Arrays.asList(a, b, c));
//
//
//
//        // 실제
//
//        List<Article> articles =articleService.index();
//
//        // 비교
//
//        assertEquals(expected.toString(),articles.toString());
//
//    }

    @Test
    void show_success() {

        //예상
        Long id= 14L;
        Article expected = new Article(id,"김정호","수정");


        //실제
        Article article =articleService.show(id);

        //비교


        assertEquals(expected.toString(),article.toString());



    }


    @Test
    void show_fail() {

        //예상
        Long id= 50L;
        Article expected = null;  // articleService 쪽에 show에서 null 이라 선언했기 때문에


        //실제
        Article article =articleService.show(id);

        //비교

        assertEquals(expected,article);
    }

//    @Test
//    @Transactional
//    void create_성공____title과_content만_있는_dto_입력() {
//        // 예상
//        String title = "라라라라";
//        String content = "4444";
//        ArticleFormDto dto = new ArticleFormDto(null, title, content);
//        Article expected = new Article(58L, title, content);
//        // 실제
//        Article article = articleService.create(dto);
//        // 비교
//        assertEquals(expected.toString(), article.toString());
//    }
    @Test
    @Transactional
    void create_실패____id가_포함된_dto_입력() {
        // 예상
        String title = "라라라라";
        String content = "4444";
        ArticleFormDto dto = new ArticleFormDto(4L, title, content);
        Article expected = null;
        // 실제
        Article article = articleService.create(dto);
        // 비교
        assertEquals(expected, article);
    }
}