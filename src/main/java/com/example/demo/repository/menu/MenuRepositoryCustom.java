package com.example.demo.repository.menu;

import com.example.demo.dto.menu.MenuAddForm;
import com.example.demo.dto.menu.MenuSearchCond;
import com.example.demo.entity.menu.Menu;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> findAllByParentIsNull();
    List<Menu> findAllWithQuerydsl(MenuSearchCond menuSearchCond);
    List<Menu> getMenus();
    String findMaxMenuCode();
    MenuAddForm findByMenuIdx(Long menuIdx);
    List<Menu> findByLoginId(String loginId);
}
