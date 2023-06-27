package com.example.demo.entity.menu;

import com.example.demo.dto.menu.MenuAddForm;
import com.example.demo.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    private String menuName;

    private int sort;
    private String authority;
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @OrderBy("sort asc")
    private List<Menu> children = new ArrayList<>();
    @OneToMany(mappedBy = "menu")
    private List<MenuAuth> menuAuths = new ArrayList<>();
    private int depth;
    private String menuCode;
    private String menuDescription;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registerDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;
    private String isUse;
    @ColumnDefault("'N'")
    private String isDelete;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_user_idx")
    private User regUserIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modify_user_idx")
    private User modifyUserIdx;

    @Builder
    public Menu(Menu parent, String menuName, int sort, String authority, List<Menu> children, List<MenuAuth> menuAuths, int depth, String menuCode, String menuDescription, LocalDateTime registerDate, LocalDateTime modifyDate, String isUse, String isDelete, User regUserIdx, User modifyUserIdx) {
        this.parent = parent;
        this.menuName = menuName;
        this.sort = sort;
        this.authority = authority;
        this.children = children;
        this.menuAuths = menuAuths;
        this.depth = depth;
        this.menuCode = menuCode;
        this.menuDescription = menuDescription;
        this.registerDate = registerDate;
        this.modifyDate = modifyDate;
        this.isUse = isUse;
        this.isDelete = isDelete;
        this.regUserIdx = regUserIdx;
        this.modifyUserIdx = modifyUserIdx;
    }

    public void updateMenu(MenuAddForm menuAddForm, User user) {
        this.menuName = menuAddForm.getMenuName();
        this.authority = menuAddForm.getAuthority();
        this.isUse = menuAddForm.getIsUse();
        this.menuDescription = menuAddForm.getMenuDescription();
        this.modifyUserIdx = user;
    }

    public void changeOrder(int sort, Menu menu, int depth) {
        this.sort = sort;
        this.parent = menu;
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", parent=" + parent +
                ", menuName='" + menuName + '\'' +
                ", sort=" + sort +
                ", authority='" + authority + '\'' +
                ", depth=" + depth +
                ", menuCode='" + menuCode + '\'' +
                ", menuDescription='" + menuDescription + '\'' +
                ", registerDate=" + registerDate +
                ", modifyDate=" + modifyDate +
                ", isUse='" + isUse + '\'' +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }
}
