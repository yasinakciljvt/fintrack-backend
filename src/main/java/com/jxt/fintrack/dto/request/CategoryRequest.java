package com.jxt.fintrack.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Kategori adı boş olamaz")
    private String name;

    private String icon;

    private String color;
}