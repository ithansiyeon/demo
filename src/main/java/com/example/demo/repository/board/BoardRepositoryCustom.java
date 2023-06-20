package com.example.demo.repository.board;

import com.example.demo.dto.board.BoardListSearchCond;
import com.example.demo.entity.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<Board> findBoardCustom(BoardListSearchCond searchCond, PageRequest PageRequest);
}
