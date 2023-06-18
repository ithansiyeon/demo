package com.example.demo.mapper.board;

import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardSearchCond;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDto> boardList(BoardSearchCond itemSearch);
}
