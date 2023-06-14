package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.xpath.operations.Bool;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardUpdateForm {
    private Long id;
    @NotBlank
    private String name;
    private String content;
    private Boolean is_top;
    private List<Long> fileId = new ArrayList<>();
    private List<String> fileName = new ArrayList<>();
    private List<MultipartFile> file = new ArrayList<>();

}
