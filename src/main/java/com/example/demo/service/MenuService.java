package com.example.demo.service;

import com.example.demo.entity.Menu;
import com.example.demo.entity.PageDTO;
import com.example.demo.repository.MenuQuerydslRepository;
import com.example.demo.repository.PageDtoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuQuerydslRepository menuQuerydslRepository;
    private final PageDtoRepository pageDtoRepository;

    public List<PageDTO> getAllMenuList() {
        return pageDtoRepository.findAll();
    }

    public List<Menu> getMenuTree() {
        List<Menu> menus = menuQuerydslRepository.getMenus();
        return menus;
    }
}
