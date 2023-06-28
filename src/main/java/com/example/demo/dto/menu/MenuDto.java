package com.example.demo.dto.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDto {
    private String authority;

    @Override
    public String toString() {
        return "MenuDto{" +
                "authority='" + authority + '\'' +
                '}';
    }
}
