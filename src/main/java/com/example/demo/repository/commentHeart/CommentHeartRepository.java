package com.example.demo.repository.commentHeart;

import com.example.demo.entity.board.CommentHeart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentHeartRepository extends JpaRepository<CommentHeart, Long>, CommentHeartRepositoryCustom {

}
