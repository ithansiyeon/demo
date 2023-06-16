package com.example.demo.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString(of = {"id","name","writer","is_top"})
public class BoardViewForm {
    @NotBlank
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String writer;
    private String content;
    private Boolean is_top;
    private LocalDateTime registerDate;

    @Builder
    public BoardViewForm(Long id, String name, String writer, LocalDateTime registerDate, String content, Boolean is_top) {
        this.id = id;
        this.name = name;
        this.writer = writer;
        this.registerDate = registerDate;
        this.content = content;
        this.is_top = is_top;
    }

    public BoardViewForm() {
    }
}
