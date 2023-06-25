package com.example.demo.dto.menu;


import lombok.Getter;

import java.util.List;

@Getter
public class MenuSaveForm {
    private String name;
    private String authority;
    private String id;
    private List<MenuSaveForm> children;
    private int depth;
    private String menuCode;
    private String isUse;

    public MenuSaveForm(String name, String authority, String id, List<MenuSaveForm> children, int depth, String menuCode, String isUse) {
        this.name = name;
        this.authority = authority;
        this.id = id;
        this.children = children;
        this.depth = depth;
        this.menuCode = menuCode;
        this.isUse = isUse;
    }

    public MenuSaveForm() {

    }

    public void setAutoMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    @Override
    public String toString() {
        return "MenuSaveForm{" +
                "name='" + name + '\'' +
                ", authority='" + authority + '\'' +
                ", id='" + id + '\'' +
                ", children=" + children +
                ", depth=" + depth +
                ", menuCode='" + menuCode + '\'' +
                ", isUse='" + isUse + '\'' +
                '}';
    }
}
