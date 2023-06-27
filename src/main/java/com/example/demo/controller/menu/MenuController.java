package com.example.demo.controller.menu;

import com.example.demo.dto.menu.MenuAddForm;
import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.dto.menu.MenuSaveForm;
import com.example.demo.dto.menu.MenuSearchCond;
import com.example.demo.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/menu/authorityMenuList")
    public String authorityMenuList(Model model) {
        String loginId = "";
        List<MenuResultDto> menuList = menuService.getAuthorityMenuList(loginId);
        model.addAttribute("commonMenuList",menuList);
        return "fragments/body :: #commonMenuTable";
    }

    @GetMapping("/menu/menuList")
    public String menuList(Model model, @ModelAttribute MenuSearchCond menuSearchCond) {
        List<MenuResultDto> menuList = menuService.getMenuList(menuSearchCond);
        model.addAttribute("menuList",menuList);
        model.addAttribute("menuSearchCond", menuSearchCond);
        return "menu/menuList";
    }

    @GetMapping("/menu/registerMenu")
    public String registerMenuForm(Model model) {
        MenuAddForm menuAddForm = new MenuAddForm();
        menuAddForm = menuService.autoMenuCode(menuAddForm);
        model.addAttribute("menuForm",menuAddForm);
        return "menu/menuList :: #menuTable";
    }

    @PostMapping("/menu/registerMenu")
    public String registerMenu(@Validated @ModelAttribute("menuForm") MenuAddForm menuAddForm, BindingResult bindingResult) {
        /*if (bindingResult.hasErrors()) {
            return "menu/menuList :: #menuTable";
        }*/
        if(menuAddForm.getMenuIdx() == null) {
            menuService.createMenuList(menuAddForm);
        } else {
            menuService.updateMenuList(menuAddForm);
        }
        return "redirect:/menu/menuList";
    }

    @GetMapping("/menu/editMenu/{menuIdx}")
    public String editMenu(@PathVariable Long menuIdx, Model model) {
        MenuAddForm menuAddForm = menuService.findMenuById(menuIdx);
        model.addAttribute("menuForm",menuAddForm);
        return "menu/menuList :: #menuTable";
    }

    @PostMapping("/menu/menuSave")
    public ResponseEntity menuSave(@RequestBody ArrayList<MenuSaveForm> menuSaveForms) {
        menuService.saveMenuList(menuSaveForms);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/menu/deleteMenu/{menuIdx}")
    public ResponseEntity menuDelete(@PathVariable Long menuIdx) {
        menuService.deleteMenu(menuIdx);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
