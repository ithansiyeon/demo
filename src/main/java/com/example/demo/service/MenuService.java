package com.example.demo.service;

import com.example.demo.entity.menu.Menu;
import com.example.demo.repository.MenuQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuQuerydslRepository menuQuerydslRepository;

    public List<Menu> getMenuTree() {
        List<Menu> menus = menuQuerydslRepository.getMenus();
        return menus;
    }
}
