package com.example.demo.service.menu;

import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.entity.menu.Menu;
import com.example.demo.repository.menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> getMenuTree() {
        List<Menu> menus = menuRepository.getMenus();
        return menus;
    }

    public List<MenuResultDto> getMenuList() {
        List<Menu> menuList = menuRepository.findAllWithQuerydsl();
        return menuList.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }
}
