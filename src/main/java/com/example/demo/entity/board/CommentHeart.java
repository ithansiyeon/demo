package com.example.demo.entity.board;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="heart_idx")
    private Long id;

    @NotNull
    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_idx")
    private Comment comment;

    @NotNull
    @ColumnDefault("'N'")
    private String isLike;

    @Builder
    public CommentHeart(Long id, String writer, Comment comment, String isLike) {
        this.id = id;
        this.writer = writer;
        this.comment = comment;
        this.isLike = isLike;
    }
}
