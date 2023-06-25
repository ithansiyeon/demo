package com.example.demo.repository.menu;

import com.example.demo.entity.menu.Menu;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> findAllByParentIsNull();
    List<Menu> findAllWithQuerydsl();
    List<Menu> getMenus();
    String findMaxMenuCode();
}
