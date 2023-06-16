package com.example.demo.repository;

import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardSearchCond;
import com.example.demo.repository.mybatis.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyBatisBoardRepository implements BoardMapper {
    private final BoardMapper boardMapper;
    @Override
    public List<BoardDto> boardList(BoardSearchCond boardSearch) {
        List<BoardDto> result = boardMapper.boardList(boardSearch);
        return result;
    }
}
