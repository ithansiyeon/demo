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
    private String is_top;

    @Builder
    public BoardEditForm(String name, String writer, String content, String is_top) {
        this.name = name;
        this.writer = writer;
        this.content = content;
        this.is_top = is_top;
    }

    public BoardEditForm toEntity() {
        return BoardEditForm.builder()
                .name(name)
                .writer(writer)
                .content(content)
                .is_top(is_top).build();
    }

    public BoardEditForm() {
    }
}
