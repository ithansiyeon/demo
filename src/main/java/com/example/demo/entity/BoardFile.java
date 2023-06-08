package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardFile_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    private String uploadFileName;
    private String storeFileName;

    @Builder
    public BoardFile(String uploadFileName, String storeFileName, Board board) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.board = board;
    }

}
