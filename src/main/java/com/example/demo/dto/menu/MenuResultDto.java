package com.example.demo.dto.menu;

import com.example.demo.entity.menu.Menu;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MenuResultDto {

    private Long id;
    private String menuName;
    private int listOrder;
    private String authority;
    private List<MenuResultDto> children;
    private int num;

    public MenuResultDto(Menu menu) {
        this.id = menu.getId();
        this.menuName = menu.getMenuName();
        this.listOrder = menu.getListOrder();
        this.authority = menu.getAuthority();
        this.children = menu.getChildren().stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "MenuResultDto{" +
                "id=" + id +
                ", menuName='" + menuName + '\'' +
                ", listOrder=" + listOrder +
                ", authority='" + authority + '\'' +
                ", children=" + children +
                '}';
    }
}
