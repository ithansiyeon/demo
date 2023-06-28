package com.example.demo.repository.menu;

import com.example.demo.dto.menu.*;
import com.example.demo.entity.menu.Menu;
import com.example.demo.entity.menu.QMenu;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

import static com.example.demo.entity.menu.QMenu.menu;
import static com.example.demo.entity.user.QUser.user;
import static com.querydsl.jpa.JPAExpressions.select;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {
    private final SqlSession sqlSession;
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
                .orderBy(menu.sort.asc(),menu.modifyDate.desc(), child.sort.asc(), child.modifyDate.desc())
                .fetch();
    }

    public List<Menu> findAllWithQuerydsl(MenuSearchCond menuSearchCond) {
        QMenu child = new QMenu("child");
        return query.selectFrom(menu)
                .distinct()
                .leftJoin(menu.children, child)
                .fetchJoin()
                .where(
                        menu.parent.isNull(),
                        isUseEq(menuSearchCond.getIsUse())
                )
                .orderBy(menu.sort.asc(),menu.modifyDate.desc(), child.sort.asc(), child.modifyDate.desc())
                .fetch();
    }

    public BooleanExpression menuSearch(MenuSearchCond menuSearchCond) {
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
                                      ExpressionUtils.as(select(user.userName).from(user).where(user.id.eq(menu.regUserIdx.id)),"registerUserName"),
                                      ExpressionUtils.as(select(user.userName).from(user).where(user.id.eq(menu.modifyUserIdx.id)),"modifyUserName"),
                                      menu.registerDate,
                                      menu.modifyDate))
                .from(menu)
                .where(menu.id.eq(menuIdx)).fetchOne();
    }

    @Override
    public List<MenuDto> findMenuList() {
        return sqlSession.selectList("mapper.menu.menuList");
    }

    @Override
    public List<Menu> findByUrl(String requestURI) {
        QMenu child = new QMenu("child");
        return query.selectFrom(menu)
                .distinct()
                .leftJoin(menu.children, child)
                .fetchJoin()
                .where(
                        menu.parent.isNull(),
                        menu.authority.eq(requestURI)
                )
                .orderBy(menu.sort.asc(),menu.modifyDate.desc(), child.sort.asc(), child.modifyDate.desc())
                .fetch();
    }
}
