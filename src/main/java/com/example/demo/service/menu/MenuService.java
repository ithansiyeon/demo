package com.example.demo.service.menu;

import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.dto.menu.MenuSaveForm;
import com.example.demo.entity.menu.Menu;
import com.example.demo.repository.menu.MenuRepository;
import com.example.demo.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> getMenuTree() {
        List<Menu> menus = menuRepository.getMenus();
        return menus;
    }

    public List<MenuResultDto> getMenuList() {
        List<Menu> menuList = menuRepository.findAllWithQuerydsl();
        return menuList.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    public void createMenuList(ArrayList<MenuSaveForm> menuSaveForms) {
        menuRepository.deleteAll();
        for (MenuSaveForm menuSaveForm : menuSaveForms) {
            int listOrder = 1;
            int depth = 1;
            Menu menu = Menu.builder().menuName(menuSaveForm.getName()).listOrder(listOrder).authority(menuSaveForm.getAuthority()).depth(depth).build();
            menuRepository.save(menu);
            List<MenuSaveForm> children = menuSaveForm.getChildren();
            for(int i=0; i<children.size();i++) {
                Menu menu1 = Menu.builder().menuName(children.get(i).getName()).listOrder(i+1).authority(children.get(i).getAuthority()).depth(depth).build();
            }
        }
    }

    public MenuSaveForm autoMenuCode() {
        MenuSaveForm menuSaveForm = new MenuSaveForm();
        String maxMenuCode = menuRepository.findMaxMenuCode();
        if(StringUtil.isEmpty(maxMenuCode)) {
             menuSaveForm.setAutoMenuCode("001");
        } else {
            int num = Integer.parseInt(maxMenuCode);
            menuSaveForm.setAutoMenuCode(String.format("%03d",num+1));
        }
        return menuSaveForm;
    }
}
