package com.example.demo.repository.menu;

import com.example.demo.dto.menu.MenuAddForm;
import com.example.demo.dto.menu.MenuSearchCond;
import com.example.demo.dto.menu.QMenuAddForm;
import com.example.demo.entity.menu.Menu;
import com.example.demo.entity.menu.QMenu;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.demo.entity.menu.QMenu.menu;
import static com.example.demo.entity.user.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Menu> findByLoginId(String loginId) {
        QMenu child = new QMenu("child");

        return query.selectFrom(menu)
                .distinct()
                .leftJoin(menu.children, child)
                .fetchJoin()
                .where(
                        menu.parent.isNull(),
                        menu.isUse.eq("Y")
                )
                .orderBy(menu.sort.asc(),menu.modifyDate.asc(), child.sort.asc(), child.modifyDate.asc())
                .fetch();
    }

    public List<Menu> findAllByParentIsNull() {
        List<Menu> results = query.select(menu).from(menu).where(menu.parent.isNull()).fetch();
        return results;
    }

    public List<Menu> findAllWithQuerydsl(MenuSearchCond menuSearchCond) {
        QMenu child = new QMenu("child");

        return query.selectFrom(menu)
                .distinct()
                .leftJoin(menu.children, child)
                .fetchJoin()
                .where(
                        menu.parent.isNull(),
                        menuSearch(menuSearchCond),
                        isUseEq(menuSearchCond.getIsUse())
                )
                .orderBy(menu.sort.asc(),menu.modifyDate.asc(), child.sort.asc(), child.modifyDate.asc())
                .fetch();
    }

    public BooleanExpression menuSearch(MenuSearchCond menuSearchCond) {
        System.out.println("menuSearchCond.getSearchType() = " + menuSearchCond.getSearchType());
        if(hasText(menuSearchCond.getSearchType())) {
            String searchType = menuSearchCond.getSearchType();
            if(hasText(menuSearchCond.getKeyword())) {
                String keyword = menuSearchCond.getKeyword();
                if(searchType.equals("")) {
                    return null;
                } else if(searchType.equals("menuName")) {
                    return menuNameEq(keyword);
                } else {
                    return menuCodeEq(keyword);
                }
            }
        }
        return null;
    }

    private BooleanExpression isUseEq(String isUse) {
        return hasText(isUse) ? menu.isUse.eq(isUse) : null;
    }

    private BooleanExpression menuCodeEq(String keyword) {
        return hasText(keyword) ? menu.menuCode.eq(keyword) : null;
    }

    private BooleanExpression menuNameEq(String keyword) {
        return hasText(keyword) ? menu.menuName.contains(keyword) : null;
    }

    private BooleanExpression childMenuCodeEq(String keyword) {
        for(Integer i=0;i < menu.children.size();i++) {
            menu.children.get(i).menuName.contains(keyword);
        }
        return hasText(keyword) ? menu.children.get(0).menuName : null;
    }

    private BooleanExpression childMenuNameEq(String keyword) {
        return hasText(keyword) ? menu.menuName.contains(keyword) : null;
    }

    public List<Menu> getMenus() {
        return query.selectFrom(menu).fetch();
    }

    public String findMaxMenuCode() {
        return query.select(menu.menuCode.max()).from(menu).fetchOne();
    }

    @Override
    public MenuAddForm findByMenuIdx(Long menuIdx) {
        return query.select(new QMenuAddForm(
                                      menu.id,
                                      menu.menuName,
                                      menu.authority,
                                      menu.depth,
                                      menu.menuCode,
                                      menu.isUse,
                                      menu.menuDescription,
                                      ExpressionUtils.as(JPAExpressions.select(user.userName).from(user).where(user.id.eq(menu.regUserIdx.id)),"registerUserName"),
                                      ExpressionUtils.as(JPAExpressions.select(user.userName).from(user).where(user.id.eq(menu.modifyUserIdx.id)),"modifyUserName"),
                                      menu.registerDate,
                                      menu.modifyDate))
                .from(menu)
                .where(menu.id.eq(menuIdx)).fetchOne();
    }
}
