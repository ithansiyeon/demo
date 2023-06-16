package com.example.demo.repository;

import com.example.demo.entity.menu.Menu;
import com.example.demo.entity.menu.QMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

import static com.example.demo.entity.menu.QMenu.menu;

@Repository
@RequiredArgsConstructor
public class MenuQuerydslRepository {
    private final JPAQueryFactory query;

    public List<Menu> findAllByParentIsNull() {
        QMenu qMenu = menu;
        List<Menu> results = query.select(menu).from(menu).where(menu.parent.isNull()).fetch();
        return results;
    }

    public List<Menu> findAllWithQuerydsl() {
        QMenu parent = new QMenu("parent");
        QMenu child = new QMenu("child");

        return query.selectFrom(parent)
                .distinct()
                .leftJoin(parent.children, child)
                .fetchJoin()
                .where(
                        parent.parent.isNull()
                )
                .orderBy(parent.listOrder.asc(), child.listOrder.asc())
                .fetch();
    }

    public List<Menu> getMenus() {
        return query.selectFrom(menu).fetch();
    }
}
