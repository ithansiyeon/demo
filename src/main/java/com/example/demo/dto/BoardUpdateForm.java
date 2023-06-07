package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardUpdateForm {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String writer;
    private String content;
    private Boolean is_top;
}
