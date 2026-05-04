package com.proje.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProblemForm {

    @NotBlank(message = "Baslik bos birakilamaz.")
    @Size(max = 120, message = "Baslik en fazla 120 karakter olabilir.")
    private String title;

    @NotBlank(message = "Aciklama bos birakilamaz.")
    @Size(min = 10, max = 2000, message = "Aciklama 10 ile 2000 karakter arasinda olmalidir.")
    private String description;

    @NotBlank(message = "Kategori bos birakilamaz.")
    @Size(max = 80, message = "Kategori en fazla 80 karakter olabilir.")
    private String category;

    private MultipartFile image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
