package com.example.dy.controller;

import com.example.dy.serivce.UserPrincipal;
import com.example.dy.service.UserTableService;
import org.mariadb.jdbc.internal.logging.Logger;
import org.mariadb.jdbc.internal.logging.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserTableController {

    private final UserTableService userTableService;

    private static final Logger logger = LoggerFactory.getLogger(UserTableController.class);

    public UserTableController(UserTableService userTableService) {
        this.userTableService = userTableService;
    }



    @GetMapping("/users")
    public String listUsers(Model model, @AuthenticationPrincipal UserPrincipal principal) {

        boolean isAdmin = userTableService.isAdmin(principal);

        if(isAdmin) {
            model.addAttribute("users", userTableService.findApprovedUsers());
            return "userList";
        } else {
            return "access-denied";
        }
    }



    @PostMapping("/users/delete")
    public String deleteSelectedUsers(@RequestParam List<Integer> userIds, RedirectAttributes redirectAttrs) {
        try {
            userTableService.deleteUsers(userIds);
            redirectAttrs.addFlashAttribute("success", "Selected users have been deleted successfully");
        } catch (Exception e) {
            logger.error("Error occurred while trying to delete selected users", e);
            redirectAttrs.addFlashAttribute("error", "An error occurred while trying to delete selected users");
        }
        return "redirect:/users";
    }






}
