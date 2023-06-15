package com.example.demo.repository.commentHeart;

public interface CommentHeartRepositoryCustom {

    void deleteByCommentId(Long commentId, String writer);
}
