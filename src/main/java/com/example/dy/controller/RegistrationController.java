package com.example.dy.controller;

// 'controller' 패키지에 속한 클래스입니다. 웹 요청에 대한 처리를 담당하는 역할입니다.

import com.example.dy.entity.Role;
import com.example.dy.entity.User;
import com.example.dy.repository.RoleRepository;
import com.example.dy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
// Java의 내장 클래스입니다. 현재 사용자의 정보를 담고 있습니다.

@Controller
public class RegistrationController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;  // roleRepository 선언
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // UserRepository와 BCryptPasswordEncoder의 객체를 멤버 변수로 선언하였습니다.

    @Autowired
    public RegistrationController(UserRepository userRepository,
                                  RoleRepository roleRepository, // 생성자에 RoleRepository 추가
                                  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository; // RoleRepository 초기화
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
                               @RequestParam(name = "email") String email,
                               Model model) {
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("usernameError", "Username already exists.");
            return "register";
        }

        Role userRole = roleRepository.findByName("USER"); // "USER" 역할을 찾습니다.
        if (userRole == null) {  // 만약 "USER" 역할이 없다면, 생성합니다.
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(Arrays.asList(userRole));  // 찾은 또는 생성한 역할을 설정합니다.
        userRepository.save(user);

        return "redirect:/login";
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