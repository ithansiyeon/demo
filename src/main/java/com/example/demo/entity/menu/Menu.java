package com.example.demo.entity.menu;

import jakarta.persistence.*;
import lombok.*;

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

    @Builder
    public Menu(Menu parent, String menuName, String authority, int listOrder, List<Menu> children, int depth) {
        this.parent = parent;
        this.menuName = menuName;
        this.authority = authority;
        this.listOrder = listOrder;
        this.children = children;
        this.depth = depth;
    }
}
