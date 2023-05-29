package com.example.demo.excel.style.font;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

public class DefaultExcelFont implements ExcelFont{

    private String fontName = "Arial";
    private Boolean bold = false;

    public DefaultExcelFont(String fontName, Boolean bold) {
        this.fontName = fontName;
        this.bold = bold;
    }

    public static DefaultExcelFont font(String fontName, Boolean bold) {
        return new DefaultExcelFont(fontName, bold);
    }

    @Override
    public void apply(CellStyle cellStyle, Workbook workbook) {
        Font font = workbook.createFont();
        font.setFontName(fontName);
        font.setBold(bold);
        cellStyle.setFont(font);
    }
}
