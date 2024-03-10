package com.example.demo.repository.board;

import com.example.demo.dto.board.BoardListSearchCond;
import com.example.demo.entity.board.Board;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.entity.board.QBoard.board;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    public final JPAQueryFactory query;
    @Override
    public Page<Board> findBoardCustom(BoardListSearchCond searchCond, PageRequest pageRequest) {
        JPAQuery<Board> boardQuery = query.
                                            select(board).
                                            from(board)
                                            .where(boardSearch(searchCond),(dateBetween(searchCond.getStrtDate(),searchCond.getEndDate())), isTopEq(searchCond.getIs_top()))
                                            .offset(pageRequest.getOffset()).limit(pageRequest.getPageSize());
        //동적으로 order by하는 부분 되록이면 파라미터 받아서 처리하는게 낫다고 함
        for(Sort.Order o : pageRequest.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            boardQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }
        List<Board> content = boardQuery.fetch();

        JPAQuery<Long> countQuery = query.select(board.count()).where(boardSearch(searchCond),(dateBetween(searchCond.getStrtDate(),searchCond.getEndDate())), isTopEq(searchCond.getIs_top())).from(board);

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne); //pageImpl 반환
    }

    public BooleanExpression boardSearch(BoardListSearchCond searchCond) {
        if(hasText(searchCond.getSearchType())) {
            String searchType = searchCond.getSearchType();
            if(hasText(searchCond.getKeyword())) {
                String keyword = searchCond.getKeyword();
                if (searchType.equals("all")) {
                    return titleEq(keyword).or(contentEq(keyword));
                } else if (searchType.equals("title")) {
                    return titleEq(keyword);
                } else if (searchType.equals("content")) {
                    return contentEq(keyword);
                }
            }
        }
        return null;
    }

    public BooleanExpression titleEq(String title) {
        return hasText(title) ? board.name.contains(title) : null;
    }

    public BooleanExpression contentEq(String content) {
        return hasText(content) ? board.content.contains(content) : null;
    }

    public BooleanExpression dateBetween(String strtDate, String endDate) {
        if(hasText(strtDate) && hasText(endDate)) {
            String[] starts = strtDate.split("-");
            String[] ends = endDate.split("-");
            LocalDateTime startDateTime = LocalDateTime.of(Integer.parseInt(starts[0]), Integer.parseInt(starts[1]), Integer.parseInt(starts[2]), 0, 0, 0);
            LocalDateTime endDateTime = LocalDateTime.of(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]), 23, 59, 59);
            return board.registerDate.goe(startDateTime).and(board.registerDate.loe(endDateTime));
        } else {
            return null;
        }
    }

    public BooleanExpression isTopEq(Boolean is_top) {
        return is_top != null ? board.is_top.eq(is_top ? "Y":"N"): null;
    }

}
