package com.example.demo.entity.menu;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuAuthIdx;
    private String authority;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_idx")
    private Menu menu;
}
