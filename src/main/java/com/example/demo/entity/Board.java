package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id","name","writer"})
@EntityListeners(AuditingEntityListener.class)
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String writer;
    @Lob
    private String content;
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registerDate;
    @Column(nullable = true)
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    private int views;

    @Builder
    public Board(String name, String writer, String content, Long id) {
        this.name = name;
        this.writer = writer;
        this.content = content;
        this.id = id;
    }

    public void viewCountUp(Board board) {
        board.views++;
    }
}
