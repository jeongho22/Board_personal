package com.example.dy.controller;

// 'controller' 패키지에 속한 클래스입니다. 웹 요청에 대한 처리를 담당하는 역할입니다.

import com.example.dy.entity.Role;
import com.example.dy.entity.User;
import com.example.dy.repository.RoleRepository;
import com.example.dy.repository.UserRepository;
import com.example.dy.serivce.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
// Java의 내장 클래스입니다. 현재 사용자의 정보를 담고 있습니다.

@Controller
public class RegistrationController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;  // roleRepository 선언
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private MailService mailService; // 추가: MailService 선언

    // UserRepository와 BCryptPasswordEncoder의 객체를 멤버 변수로 선언하였습니다.

    // 생성자를 통해 UserRepository와 BCryptPasswordEncoder의 의존성을 주입합니다.
    @Autowired
    public RegistrationController(UserRepository userRepository,
                                  RoleRepository roleRepository,
                                  BCryptPasswordEncoder bCryptPasswordEncoder,
                                  MailService mailService) { // 추가: 생성자에 MailService 추가
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.mailService = mailService; // 추가: MailService 초기화
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

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(Arrays.asList(userRole));
        user.setEmailVerificationToken(UUID.randomUUID().toString());
        user.setEmailVerified(false);

        // 이메일 인증 토큰 생성 및 저장
        userRepository.save(user);


        String subject = "Please verify your email";

        // 이메일 인증 링크 생성
        String verificationLink = "http://localhost:8090/verify-email?token=" + user.getEmailVerificationToken();
        String message = "Thank you for registering. Click the below link to verify your email: " + verificationLink;

        // 메일 서비스를 통해 이메일 인증 메일 발송

        mailService.sendEmail(user.getEmail(), subject, message);

        return "redirect:/login";
    }


    @GetMapping("/verify-email")
    public String verifyUser(@RequestParam(name = "token") String token, Model model) {
        Optional<User> optionalUser = userRepository.findByEmailVerificationToken(token);
        if (!optionalUser.isPresent()) {
            model.addAttribute("error", "Invalid token");
            return "error";
        }

        User user = optionalUser.get();
        user.setEmailVerified(true); // 인증되면 1로 바뀜
        user.setEmailVerificationToken(null); // 인증이 완료되면 토큰을 null로 설정하여 다시 사용하지 않도록 합니다.
        userRepository.save(user); // 저장
        

        return "redirect:/login"; // login으로 리다이렉션
    }



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

