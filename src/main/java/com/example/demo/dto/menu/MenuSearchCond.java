package com.example.demo.dto.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuSearchCond {
    private String searchType;
    private String keyword;
    private String isUse;

    @Override
    public String toString() {
        return "MenuSearchCond{" +
                "searchType='" + searchType + '\'' +
                ", keyword='" + keyword + '\'' +
                ", isUse='" + isUse + '\'' +
                '}';
    }
}
