package com.example.demo.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserLogDto {
    private Long id;
    private LocalDateTime registerDate;
    private String ip;
    private String isLoginSuccess;
}
