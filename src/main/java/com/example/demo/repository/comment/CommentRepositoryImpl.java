package com.example.demo.repository.comment;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.QCommentDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.demo.entity.QComment.comment;
import static com.example.demo.entity.QCommentHeart.commentHeart;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<CommentDto> findByBoardId(Long id) {
        return query.select
                (new QCommentDto(comment.id,
                              comment.registerDate,
                              comment.modifiedDate,
                              comment.content,
                              comment.writer,
                              query.select(commentHeart.count()).from(commentHeart).where(commentHeart.comment.id.eq(comment.id)),
                              commentHeart.id,
                              commentHeart.isLike))
                .from(comment).leftJoin(comment.commentHeartList, commentHeart).where(comment.board.id.eq(id)).fetch();
    }
}
