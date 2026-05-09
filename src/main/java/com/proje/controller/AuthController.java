package com.proje.controller;

import com.proje.dto.RegistrationForm;
import com.proje.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registrationForm") RegistrationForm form,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (StringUtils.hasText(form.getUsername()) && userService.usernameExists(form.getUsername().trim())) {
            bindingResult.rejectValue("username", "username.exists", "Bu kullanıcı adı zaten kullanılıyor.");
        }

        if (!form.passwordsMatch()) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Şifreler eşleşmiyor.");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.registerUser(form);
        redirectAttributes.addFlashAttribute("success", "Kayıt başarılı. Şimdi giriş yapabilirsin.");
        return "redirect:/login";
    }
}
