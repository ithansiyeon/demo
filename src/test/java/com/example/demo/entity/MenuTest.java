package com.example.demo.entity;

import com.example.demo.entity.menu.Menu;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Commit
class MenuTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void createMenu() {
            Menu m =  Menu.builder()
                    .menuName("1. 메뉴")
                    .listOrder(1)
                    .parent(null)
                    .build();
            em.persist(m);
            Menu m2 = Menu.builder()
                .menuName("2. 메뉴")
                .listOrder(2)
                .parent(null)
                    .build();
            em.persist(m2);
            Menu m3 = Menu.builder()
                .menuName("3. 메뉴")
                .listOrder(3)
                .parent(null)
                    .build();
            em.persist(m3);
            Menu m4 = Menu.builder()
                .menuName("1-1. 하위메뉴")
                .listOrder(2)
                .parent(m)
                    .build();
            em.persist(m4);
            Menu m5 = Menu.builder()
                .menuName("1-2. 하위메뉴")
                .listOrder(1)
                .parent(m)
                    .build();
            em.persist(m5);
            Menu m6 = Menu.builder()
                    .menuName("2-1 하위메뉴")
                    .listOrder(2)
                    .parent(m2)
                    .build();
            em.persist(m6);
    }
}