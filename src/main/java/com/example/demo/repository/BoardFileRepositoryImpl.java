package com.example.demo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.demo.entity.QBoardFile.boardFile;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class BoardFileRepositoryImpl implements BoardFileRepositoryCustom{
    private final JPAQueryFactory query;
    @Override
    public void deleteByStoreFileName(String storeFileName, Long boardIdx) {
        long cnt = query.delete(boardFile).where(storeFileNameEq(storeFileName), boardIdxEq(boardIdx)).execute();
        System.out.println("cnt = " + cnt);
    }

    public BooleanExpression boardIdxEq(Long boardIdx) {
        return boardIdx != null ? boardFile.board.id.eq(boardIdx) : null;
    }

    public BooleanExpression storeFileNameEq(String storeFileName) {
        return hasText(storeFileName) ? boardFile.storeFileName.eq(storeFileName) : null;
    }
}
