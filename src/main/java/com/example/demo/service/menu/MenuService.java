package com.example.demo.service.menu;

import com.example.demo.dto.menu.MenuAddForm;
import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.dto.menu.MenuSaveForm;
import com.example.demo.dto.menu.MenuSearchCond;
import com.example.demo.entity.menu.Menu;
import com.example.demo.entity.user.User;
import com.example.demo.repository.menu.MenuRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.springsecurity.SecurityUser;
import com.example.demo.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public List<Menu> getMenuTree() {
        List<Menu> menus = menuRepository.getMenus();
        return menus;
    }

    public List<MenuResultDto> getMenuList(MenuSearchCond menuSearchCond) {
        List<Menu> menuList = menuRepository.findAllWithQuerydsl(menuSearchCond);
        return menuList.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public void createMenuList(MenuAddForm menuAddForm) {
        autoMenuCode(menuAddForm);
        User user = getUser();
        menuRepository.save(Menu.builder().menuName(menuAddForm.getMenuName()).menuCode(menuAddForm.getMenuCode()).authority(menuAddForm.getAuthority()).isUse(menuAddForm.getIsUse()).regUserIdx(user).modifyUserIdx(user).build());
    }

    public MenuAddForm autoMenuCode(MenuAddForm menuAddForm) {
        String maxMenuCode = menuRepository.findMaxMenuCode();
        if(StringUtil.isEmpty(maxMenuCode)) {
            menuAddForm.setAutoMenuCode("001");
        } else {
            int num = Integer.parseInt(maxMenuCode);
            menuAddForm.setAutoMenuCode(String.format("%03d",num+1));
        }
        return menuAddForm;
    }

    public MenuAddForm findMenuById(Long menuIdx) {
        return mapper.map(menuRepository.findByMenuIdx(menuIdx),MenuAddForm.class);
    }

    @Transactional(readOnly = false)
    public void updateMenuList(MenuAddForm menuAddForm) {
        Menu findMenu = menuRepository.findById(menuAddForm.getMenuIdx()).get();
        findMenu.updateMenu(menuAddForm, getUser());
        menuRepository.save(findMenu);
    }

    private User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityUser userDetails = (SecurityUser)principal;
        User user = userRepository.findById(userDetails.getUserIdx()).get();
        return user;
    }

    @Transactional(readOnly = false)
    public void deleteMenu(Long menuIdx) {
        menuRepository.deleteById(menuIdx);
    }

    @Transactional(readOnly = false)
    public void saveMenuList(ArrayList<MenuSaveForm> menuSaveForms) {
        for(int i=0;i<menuSaveForms.size();i++) {
            Menu menu = menuRepository.findById(menuSaveForms.get(i).getId()).get();
            menu.changeOrder(i+1,null,1);
            if(menuSaveForms.get(i).getChildren() != null) {
                for(int j=0;j<menuSaveForms.get(i).getChildren().size();j++) {
                    Menu subMenu1 = menuRepository.findById(menuSaveForms.get(i).getChildren().get(j).getId()).get();
                    subMenu1.changeOrder(j+1, menu, 2);
                    if(menuSaveForms.get(i).getChildren().get(j).getChildren() != null) {
                        for(int k=0;k<menuSaveForms.get(i).getChildren().get(j).getChildren().size();k++) {
                            Menu subMenu2 = menuRepository.findById(menuSaveForms.get(i).getChildren().get(j).getChildren().get(k).getId()).get();
                            subMenu2.changeOrder(k+1, subMenu1, 3);
                        }
                    }
                }
            }
        }
    }

    public List<MenuResultDto> getAuthorityMenuList(String loginId) {
        List<Menu> menuList = menuRepository.findByLoginId(loginId);
         return menuList.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }
}
