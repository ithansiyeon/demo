package com.example.demo.repository.mybatis;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardSearchCond;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDto> boardList(BoardSearchCond itemSearch);
}
