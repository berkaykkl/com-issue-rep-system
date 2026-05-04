package com.proje.controller;

import com.proje.dto.ProblemForm;
import com.proje.entity.Problem;
import com.proje.entity.User;
import com.proje.service.ProblemService;
import com.proje.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/problem")
public class ProblemController {

    private final ProblemService problemService;
    private final UserService userService;

    public ProblemController(ProblemService problemService, UserService userService) {
        this.problemService = problemService;
        this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String keyword,
                       Authentication authentication,
                       Model model) {
        User currentUser = userService.findByUsername(authentication.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("problems", problemService.findForUser(currentUser, keyword));
        model.addAttribute("q", keyword);
        return "problem/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("problemForm", new ProblemForm());
        model.addAttribute("mode", "create");
        return "problem/form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("problemForm") ProblemForm form,
                      BindingResult bindingResult,
                      Authentication authentication,
                      Model model,
                      RedirectAttributes redirectAttributes) throws IOException {
        validateImage(form.getImage(), bindingResult, true);
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            return "problem/form";
        }

        User currentUser = userService.findByUsername(authentication.getName());
        problemService.create(form, currentUser);
        redirectAttributes.addFlashAttribute("success", "Bildirim olusturuldu ve onay bekliyor.");
        return "redirect:/problem";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Authentication authentication, Model model) {
        User currentUser = userService.findByUsername(authentication.getName());
        Problem problem = problemService.findById(id);
        requireOwnerOrAdmin(currentUser, problem);

        model.addAttribute("problem", problem);
        model.addAttribute("problemForm", problemService.toForm(problem));
        model.addAttribute("mode", "edit");
        return "problem/form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("problemForm") ProblemForm form,
                       BindingResult bindingResult,
                       Authentication authentication,
                       Model model,
                       RedirectAttributes redirectAttributes) throws IOException {
        User currentUser = userService.findByUsername(authentication.getName());
        Problem problem = problemService.findById(id);
        requireOwnerOrAdmin(currentUser, problem);

        validateImage(form.getImage(), bindingResult, false);
        if (bindingResult.hasErrors()) {
            model.addAttribute("problem", problem);
            model.addAttribute("mode", "edit");
            return "problem/form";
        }

        problemService.update(id, form);
        redirectAttributes.addFlashAttribute("success", "Bildirim guncellendi.");
        return currentUser.isAdmin() ? "redirect:/admin/problems" : "redirect:/problem";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        User currentUser = userService.findByUsername(authentication.getName());
        Problem problem = problemService.findById(id);
        requireOwnerOrAdmin(currentUser, problem);

        problemService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Bildirim silindi.");
        return currentUser.isAdmin() ? "redirect:/admin/problems" : "redirect:/problem";
    }

    private void validateImage(MultipartFile image, BindingResult bindingResult, boolean required) {
        if (required && (image == null || image.isEmpty())) {
            bindingResult.rejectValue("image", "image.required", "Fotograf yuklemek zorunludur.");
            return;
        }

        if (image != null && !image.isEmpty()) {
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                bindingResult.rejectValue("image", "image.type", "Yalnizca gorsel dosyalari yuklenebilir.");
            }
        }
    }

    private void requireOwnerOrAdmin(User currentUser, Problem problem) {
        if (!problemService.canManage(currentUser, problem)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bu bildirim icin yetkiniz yok.");
        }
    }
}
