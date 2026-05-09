package com.proje.controller;

import com.proje.entity.ProblemStatus;
import com.proje.service.ProblemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProblemService problemService;

    public AdminController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping("/problems")
    public String problems(@RequestParam(value = "q", required = false) String keyword,
                           @RequestParam(value = "status", required = false) ProblemStatus status,
                           Model model) {
        model.addAttribute("problems", problemService.findAll(keyword, status));
        model.addAttribute("statuses", ProblemStatus.values());
        model.addAttribute("q", keyword);
        model.addAttribute("selectedStatus", status);
        return "admin/problems";
    }

    @PostMapping("/problems/status")
    public String updateStatus(@RequestParam Long id,
                               @RequestParam ProblemStatus status,
                               @RequestParam(value = "q", required = false) String keyword,
                               @RequestParam(value = "filterStatus", required = false) ProblemStatus filterStatus,
                               RedirectAttributes redirectAttributes) {
        problemService.changeStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Bildirim durumu güncellendi.");
        if (keyword != null && !keyword.trim().isEmpty()) {
            redirectAttributes.addAttribute("q", keyword.trim());
        }
        if (filterStatus != null) {
            redirectAttributes.addAttribute("status", filterStatus);
        }
        return "redirect:/admin/problems";
    }
}
