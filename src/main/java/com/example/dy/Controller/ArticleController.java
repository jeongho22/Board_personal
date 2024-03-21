package com.example.dy.Controller;

import com.example.dy.Domain.Article;
import com.example.dy.Domain.User;
import com.example.dy.Dto.ArticleDto;
import com.example.dy.Dto.CommentDto;
import com.example.dy.Repository.ArticleRepository;
import com.example.dy.Service.ArticleService;
import com.example.dy.Service.CommentService;
import com.example.dy.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j                                          //  로깅을 위한 어노테이션
public class ArticleController {

    // 생성자 주입 방식
    private final ArticleRepository articleRepository;
    private final CommentService commentService;
    private final ArticleService articleService;
    private final UserService userService;


    public ArticleController(ArticleRepository articleRepository,
                             CommentService commentService,
                             ArticleService articleService,
                             UserService userService) {
        this.articleRepository = articleRepository;
        this.commentService = commentService;
        this.articleService = articleService;
        this.userService = userService;
    }





    // 1. CREATE(생성)

    @GetMapping("/articles/new")
    public String newArticleForm() {
        return "articles/new";
    }


    @PostMapping("/articles/create")
    public String create(ArticleDto dto, RedirectAttributes rttr) {
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

        log.info("수정 entity 아이디 : {}",articleEntity);
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
        ArticleDto articleDto  = articleService.show(id);
        List<CommentDto> commentsDtos = commentService.comments(id);

        // 현재 사용자 정보 가져오기
        User currentUser = userService.getCurrentUser();

        //2. 가져온 데이터를 모델에 등록!
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("article", articleDto);
        model.addAttribute("commentDtos", commentsDtos);


        //3. 보여줄 페이지를 설정!
        return "articles/show";

    }


    // 3-2. 인덱스 화면 조회
    @GetMapping("/articles")
    public String index(Model model,
                        @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                        @RequestParam(required = false) String searchType,
                        @RequestParam(required = false) String searchKeyword) {


        Page<ArticleDto> articlePage = articleService.index(searchType, searchKeyword, pageable);
        // 현재 사용자 정보 가져오기
        User currentUser = userService.getCurrentUser();

        model.addAttribute("currentUser", currentUser);
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