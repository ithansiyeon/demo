package com.example.demo.repository.commentHeart;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.demo.entity.QCommentHeart.commentHeart;

@RequiredArgsConstructor
public class CommentHeartRepositoryImpl implements CommentHeartRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public void deleteByCommentId(Long commentId, String writer) {
        query.delete(commentHeart).where(commentHeart.comment.id.eq(commentId).and(commentHeart.writer.eq(writer))).execute();
    }
}
