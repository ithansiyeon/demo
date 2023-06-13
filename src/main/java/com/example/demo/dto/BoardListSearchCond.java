package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardListSearchCond {
    private String searchType;
    private String keyword;
    private String strtDate;
    private String endDate;
    private Boolean is_top = false;

    @Override
    public String toString() {
        return "BoardListSearchCond{" +
                "searchType='" + searchType + '\'' +
                ", keyword='" + keyword + '\'' +
                ", strtDate='" + strtDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", is_top=" + is_top +
                '}';
    }
}
