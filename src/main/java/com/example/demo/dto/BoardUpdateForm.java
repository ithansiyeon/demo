package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.xpath.operations.Bool;
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
    private Boolean is_top;
    private List<String> storeFileName = new ArrayList<>();
    private List<String> fileName = new ArrayList<>();
    private List<MultipartFile> file = new ArrayList<>();

}
