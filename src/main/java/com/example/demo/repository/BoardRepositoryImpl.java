package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.demo.entity.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory query;
    @Override
    public Page<Board> findBoardCustom(org.springframework.data.domain.Pageable pageable) {
        orderParameter(pageable);
//        List<Board> content =
        JPAQuery<Board> boardQuery = query.select(board).from(board).offset(pageable.getOffset()).limit(pageable.getPageSize());
        //동적으로 order by하는 부분 되록이면 파라미터 받아서 처리하는게 낫다고 함
        for(Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            boardQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
        List<Board> content = boardQuery.fetch();
        JPAQuery<Long> countQuery = query.select(board.count()).from(board);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne); //pageImpl 반환
    }

    private void orderParameter(Pageable pageable) {

    }
}
