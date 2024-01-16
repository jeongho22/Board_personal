package com.example.dy.Controller;

import com.example.dy.Domain.Article;
import com.example.dy.Dto.ArticleFormDto;
import com.example.dy.Dto.CommentDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Service.ArticleService;
import com.example.dy.Service.CommentService;
import com.example.dy.Service.PaginationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@Slf4j                                          //  로깅을 위한 어노테이션
public class ArticleController {

    @Autowired                                  //객체를 안만들어도된다 스프링이 자동으로 만들어줌...
    private ArticleRepository articleRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private PaginationService paginationService;



    // 1. CREATE(생성)

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }

    @PostMapping("/articles/create") // 글쓰기를 누르면 Submit 여기로가는데 redirect 해줌... 아래에서
    public String createArticle(ArticleFormDto form) {  // dto 를 form 이라고 명시해주고 dto의 toString 함수를 불러옴.
        log.info(form.toString());

        //1. dto를 -> entity 변환
        //엔티티
        Article articleEntity = form.toEntity();
        log.info(articleEntity.toString());


        //2. 엔티티를 db로 저장한다.

        Article saved = articleRepository.save(articleEntity); // dto에서 entity로 변환한걸 save
        log.info(saved.toString());


        //3. 페이지로 리다이렉트 한다.
        return "redirect:/articles/" + saved.getId();
//        articleEntity.getId()를 사용하는 경우는
//        엔티티가 데이터베이스에 저장되기 전의 ID 값을 사용하는 것이고,
//        saved.getId()를 사용하는 경우는
//        데이터베이스에 저장된 후의 정확한 ID 값을 사용하는 것입니다.
    }

    // 2. UPDATE(수정)

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){// 위에 경로이름과 같아야함
        // 1.수정할 데이터를 db에서 가져와야함.

        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 2. 모델 등록
        model.addAttribute("article",articleEntity);

        log.info(articleEntity.toString());
        // article/edit 페이지 에서만 사용 가능

        // 3.뷰 페이지 설정
        return "articles/edit";
    }


    @PostMapping("/articles/update")
    public String update(ArticleFormDto form) {  // dto 를 form 이라고 명시해주고 dto의 toString 함수를 불러옴.

        log.info(form.toString());

        //1. dto를 entity로 변환하는작업을 거쳐야함


        Article articleEntity = form.toEntity();    // dto를 -> entity로 변환
                                                    // ArticleFormDto에는 view 필드가 없기 때문에, toEntity 메소드를 통해 생성된 Article 객체의 view 필드는 기본값으로 설정됩니다.
                                                    // 이 경우, Java에서 기본값은 0이 됩니다.
        log.info(articleEntity.toString());         // 따라서, articleRepository.save(articleEntity)를 호출하면, 데이터베이스에 저장된 기존 게시글의 view가 0으로 덮어씌워집니다.


        //2. 엔티티를 db로 저장한다.

        //2-1. DB에서 기존 데이터를 가져온다. 있으면 가져오고, 없으면 null 반환
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        //아티클 안에
        log.info(Objects.requireNonNull(target).toString());

        //2-2. 기존 데이터에 값을 갱신한다.
        if (target!=null) {               // null 이 아니라면 즉, 기존 데이터가 있다면
            // target의 title과 content를 업데이트
            target.setTitle(articleEntity.getTitle());
            target.setContent(articleEntity.getContent());

//              articleRepository.save(articleEntity); // 문제의 부분 바로 DTO에서 ENTITY로 변환된곳에서 자동으로 기본값 0이 되었기 때문에...
                                                        //  문제 발생 그래서 전체가 아니라 TITIE과 CONTENT 만 업데이트

            // target을 저장 (이때 view는 기존 값으로 유지됨)
            articleRepository.save(target);
        }
        log.info(Objects.requireNonNull(target).toString());

        //3. 수정페이지로 리다이렉트 한다.
        return "redirect:/articles/" + articleEntity.getId();


    }



    // 3. READ(읽기)
    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id,
                       Model model){   // id타입을 가져온다 @pathvariable 통해
        log.info("id = " + id);

        //1.id로 데이터를 가져옴 !
        Article articleEntity  = articleService.show(id);
//        Article articleEntity = articleRepository.findById(id).orElse(null); Service에서 구현
        List<CommentDto> commentsDtos = commentService.comments(id);


        //2. 가져온 데이터를 모델에 등록!

        model.addAttribute("article", articleEntity);
        model.addAttribute("commentDtos", commentsDtos);



        //3. 보여줄 페이지를 설정!
        return "articles/show";

        //    조회수를 증가시키는 경우에는 일반적으로 게시글의
        //    전체 데이터를 업데이트(create,update) 하는 것이 아니라 특정 필드(여기서는 조회수)만을 변경합니다.
        //    따라서, ArticleFormDto를 사용하지 않고,
        //    대신 Article 엔티티의 ID만을 사용하여
        //    조회수를 증가시키는 것이 적절할 수 있습니다.

    }

    @GetMapping("/articles")
    public String index(Model model, Pageable pageable){
        // 1: 모든 Article을 가져온다. 전체목록이라... List로 담아야함.
        // ** List findAll() 하는 기능은 CrudRepository 에는 없음.
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
            rttr.addFlashAttribute("msg","삭제 완료"); // 한번쓰고 사라지는 플래쉬 휘발성 데이터
        }


        //3. 보여줄 페이지를 설정!
        return "redirect:/articles";
    }




}