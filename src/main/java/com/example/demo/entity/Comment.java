package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_idx")
    private Board board;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime registerDate;
    @Column(nullable = true)
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    private String writer;
    private String content;

    @Builder
    public Comment(Long id, Board board, LocalDateTime registerDate, LocalDateTime modifiedDate, String writer, String content) {
        this.id = id;
        this.board = board;
        this.registerDate = registerDate;
        this.modifiedDate = modifiedDate;
        this.writer = writer;
        this.content = content;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", board=" + board +
                ", registerDate=" + registerDate +
                ", modifiedDate=" + modifiedDate +
                ", writer='" + writer + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
