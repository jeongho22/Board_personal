package com.example.dy.controller;

import com.example.dy.entity.User;
import com.example.dy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class PasswordController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public PasswordController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping("/change_password")
    public String showChangePasswordForm(Principal principal, Model model) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());

        if (!optionalUser.isPresent()) {
            return "error";  // If user does not exist, redirect to error page
        }

        User user = optionalUser.get();
        model.addAttribute("user", user);
        return "change_password";
    }

    @PostMapping("/change_password")
    public String changePassword(@RequestParam(name = "old_password") String oldPassword,
                                 @RequestParam(name = "new_password") String newPassword,
                                 @RequestParam(name = "new_password_confirm") String newPasswordConfirm,
                                 Principal principal, Model model) {

        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());

        if (!optionalUser.isPresent()) {
            return "error";  // If user does not exist, redirect to error page
        }

        User user = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("passwordError", true);
            return "change_password";
        }

        if (!newPassword.equals(newPasswordConfirm)) {
            model.addAttribute("passwordMismatch", true);
            return "change_password";
        }

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
        return "redirect:/list";
    }
}
