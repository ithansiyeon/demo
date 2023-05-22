package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.example.demo.entity.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory query;
    @Override
    public Page<Board> findBoardCustom(org.springframework.data.domain.Pageable pageable) {
        List<Board> content = query.select(board).from(board).offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        Long count = query.select(board.count()).from(board).fetchOne();

        return new PageImpl<>(content, pageable, count); //pageImpl 반환
    }
}
