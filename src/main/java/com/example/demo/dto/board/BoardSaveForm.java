package com.example.demo.dto.board;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardSaveForm {
    @NotBlank
    private String name;
    @NotBlank
    private String writer;
    private String content;
    private Boolean is_top;
    List<MultipartFile> file = new ArrayList<>();
}
