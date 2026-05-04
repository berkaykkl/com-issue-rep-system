package com.proje.controller;

import com.proje.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RankingController {

    private final UserService userService;

    public RankingController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/ranking")
    public String ranking(Model model) {
        model.addAttribute("users", userService.ranking());
        return "ranking";
    }
}
