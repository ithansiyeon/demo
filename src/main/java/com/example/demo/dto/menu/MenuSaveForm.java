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

    public MenuSaveForm(String name, String authority, String id, List<MenuSaveForm> children, int depth) {
        this.name = name;
        this.authority = authority;
        this.id = id;
        this.children = children;
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "MenuSaveForm{" +
                "name='" + name + '\'' +
                ", authority='" + authority + '\'' +
                ", id='" + id + '\'' +
                ", children=" + children +
                ", depth=" + depth +
                '}';
    }
}
