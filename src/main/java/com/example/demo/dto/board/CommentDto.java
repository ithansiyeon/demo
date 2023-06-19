package com.example.demo.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(of = {"id","content","writer"})
public class CommentDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registerDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedDate;
    private String content;
    private String writer;
    private Long likeCnt;
    private Long commentHeartIdx;
    private String isLike;

    @QueryProjection
    public CommentDto(Long id, LocalDateTime registerDate, LocalDateTime modifiedDate, String content, String writer, Long likeCnt, Long commentHeartIdx, String isLike) {
        this.id = id;
        this.registerDate = registerDate;
        this.modifiedDate = modifiedDate;
        this.content = content;
        this.writer = writer;
        this.likeCnt = likeCnt;
        this.commentHeartIdx = commentHeartIdx;
        this.isLike = isLike;
    }

    public CommentDto() {
    }
}