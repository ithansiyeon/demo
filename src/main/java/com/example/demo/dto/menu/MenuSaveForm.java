package com.example.demo.dto.menu;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MenuSaveForm {
    private Long id;
    private String menuName;
    private String authority;
    private List<MenuSaveForm> children = new ArrayList<>();
    private int depth;
    private String menuCode;
    private String isUse;
    private String menuDescription;

    public MenuSaveForm(Long id, String menuName, String authority, List<MenuSaveForm> children, int depth, String menuCode, String isUse, String menuDescription) {
        this.id = id;
        this.menuName = menuName;
        this.authority = authority;
        this.children = children;
        this.depth = depth;
        this.menuCode = menuCode;
        this.isUse = isUse;
        this.menuDescription = menuDescription;
    }

    @Override
    public String toString() {
        return "MenuSaveForm{" +
                "menuName='" + menuName + '\'' +
                ", authority='" + authority + '\'' +
                ", children=" + children +
                ", depth=" + depth +
                ", menuCode='" + menuCode + '\'' +
                ", isUse='" + isUse + '\'' +
                '}';
    }
}
