package com.example.demo.controller.menu;

import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.dto.menu.MenuSaveForm;
import com.example.demo.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
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

    @GetMapping("/menu/registerMenu")
    public String registerMenu(Model model) {
        MenuSaveForm menuSaveForm = menuService.autoMenuCode();
        model.addAttribute("menuForm",menuSaveForm);
        System.out.println("menuSaveForm.toString() = " + menuSaveForm.toString());
        return "menu/menuList :: #menuTable";
    }

    @PostMapping("/menu/menuSave")
    public ResponseEntity meunSave(@RequestBody ArrayList<MenuSaveForm> menuSaveForms) {
        System.out.println("menuSaveForms.toString() = " + menuSaveForms.toString());
        menuService.createMenuList(menuSaveForms);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
