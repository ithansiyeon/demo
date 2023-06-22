package com.example.demo.controller.menu;

import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.dto.menu.MenuSaveForm;
import com.example.demo.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/menu/menuList")
    public String menuList(Model model) {
        List<MenuResultDto> menuList = menuService.getMenuList();
        model.addAttribute("menuList",menuList);
        return "menu/menuList";
    }

    @PostMapping("/menu/menuSave")
    public String meunSave(@ModelAttribute List<MenuSaveForm> menuSaveForm) {
        System.out.println("menuSaveForm.toString() = " + menuSaveForm.toString());
        return "redirect:/menu/menuList";
    }
}
