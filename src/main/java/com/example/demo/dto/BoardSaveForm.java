package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardSaveForm {
    @NotBlank
    String name;
    @NotBlank
    String writer;
    String content;
}
