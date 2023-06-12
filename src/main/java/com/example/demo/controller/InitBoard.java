package com.example.demo.controller;

import com.example.demo.entity.Board;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
            for (int i = 0; i < 110; i++) {
                String is_top = "Y";
                if(i%2==0) {
                    is_top = "N";
                }
                Board board = Board.builder().name("board" + (i + 1)).writer("test" + (i + 1)).content("aaaaaa").is_top(is_top).build();
                em.persist(board);
            }
        }
    }
}
