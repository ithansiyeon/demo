package com.example.demo.service;

import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepository.findBoardCustom(pageable);
    }

    @Transactional(readOnly = false)
    public void createBoard() {
        for(int i=0;i<25;i++) {
            Board board = Board.builder().name("board"+String.valueOf(i+1)).writer("test"+String.valueOf(i+1)).content("aaaaaa").build();
            boardRepository.save(board);
        }
    }
}
