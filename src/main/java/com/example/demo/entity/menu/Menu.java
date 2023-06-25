package com.example.demo.entity.menu;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name","listOrder"})
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    private String menuName;

    private int listOrder;
    private String authority;
    @OneToMany(mappedBy = "parent")
    @OrderBy("listOrder asc")
    private List<Menu> children = new ArrayList<>();
    @OneToMany(mappedBy = "menu")
    private List<MenuAuth> menuAuths = new ArrayList<>();
    private int depth;
    private String menuCode;
    private String menuExplain;
    private LocalDateTime registerDate;
    private LocalDateTime modifyDate;
    private String registerUserName;
    private String modifyUserName;
    private String isUse;
    private String isDel;

    @Builder
    public Menu(Long id, Menu parent, String menuName, int listOrder, String authority, List<Menu> children, List<MenuAuth> menuAuths, int depth, String menuCode, String menuExplain, LocalDateTime registerDate, LocalDateTime modifyDate, String registerUserName, String modifyUserName, String isUse, String isDel) {
        this.id = id;
        this.parent = parent;
        this.menuName = menuName;
        this.listOrder = listOrder;
        this.authority = authority;
        this.children = children;
        this.menuAuths = menuAuths;
        this.depth = depth;
        this.menuCode = menuCode;
        this.menuExplain = menuExplain;
        this.registerDate = registerDate;
        this.modifyDate = modifyDate;
        this.registerUserName = registerUserName;
        this.modifyUserName = modifyUserName;
        this.isUse = isUse;
        this.isDel = isDel;
    }
}
