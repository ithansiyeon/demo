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
    private int sort;
    private String authority;
    private List<MenuResultDto> children;
    private String isUse;
    private int num;

    public MenuResultDto(Menu menu) {
        this.id = menu.getId();
        this.menuName = menu.getMenuName();
        this.sort = menu.getSort();
        this.authority = menu.getAuthority();
        this.isUse = menu.getIsUse();
        this.children = menu.getChildren().stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "MenuResultDto{" +
                "id=" + id +
                ", menuName='" + menuName + '\'' +
                ", sort=" + sort +
                ", authority='" + authority + '\'' +
                ", children=" + children +
                '}';
    }
}
