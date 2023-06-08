package com.example.demo.repository;

import com.example.demo.entity.BoardFile;
import com.querydsl.core.Fetchable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile,Long> {
    List<BoardFile> findByBoardId(Long itemId);
}
