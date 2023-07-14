package com.example.dy.controller;

// 'controller' 패키지에 속한 클래스입니다. 웹 요청에 대한 처리를 담당하는 역할입니다.

import com.example.dy.entity.User;
import com.example.dy.repository.UserRepository;
// 'entity'와 'repository' 패키지로부터 클래스를 import 하였습니다. User 클래스와 UserRepository 인터페이스를 사용합니다.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// Spring Framework에서 제공하는 클래스와 애노테이션입니다. 의존성 주입과 암호화를 위해 사용됩니다.

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
// Spring에서 제공하는 웹 관련 애노테이션과 클래스입니다. 컨트롤러 선언, 요청 매핑, 파라미터 바인딩, 모델 처리 등에 사용됩니다.

import java.security.Principal;
// Java의 내장 클래스입니다. 현재 사용자의 정보를 담고 있습니다.

@Controller
public class RegistrationController {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // UserRepository와 BCryptPasswordEncoder의 객체를 멤버 변수로 선언하였습니다.

    @Autowired
    public RegistrationController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 생성자를 통해 UserRepository와 BCryptPasswordEncoder의 의존성을 주입합니다.

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";  // 회원 가입 폼을 보여주는 뷰의 이름을 반환합니다.
    }

    // "/register" URL에 대한 GET 요청을 처리합니다.

    @PostMapping("/register")
    public String registerUser(@RequestParam(name = "username") String username,
                               @RequestParam(name = "password") String password,
                               @RequestParam(name = "email") String email) {  // 이메일 파라미터를 추가하였습니다.
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password)); // 비밀번호는 암호화하여 설정합니다.
        user.setEmail(email);  // 이메일을 설정합니다.
        userRepository.save(user); // 이름 ,이메일 , 비밀번호(암호화) 되어서 userRepository 저장

        return "redirect:/login";  // 회원 가입이 성공하면, 로그인 페이지로 리다이렉션합니다.
    }

    // "/register" URL에 대한 POST 요청을 처리합니다. 사용자가 제공한 정보로 새로운 User 객체를 생성하고 저장합니다.

    @GetMapping("/login")
    public String showLoginForm(Principal principal, Model model) {
        if (principal != null) { // 사용자가 이미 로그인한 상태인지 확인합니다.
            model.addAttribute("username", principal.getName()); // 현재 사용자의 이름을 모델에 추가합니다.
            return "logged_in"; // 로그인 상태를 나타내는 다른 뷰를 반환합니다.
        }

        return "login"; // 로그인하지 않았다면 로그인 폼을 보여주는 뷰를 반환합니다.
    }

    // "/login" URL에 대한 GET 요청을 처리합니다. 로그인 폼을 보여주거나, 이미 로그인한 경우 다른 뷰를 보여줍니다.
}




//용자의 등록과 로그인을 관리하는 컨트롤러입니다.
// 주요 기능으로는 회원 가입 폼 제공, 회원 가입 처리, 로그인 폼 제공 등이 있습니다.
// UserRepository를 통해 사용자 정보를 데이터베이스에 저장하고,
// BCryptPasswordEncoder를 사용하여 사용자의 비밀번호를 암호화합니다.