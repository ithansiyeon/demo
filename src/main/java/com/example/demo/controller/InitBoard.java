package com.example.demo.controller;

import com.example.demo.entity.board.Board;
import com.example.demo.entity.board.Comment;
import com.example.demo.entity.menu.Menu;
import com.example.demo.entity.user.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Random;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitBoard {

    private final InitBoardService initBoardService;

    @PostConstruct
    public void init() {
        initBoardService.init();
    }


    @Component
    static class InitBoardService {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            Random rm = new Random();
            for (int i = 1; i < 50; i++) {
                String is_top = "Y";
                if(i%2==0) {
                    is_top = "N";
                }
                Board board = Board.builder().name("게시판 board 테스트 " + i).writer("test" + i).content("aaaaaa").is_top(is_top).build();
                em.persist(board);
                Comment comment = Comment.builder().content("댓글 구현 테스트 " + i).writer("홍길동"+i).board(board).build();
                em.persist(comment);
                String numStr = "";
                for(int j=0;j<8;j++) {
                    if(j==4) numStr+="-";
                    numStr += rm.nextInt(9);
                }
                User user = User.builder().userId("test" + i).userName("홍길동" + i).password("$2a$10$3A58XScJ40dXdqKu0bBnFeqrjZHvNLaWfwehtnjD.WrQC8aVfTHvK").url("/board").isDel("N").authority("관리자").phoneNumber("010-"+numStr).isAlert(is_top).build();
                em.persist(user);
            }
            Menu m =  Menu.builder()
                    .menuName("1. 메뉴")
                    .authority("/aaa")
                    .listOrder(1)
                    .parent(null)
                    .build();
            em.persist(m);
            Menu m2 = Menu.builder()
                    .menuName("2. 메뉴")
                    .authority("/bbb")
                    .listOrder(2)
                    .parent(null)
                    .build();
            em.persist(m2);
            Menu m3 = Menu.builder()
                    .menuName("3. 메뉴")
                    .authority("/ccc")
                    .listOrder(3)
                    .parent(null)
                    .build();
            em.persist(m3);
            Menu m4 = Menu.builder()
                    .menuName("1-1. 하위메뉴")
                    .authority("/fff")
                    .listOrder(1)
                    .parent(m)
                    .build();
            em.persist(m4);
            Menu m5 = Menu.builder()
                    .menuName("1-2. 하위메뉴")
                    .authority("/ggg")
                    .listOrder(2)
                    .parent(m)
                    .build();
            em.persist(m5);
            Menu m6 = Menu.builder()
                    .menuName("2-1. 하위메뉴")
                    .authority("/hhh")
                    .listOrder(1)
                    .parent(m2)
                    .build();
            em.persist(m6);
            Menu m7 = Menu.builder()
                    .menuName("1-1. 1. 하위메뉴")
                    .authority("/ggg")
                    .listOrder(1)
                    .parent(m4)
                    .build();
            em.persist(m7);
        }
    }
}
