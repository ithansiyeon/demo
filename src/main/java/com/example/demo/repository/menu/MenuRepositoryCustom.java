package com.example.demo.repository.menu;

import com.example.demo.dto.menu.MenuAddForm;
import com.example.demo.dto.menu.MenuSearchCond;
import com.example.demo.entity.menu.Menu;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> findAllWithQuerydsl();
    String findMaxMenuCode();
    MenuAddForm findByMenuIdx(Long menuIdx);
    List<Menu> findByLoginId(String loginId);
    List<Menu> findMenuList(MenuSearchCond menuSearchCond);

    String findByUrl(String requestURI);
}
