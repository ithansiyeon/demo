package com.example.demo.service;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final EntityManager em;
    private final BoardRepository boardRepository;

    public Page<Board> getBoardList(Pageable pageable) {
        return boardRepository.findBoardCustom(pageable);
    }

    @Transactional(readOnly = false)
    public void createBoard() {
        for(int i=0;i<110;i++) {
            Board board = Board.builder().name("board"+ (i + 1)).writer("test"+ (i + 1)).content("aaaaaa").build();
            boardRepository.save(board);
        }
    }

    public void mergeBoard(Board board) {
        Board findBoard = boardRepository.findByName(board.getName());
//        if(findBoard == null) {
//            em.persist(board);
//            boardRepository.save()
//        } else {
//            em.merge(board);
//        }
        boardRepository.save(board);
    }

    public List<BoardDto> getBoardList() {
        return boardRepository.findAll().stream().map(BoardDto::new).collect(Collectors.toList());
    }
}
