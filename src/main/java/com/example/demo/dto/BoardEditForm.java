package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(of = {"id","name","writer","is_top"})
public class BoardEditForm {
    @NotBlank
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String writer;
    private String content;
    private Boolean is_top;

    @Builder
    public BoardEditForm(Long id, String name, String writer, String content, Boolean is_top) {
        this.id = id;
        this.name = name;
        this.writer = writer;
        this.content = content;
        this.is_top = is_top;
    }

    public BoardEditForm() {
    }
}
