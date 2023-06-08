package com.example.demo.repository;

import com.example.demo.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile,Long> {
}
