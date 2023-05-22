package com.example.demo.dto;

import com.example.demo.entity.Board;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDto {
    Long id;
    String name;
    String writer;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.writer = board.getWriter();
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", writer='" + writer + '\'' +
                '}';
    }
}
