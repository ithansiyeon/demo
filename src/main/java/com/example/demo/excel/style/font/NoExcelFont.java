package com.example.demo.excel.style.font;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public class NoExcelFont implements ExcelFont {
    @Override
    public void apply(CellStyle cellStyle, Workbook workbook) {
        // Do nothing
    }
}
