package com.example.demo.repository.comment;

import com.example.demo.dto.board.CommentDto;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentDto> findByBoardId(Long boardIdx);
}
