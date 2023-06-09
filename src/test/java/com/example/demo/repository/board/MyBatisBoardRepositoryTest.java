package com.example.demo.repository.board;

import com.example.demo.dto.board.BoardDto;
import com.example.demo.dto.board.BoardSearchCond;
import com.example.demo.repository.board.MyBatisBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MyBatisBoardRepositoryTest {

    @Autowired
    private MyBatisBoardRepository myBatisBoardRepository;

    @Test
    public void dynamicTableQuery() {
        BoardSearchCond boardSearch = new BoardSearchCond("board",1L);
        List<BoardDto> result = myBatisBoardRepository.boardList(boardSearch);
    }
}