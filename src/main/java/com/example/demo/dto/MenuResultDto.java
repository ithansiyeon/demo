package com.example.demo.dto;

import com.example.demo.entity.Menu;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MenuResultDto {

    private Long id;

    private String name;

    private int listOrder;

    private List<MenuResultDto> children;

    public MenuResultDto(final Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.listOrder = menu.getListOrder();
        this.children = menu.getChildren().stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

}