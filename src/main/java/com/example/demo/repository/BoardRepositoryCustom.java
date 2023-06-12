package com.example.demo.repository;

import com.example.demo.dto.BoardListSearchCond;
import com.example.demo.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> findBoardCustom(BoardListSearchCond searchCond, Pageable pageable);
}
