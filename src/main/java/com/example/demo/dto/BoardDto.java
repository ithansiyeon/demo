package com.example.demo.dto;

import com.example.demo.entity.Board;
import com.example.demo.excel.annotation.DefaultBodyStyle;
import com.example.demo.excel.annotation.DefaultHeaderStyle;
import com.example.demo.excel.annotation.ExcelColumn;
import com.example.demo.excel.annotation.ExcelColumnStyle;
import com.example.demo.excel.style.DefaultExcelCellStyle;
import com.example.demo.excel.style.custom.PinkHeaderStyle;
import com.example.demo.excel.style.custom.BlueBodyStyle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DefaultHeaderStyle(
        style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BLUE_HEADER")
)
@DefaultBodyStyle(
        style = @ExcelColumnStyle(excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BODY")
)
public class BoardDto {
    @ExcelColumn(headerName = "id", headerStyle = @ExcelColumnStyle(excelCellStyleClass = PinkHeaderStyle.class), bodyStyle = @ExcelColumnStyle(excelCellStyleClass = BlueBodyStyle.class))
    Long id;
    @ExcelColumn(headerName = "name")
    String name;
    @ExcelColumn(headerName = "writer")
    String writer;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.writer = board.getWriter();
    }

    @Override
    public String toString() {
        return "BoardDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", writer='" + writer + '\'' +
                '}';
    }
}
