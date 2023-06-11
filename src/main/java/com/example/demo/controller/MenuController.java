package com.example.demo.controller;

import com.example.demo.dto.MenuResultDto;
import com.example.demo.entity.Menu;
import com.example.demo.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/menu")
    public String getMenuTree(Model model) {
        List<Menu> menus = menuService.getMenuTree();
        List<Menu> topLevelMenus = new ArrayList<>();

        Map<Long,List<Menu>> childMenusMap = new LinkedHashMap<>();
        for (Menu menu : menus) {
            if(menu.getParent() == null) {
                topLevelMenus.add(menu);
            } else {
                List<Menu> childMenus = childMenusMap.getOrDefault(menu.getParent().getId(),new ArrayList<>());
                childMenus.add(menu);
                childMenusMap.put(menu.getParent().getId(),childMenus);
            }
        }
        model.addAttribute("topLevelMenus",topLevelMenus);
        model.addAttribute("childMenusMap",childMenusMap);
        return "menuTree";
    }

}