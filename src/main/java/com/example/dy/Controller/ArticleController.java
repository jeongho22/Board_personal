package com.example.dy.Controller;

import com.example.dy.Domain.Article;
import com.example.dy.Dto.ArticleDto;
import com.example.dy.Dto.CommentDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Service.ArticleService;
import com.example.dy.Service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
@Slf4j                                          //  로깅을 위한 어노테이션
public class ArticleController {

    // 생성자 주입 방식
    private final ArticleRepository articleRepository;
    private final CommentService commentService;
    private final ArticleService articleService;

    public ArticleController(ArticleRepository articleRepository, CommentService commentService, ArticleService articleService) {
        this.articleRepository = articleRepository;
        this.commentService = commentService;
        this.articleService = articleService;
    }





    // 1. CREATE(생성)

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }


    @PostMapping("/articles/create")
    public String createArticle(ArticleDto dto, RedirectAttributes rttr) {
        ArticleDto createdDto = articleService.create(dto);

        log.info("생성 변환 성공(Dto) 3 : {}",createdDto);

        rttr.addFlashAttribute("msg", "게시글 생성 완료");

        return "redirect:/articles/" + createdDto.getId();
    }


    // 2. UPDATE(수정)

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){// 위에 경로이름과 같아야함
        // 1.수정할 데이터를 db에서 가져와야함.

        Article articleEntity = articleRepository.findById(id).orElse(null);

        // 2. 모델 등록
        model.addAttribute("article",articleEntity);

        log.info(Objects.requireNonNull(articleEntity).toString());
        // article/edit 페이지 에서만 사용 가능

        // 3.뷰 페이지 설정
        return "articles/edit";
    }



    @PostMapping("/articles/update")
    public String update(Long id, ArticleDto dto, RedirectAttributes rttr) {
        ArticleDto updatedDto = articleService.update(id, dto);

        log.info("수정 변환 성공(Dto) 3 : {}",updatedDto);

        rttr.addFlashAttribute("msg", "게시글 수정 완료");
        return "redirect:/articles/" + updatedDto.getId();
    }



    // 3. READ(읽기)

    // 3-1. 단건 화면 조회
    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id,
                       Model model){   // id타입을 가져온다 @pathvariable 통해
        log.info("id = " + id);

        //1.id로 데이터를 가져옴 !
        ArticleDto articleEntity  = articleService.show(id);
        List<CommentDto> commentsDtos = commentService.comments(id);

        log.info("조회 변환 성공(Dto) 2 : {}",articleEntity);


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


    // 3-2. 인덱스 화면 조회
    @GetMapping("/articles")
    public String index(Model model,
                        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false) String searchType,
                        @RequestParam(required = false) String searchKeyword) {

        //        // 1: 모든 Article을 가져온다. 전체목록이라... List로 담아야함.
        //        // ** List findAll() 하는 기능은 CrudRepository 에는 없음.
        //        List<Article> articleEntityList = articleRepository.findAll();
        //
        //        // 2: 가져온 모든 Article 묶음을 뷰로 전달!
        //        model.addAttribute("articleList",articleEntityList);

        Page<Article> articlePage = articleService.index(searchType, searchKeyword, pageable);

        model.addAttribute("articlePage", articlePage);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);

        return "articles/index";
    }




    // 4. Delete(삭제)
    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {
        ArticleDto deleted = articleService.delete(id);
        log.info("2 .삭제 변환 성공(Dto) : {}",deleted);
        rttr.addFlashAttribute("msg", "게시글 삭제 완료: " + deleted.getTitle());
        return "redirect:/articles";
    }

}