package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardUpdateForm {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String writer;
    private String content;
    private String is_top;
    private List<String> storeFileName = new ArrayList<>();
    private List<MultipartFile> file = new ArrayList<>();

}
