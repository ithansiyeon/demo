package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","name","listOrder"})
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    private String name;

    private int listOrder;

    @OneToMany(mappedBy = "parent")
    @OrderBy("listOrder asc")
    private List<Menu> children = new ArrayList<>();

    @Builder
    public Menu(Menu parent, String name, int listOrder, List<Menu> children) {
        this.parent = parent;
        this.name = name;
        this.listOrder = listOrder;
        this.children = children;
    }
}
