package com.example.demo.dto.menu;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MenuAddForm {
    private Long menuIdx;
    @NotNull
    private String menuName;
    private String authority;
    private int depth;
    private String menuCode;
    private String isUse;
    @NotNull
    private String menuDescription;
    private String registerUserName;
    private String modifyUserName;
    private LocalDateTime registerDate;
    private LocalDateTime modifyDate;

    @QueryProjection
    public MenuAddForm(Long menuIdx, String menuName, String authority, int depth, String menuCode, String isUse, String menuDescription, String registerUserName, String modifyUserName, LocalDateTime registerDate, LocalDateTime modifyDate) {
        this.menuIdx = menuIdx;
        this.menuName = menuName;
        this.authority = authority;
        this.depth = depth;
        this.menuCode = menuCode;
        this.isUse = isUse;
        this.menuDescription = menuDescription;
        this.registerUserName = registerUserName;
        this.modifyUserName = modifyUserName;
        this.registerDate = registerDate;
        this.modifyDate = modifyDate;
    }

    public MenuAddForm() {

    }

    public void setAutoMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    @Override
    public String toString() {
        return "MenuSaveForm{" +
                "menuName='" + menuName + '\'' +
                ", authority='" + authority + '\'' +
                ", depth=" + depth +
                ", menuCode='" + menuCode + '\'' +
                ", isUse='" + isUse + '\'' +
                '}';
    }
}
