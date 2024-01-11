package com.example.dy.Controller;

import com.example.dy.Domain.Article;
import com.example.dy.Dto.ArticleFormDto;
import com.example.dy.Dto.CommentDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j                                          //  로깅을 위한 어노테이션
public class ArticleController {

    @Autowired                                  //객체를 안만들어도된다 스프링이 자동으로 만들어줌...
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;




    // 1. CREATE(생성)

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create")
    public String createArticle(ArticleFormDto form) {  // dto 를 form 이라고 명시해주고 dto의 toString 함수를 불러옴.
        log.info(form.toString());

        //1. dto를 변환하는작업을 거쳐야함 entity로..
        //엔티티
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());


        //2. 엔티티를 db로 저장한다.

        Article saved = articleRepository.save(articleEntity); // 위에서 만든거 save
        log.info(saved.toString());


        //3. 페이지로 리다이렉트 한다.
        return "redirect:/articles/" + articleEntity.getId();
    }
    // 2. UPDATE(수정)
    @PostMapping("/articles/update")
    public String updateArticle(ArticleFormDto form) {  // dto 를 form 이라고 명시해주고 dto의 toString 함수를 불러옴.
        log.info(form.toString());

        //1. dto를 entity로 변환하는작업을 거쳐야함

        //엔티티
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());

        //2. 엔티티를 db로 저장한다.
        //2-1. DB에서 기존 데이터를 가져온다.
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);

        //2-2. 기존 데이터에 값을 갱신한다.
        if (target!=null) {                                // null 이 아니라면 즉, 비어있지않다면
            articleRepository.save(articleEntity);
        }

        //3. 수정페이지로 리다이렉트 한다.
        return "redirect:/articles/" + articleEntity.getId();
    }


    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        // 1.수정할 데이터를 가져와야함.

        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 2. 모델 등록
        model.addAttribute("article",articleEntity);

        // 3.뷰 페이지 설정
        return "articles/edit";
    }

    // 3. READ(읽기)
    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id,
                       Model model){   // id타입을 가져온다 @pathvariable 통해
        log.info("id = " + id);

        //1.id로 데이터를 가져옴 !
        Article articleEntity = articleRepository.findById(id).orElse(null);
        List<CommentDto> commentsDtos = commentService.comments(id);


        //2. 가져온 데이터를 모델에 등록!

        model.addAttribute("article", articleEntity);
        model.addAttribute("commentDtos", commentsDtos);


        //3. 보여줄 페이지를 설정!
        return "articles/show";
    }

    @GetMapping("/articles")
    public String index(Model model){
        // 1: 모든 Article을 가져온다.

        List<Article> articleEntityList = articleRepository.findAll();

        // 2: 가져온 모든 Article 묶음을 뷰로 전달!
        model.addAttribute("articleList",articleEntityList);

        // 3: 뷰 페이지를 설정 !
        return "articles/index";
    }

//     4. Delete(삭제)
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id , RedirectAttributes rttr){   // id타입을 가져온다 @pathvariable 통해


        //1. 삭제 대상을 가져온다

        Article target = articleRepository.findById(id).orElse(null);
        log.info("삭제요청이 들어왔습니다: " + target);


        //2. 대상을 삭제한다 !
        if (target!=null) {                                // null 이 아니라면 즉, 비어있지않다면 삭제
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg","삭제 되었습니다"); // 한번쓰고 사라지는 플래쉬 휘발성 데이터
        }


        //3. 보여줄 페이지를 설정!
        return "redirect:/articles";
    }




}
