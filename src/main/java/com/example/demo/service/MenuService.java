package com.example.demo.service;

import com.example.demo.dto.MenuResultDto;
import com.example.demo.entity.Menu;
import com.example.demo.entity.PageDTO;
import com.example.demo.repository.MenuQuerydslRepository;
import com.example.demo.repository.MenuRepository;
import com.example.demo.repository.PageDtoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuQuerydslRepository menuQuerydslRepository;
    private final PageDtoRepository pageDtoRepository;

    public List<MenuResultDto> getV1Menus() {
        final List<Menu> all = menuRepository.findAll();
        return all.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    public List<MenuResultDto> getV2Menus() {
        final List<Menu> all = menuQuerydslRepository.findAllByParentIsNull();
        return all.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    public List<MenuResultDto> getV3Menus() {
        final List<Menu> all = menuRepository.findAll(Sort.by(Sort.Direction.ASC, "listOrder"));
        return all.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    public List<MenuResultDto> getV4Menus() {
        final List<Menu> all = menuRepository.findAllByParentIsNull(Sort.by(Sort.Direction.ASC, "listOrder"));
        return all.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    public List<MenuResultDto> getJpqlMenus() {
        final List<Menu> all = menuRepository.findAllWithJpql();
        return all.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    public List<MenuResultDto> getAllWithQuerydsl() {
        final List<Menu> all = menuQuerydslRepository.findAllWithQuerydsl();
        return all.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    public List<PageDTO> getAllMenuList() {
        return pageDtoRepository.findAll();
    }

    public List<Menu> getMenuTree() {
        List<Menu> menus = menuQuerydslRepository.getMenus();
        return menus;
    }
}
