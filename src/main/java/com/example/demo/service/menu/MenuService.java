package com.example.demo.service.menu;

import com.example.demo.dto.menu.MenuAddForm;
import com.example.demo.dto.menu.MenuResultDto;
import com.example.demo.dto.menu.MenuSaveForm;
import com.example.demo.entity.menu.Menu;
import com.example.demo.entity.user.User;
import com.example.demo.repository.menu.MenuRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.springsecurity.SecurityUser;
import com.example.demo.utils.StringUtil;
import javassist.NotFoundException;
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

    public List<MenuResultDto> getMenuList() {
        List<Menu> menuList = menuRepository.findAllWithQuerydsl();
        return menuList.stream().map(MenuResultDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = false)
    public void createMenuList(MenuAddForm menuAddForm) {
        autoMenuCode(menuAddForm);
        User user = getUser();
        menuRepository.save(Menu.builder().menuName(menuAddForm.getMenuName()).menuCode(menuAddForm.getMenuCode()).menuDescription(menuAddForm.getMenuDescription()).authority(menuAddForm.getAuthority()).isUse(menuAddForm.getIsUse()).regUserIdx(user).modifyUserIdx(user).build());
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
        return menuRepository.findByMenuIdx(menuIdx);
    }

    @Transactional(readOnly = false)
    public void updateMenuList(MenuAddForm menuAddForm) throws Exception {
        Menu findMenu = menuRepository.findById(menuAddForm.getMenuIdx()).orElse(null);
        if(findMenu == null) {
            throw new NotFoundException("해당 데이터가 존재하지 않습니다.");
        }
        findMenu.updateMenu(menuAddForm, getUser());
        menuRepository.save(findMenu);
    }

    private User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        SecurityUser userDetails = (SecurityUser)principal;
        User user = userRepository.findById(userDetails.getUserIdx()).get();
        return user;
    }

    @Transactional(readOnly = false)
    public void deleteMenu(Long menuIdx) {
        menuRepository.deleteById(menuIdx);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class) // 트랜잭션 설정
    public void saveMenuList(ArrayList<MenuSaveForm> menuSaveForms) throws Exception {
        for(int i=0;i<menuSaveForms.size();i++) {
            Menu menu = menuRepository.findById(menuSaveForms.get(i).getId()).orElse(null);
            if(menu == null) {
                NotFoundException error = new NotFoundException("데이터가 존재하지 않습니다.");
                throw new Exception(error);
            }
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

    public String findByUrl(String requestURI) {
        return menuRepository.findByUrl(requestURI);
    }
}
