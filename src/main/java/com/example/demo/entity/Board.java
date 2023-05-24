package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id","name","writer"})
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String writer;
    @Column(length = 1000)
    String content;

    @Builder
    public Board(String name, String writer, String content) {
        this.name = name;
        this.writer = writer;
        this.content = content;
    }
}
