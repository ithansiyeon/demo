package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardUpdateForm {
    @NotBlank
    private String name;
    @NotBlank
    private String writer;
    private String content;
}
