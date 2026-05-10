package com.proje.controller;

import com.proje.entity.User;
import com.proje.service.ProblemService;
import com.proje.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final ProblemService problemService;

    public HomeController(UserService userService, ProblemService problemService) {
        this.userService = userService;
        this.problemService = problemService;
    }

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User currentUser = userService.findByUsername(authentication.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userProblemCount", problemService.findForUser(currentUser, null).size());
        model.addAttribute("approvedProblems", problemService.findApprovedFeed());
        return "dashboard";
    }
}
