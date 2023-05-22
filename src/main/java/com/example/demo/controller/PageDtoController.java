package com.example.demo.controller;

import com.example.demo.entity.PageDTO;
import com.example.demo.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PageDtoController {

    private final MenuService menuService;

    @GetMapping("/index")
    public String findAllMenuList(Model model) {
        List<PageDTO> menuList = menuService.getAllMenuList();
        model.addAttribute("menuList",menuList);
        return "index";
    }
}
