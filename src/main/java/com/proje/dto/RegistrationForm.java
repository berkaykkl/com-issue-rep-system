package com.proje.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "Kullanıcı adı boş bırakılamaz.")
    @Size(min = 3, max = 30, message = "Kullanıcı adı 3 ile 30 karakter arasında olmalıdır.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Kullanıcı adı sadece harf, rakam, nokta, tire ve alt çizgi içerebilir.")
    private String username;

    @NotBlank(message = "Şifre boş bırakılamaz.")
    @Size(min = 6, max = 60, message = "Şifre en az 6 karakter olmalıdır.")
    private String password;

    @NotBlank(message = "Şifre tekrarı boş bırakılamaz.")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean passwordsMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
