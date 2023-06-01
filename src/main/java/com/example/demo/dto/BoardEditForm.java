package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class BoardEditForm {
    @NotBlank
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String writer;
    private String content;

    @Builder
    public BoardEditForm(String name, String writer, String content) {
        this.name = name;
        this.writer = writer;
        this.content = content;
    }

    public BoardEditForm toEntity() {
        return BoardEditForm.builder()
                .name(name)
                .writer(writer)
                .content(content).build();
    }

    @Override
    public String toString() {
        return "BoardEditForm{" +
                "name='" + name + '\'' +
                ", writer='" + writer + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public BoardEditForm() {
    }
}
