package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageDTO {
    @Id
    private Long id;
    private String pageIdx;
    private String pageNm;
    private String pageUrl;
    private String gnbIdx;
    private String gnbNm;
    private String gnbIcon;
    private String authRoleNm;
}
