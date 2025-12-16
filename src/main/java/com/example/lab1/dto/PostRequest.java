package com.example.lab1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

}

