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

import java.util.Objects;
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
                StringBuilder numStr = new StringBuilder();
                for(int j=0;j<8;j++) {
                    if(j==4) numStr.append("-");
                    numStr.append(rm.nextInt(9));
                }
                User user = Objects.requireNonNull(User.builder().userId("test" + i).userName("홍길동" + i).password("$2a$10$TDZwAe142fAe2jLvfMCGhOCqT7PYWFyTGUWZYDRb7iAm2ml0F/x/y").url("/board").isDel("N").authority("관리자").phoneNumber("010-" + numStr).isAlert(is_top)).build();
                em.persist(user);
            }
            User user = em.find(User.class,1L);
            Menu m =  Menu.builder()
                    .menuName("관리자운영관리")
                    .authority("/aaa")
                    .sort(1)
                    .parent(null)
                    .isUse("Y")
                    .menuCode("001")
                    .regUserIdx(user)
                    .build();
            em.persist(m);
            Menu m2 = Menu.builder()
                    .menuName("스프링 부트 게시판")
                    .authority("/board")
                    .sort(2)
                    .parent(null)
                    .isUse("Y")
                    .menuCode("002")
                    .regUserIdx(user)
                    .build();
            em.persist(m2);
            Menu m3 = Menu.builder()
                    .menuName("3. 메뉴")
                    .authority("/ccc")
                    .sort(3)
                    .parent(null)
                    .isUse("Y")
                    .menuCode("003")
                    .regUserIdx(user)
                    .build();
            em.persist(m3);
            Menu m4 = Menu.builder()
                    .menuName("공통관리")
                    .authority("/fff")
                    .sort(1)
                    .parent(m)
                    .isUse("Y")
                    .menuCode("004")
                    .regUserIdx(user)
                    .build();
            em.persist(m4);
            Menu m5 = Menu.builder()
                    .menuName("운영자접속관리")
                    .authority("/userLog")
                    .sort(2)
                    .parent(m)
                    .isUse("Y")
                    .menuCode("005")
                    .regUserIdx(user)
                    .build();
            em.persist(m5);
            Menu m6 = Menu.builder()
                    .menuName("운영자정보관리")
                    .authority("/user/userRegister")
                    .sort(1)
                    .parent(m2)
                    .isUse("Y")
                    .menuCode("006")
                    .regUserIdx(user)
                    .build();
            em.persist(m6);
            Menu m7 = Menu.builder()
                    .menuName("관리자메뉴관리_List")
                    .authority("/menu/menuList")
                    .sort(1)
                    .parent(m4)
                    .isUse("Y")
                    .menuCode("007")
                    .regUserIdx(user)
                    .build();
            em.persist(m7);
        }
    }
}
