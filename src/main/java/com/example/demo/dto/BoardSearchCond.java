package com.example.demo.dto;

public class BoardSearchCond {
    String tableName;
    Long boardIdx;

    public BoardSearchCond(String tableName, Long boardIdx) {
        this.tableName = tableName;
        this.boardIdx = boardIdx;
    }

    @Override
    public String toString() {
        return "BoardSearchCond{" +
                "tableName='" + tableName + '\'' +
                ", boardIdx=" + boardIdx +
                '}';
    }
}
