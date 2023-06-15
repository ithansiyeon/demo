package com.example.demo.repository.comment;

import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>, CommentRepositoryCustom {
//    List<Comment> findByBoardId(Long boardId);

}
