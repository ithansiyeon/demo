package com.example.demo.repository.board;

import com.example.demo.entity.Board;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardRepositoryCustom {

    @QueryHints({
            @QueryHint(name = org.hibernate.annotations.QueryHints.COMMENT, value = "BoardRepository.findByName")
    })
    Board findByName(String name);

}
