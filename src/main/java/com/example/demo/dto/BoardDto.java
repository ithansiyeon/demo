package com.example.demo.dto;

import com.example.demo.entity.Board;
import com.example.demo.excel.annotation.DefaultBodyStyle;
import com.example.demo.excel.annotation.DefaultHeaderStyle;
import com.example.demo.excel.annotation.ExcelColumn;
import com.example.demo.excel.annotation.ExcelColumnStyle;
import com.example.demo.excel.style.DefaultExcelCellStyle;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@DefaultHeaderStyle(
        style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BLUE_HEADER")
)
@DefaultBodyStyle(
        style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BODY")
)
public class BoardDto {
    @ExcelColumn(headerName = "id")
    private Long id;
    @ExcelColumn(headerName = "name")
    private String name;
    @ExcelColumn(headerName = "writer")
    private String writer;
    @ExcelColumn(headerName = "registerDate")
    private LocalDateTime registerDate;
    private int views;
    private String is_top;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.writer = board.getWriter();
        this.registerDate = board.getRegisterDate();
        this.views = board.getViews();
        this.is_top = board.getIs_top();
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", writer='" + writer + '\'' +
                ", registerDate=" + registerDate +
                ", views=" + views +
                ", is_top='" + is_top + '\'' +
                '}';
    }
}
