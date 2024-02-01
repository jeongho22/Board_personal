package com.example.dy.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/signup")
    public String showSignUpForm() {
        return "signup"; // signup.html 페이지 반환
    }
}
