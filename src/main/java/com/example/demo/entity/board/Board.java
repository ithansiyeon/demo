package com.example.demo.entity.board;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id","name","writer","is_top"})
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Builder
@AllArgsConstructor
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="board_idx")
    private Long id;
    private String name;
    private String writer;
    @Lob
    private String content;
    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime registerDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    private int views;
    @ColumnDefault("'N'")
    @Column(length = 1)
    private String is_top;
    @OneToMany(mappedBy="board", cascade = CascadeType.REMOVE)
    private List<BoardFile> boardFiles = new ArrayList<>();
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public void viewCountUp(Board board) {
        board.views++;
    }

    public void changeIsTop(String is_top) {
        this.is_top = is_top;
    }
}
