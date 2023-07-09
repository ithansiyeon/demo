package com.example.demo.controller.menu;

import com.example.demo.dto.menu.MenuAddForm;
import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.dto.menu.MenuSaveForm;
import com.example.demo.service.menu.MenuService;
import com.example.demo.springsecurity.SecurityUser;
import jakarta.validation.Valid;

import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    /*
     메뉴 상단 리스트 조회
     */
    @GetMapping("/menu/authorityMenuList")
    public String authorityMenuList(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MenuResultDto> menuList = null;
        if(principal instanceof SecurityUser) {
            SecurityUser userDetails = (SecurityUser) principal;
            menuList = userDetails.getMenuList();
        }
        model.addAttribute("commonMenuList", menuList);
        return "fragments/body :: .sideMenuTable";
    }

    /*
     관리자 메뉴 관리 리스트 조회
     */
    @GetMapping("/menu/menuList")
    public String menuList(Model model) {
        List<MenuResultDto> menuList = menuService.getMenuList();
        model.addAttribute("menuList",menuList);
        return "menu/menuList";
    }

    /*
     메뉴 등록 폼 조회
     */
    @GetMapping("/menu/registerMenu")
    public String registerMenuForm(Model model) {
        MenuAddForm menuAddForm = new MenuAddForm();
        menuAddForm = menuService.autoMenuCode(menuAddForm);
        model.addAttribute("menuForm",menuAddForm);
        return "menu/menuList :: #menuTable";
    }

    /*
     메뉴 등록 및 수정
     */
    @PostMapping("/menu/registerMenu")
    public ResponseEntity<String> registerMenu(@Valid @ModelAttribute("menuForm") MenuAddForm menuAddForm) throws Exception {
        if(menuAddForm.getMenuIdx() == null) {
            menuService.createMenuList(menuAddForm);
        } else {
            menuService.updateMenuList(menuAddForm);
        }
        return new ResponseEntity<>("ok",HttpStatus.OK);
    }

    /*
     메뉴 수정 폼 조회
     */
    @GetMapping("/menu/editMenu/{menuIdx}")
    public String editMenu(@PathVariable Long menuIdx, Model model) throws Exception {
        MenuAddForm menuAddForm = menuService.findMenuById(menuIdx);
        if(menuAddForm == null) {
            NotFoundException error = new NotFoundException("데이터가 존재하지 않습니다.");
            throw new Exception(error);
        }
        model.addAttribute("menuForm",menuAddForm);
        return "menu/menuList :: #menuTable";
    }

    /*
    메뉴 순서 저장
     */
    @PostMapping("/menu/menuSave")
    public ResponseEntity menuSave(@RequestBody ArrayList<MenuSaveForm> menuSaveForms) throws Exception {
        menuService.saveMenuList(menuSaveForms);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /*
    메뉴 삭제
     */
    @GetMapping("/menu/deleteMenu/{menuIdx}")
    public ResponseEntity menuDelete(@PathVariable Long menuIdx) {
        menuService.deleteMenu(menuIdx);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
