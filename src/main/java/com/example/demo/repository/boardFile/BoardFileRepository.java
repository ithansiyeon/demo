package com.example.demo.repository.boardFile;

import com.example.demo.entity.board.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile,Long>, BoardFileRepositoryCustom {
    List<BoardFile> findByBoardId(Long boardId);

}
