package com.example.demo.controller.menu;

import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.dto.menu.MenuSaveForm;
import com.example.demo.service.menu.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/menu/menuList")
    public String menuList(Model model) {
        List<MenuResultDto> menuList = menuService.getMenuList();
        System.out.println("menuList.toString() = " + menuList.toString());
        model.addAttribute("menuList",menuList);
        return "menu/menuList";
    }

    @GetMapping("/menu/registerMenu")
    public String registerMenu() {
        return "/menu/registerMenu";
    }

    @PostMapping("/menu/menuSave")
    public ResponseEntity meunSave(@RequestBody ArrayList<MenuSaveForm> menuSaveForms) {
        System.out.println("menuSaveForms.toString() = " + menuSaveForms.toString());
        menuService.createMenuList(menuSaveForms);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
