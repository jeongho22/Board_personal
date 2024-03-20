package com.example.dy.Controller;
import com.example.dy.Domain.User;
import com.example.dy.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
@Controller
public class WebController {


    private final UserService userService;
    public WebController(UserService userService) {
        this.userService = userService;
    }




    //1. 회원 가입
    @GetMapping("/signup")
    public String showSignUpForm() {
        return "user/signup"; // signup.html 페이지 반환
    }

    //2. 로그인 페이지
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model, HttpServletRequest request, HttpServletResponse response) {

        // 기존 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // JSESSIONID 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        if (error != null) {
            model.addAttribute("loginError", true);
        }

        return "login"; // 사용자 정의 로그인 페이지 반환

    }

    // 3. 유저 조회 화면
    @GetMapping("/user/information")
    public String UserTable(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "user/usertable";
    }


    // 4. 마이 페이지
    @GetMapping("/user/my_page")
    public String MyPage(Model model) {
        // 현재 사용자 정보 가져오기
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);

        return "user/mypage";
    }

    // 5. 마이 페이지 정보 수정
    @GetMapping("user/my_page/edit")
    public String UserEdit(Model model) {
        // 현재 사용자 정보 가져오기
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);

        return "user/useredit";
    }


    //6. 북마크 조회
    @GetMapping("/bookmark")
    public String BookMark(Model model) {

        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "bookmark/bookmark";
    }


    //7. 인기 게시물 조회
    @GetMapping("/articles/popular")
    public String ArticlePopular(Model model) {

        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        return "articles/popular";
    }

}
