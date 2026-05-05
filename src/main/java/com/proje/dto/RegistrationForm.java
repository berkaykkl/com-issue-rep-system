package com.proje.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegistrationForm {

    @NotBlank(message = "Kullanici adi bos birakilamaz.")
    @Size(min = 3, max = 30, message = "Kullanici adi 3 ile 30 karakter arasinda olmalidir.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Kullanici adi sadece harf, rakam, nokta, tire ve alt cizgi icerebilir.")
    private String username;

    @NotBlank(message = "Sifre bos birakilamaz.")
    @Size(min = 6, max = 60, message = "Sifre en az 6 karakter olmalidir.")
    private String password;

    @NotBlank(message = "Sifre tekrari bos birakilamaz.")
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
