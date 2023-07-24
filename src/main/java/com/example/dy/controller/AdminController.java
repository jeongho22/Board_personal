package com.example.dy.controller;

import com.example.dy.entity.User;
import com.example.dy.repository.UserRepository;
import com.example.dy.serivce.UserApproveService;
import com.example.dy.serivce.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final UserApproveService userApproveService;

    @Autowired
    public AdminController(UserRepository userRepository, UserApproveService userApproveService) {
        this.userRepository = userRepository;
        this.userApproveService = userApproveService;
    }



    @GetMapping("/unapproved-users")
    @ResponseBody
    public List<User> getUnapprovedUsers() {
        return userRepository.findByApproved(false);
    }



    @PutMapping("/approve/{userId}")
    @ResponseBody
    public ResponseEntity<?> approveUser(@PathVariable int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            if (user.isApproved()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with ID " + userId + " is already approved.");
            } else {
                // 이메일 토큰 인증이 먼저 되어야만 승인 처리가 됩니다.
                if (user.isEmailVerified()) {
                    userApproveService.approveUser(userId);
                    return ResponseEntity.ok("User with ID " + userId + " approved.");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with ID " + userId + " is not verified by email.");
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with ID " + userId + " does not exist.");
        }
    }

    @GetMapping("/approve")
    public String approvePage(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        boolean isAdmin = userApproveService.isAdmin(principal);

        if (isAdmin) {
            model.addAttribute("users", userRepository.findByApproved(false));
            return "Approve";
        } else {
            return "access-denied";
        }
    }





}
