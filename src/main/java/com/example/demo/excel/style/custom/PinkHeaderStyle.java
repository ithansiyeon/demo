package com.example.demo.excel.style.custom;

import com.example.demo.excel.configurer.ExcelCellStyleConfigurer;
import com.example.demo.excel.style.align.DefaultExcelAlign;
import com.example.demo.excel.style.border.DefaultExcelBorders;
import com.example.demo.excel.style.border.ExcelBorderStyle;
import com.example.demo.excel.style.font.DefaultExcelFont;

public class PinkHeaderStyle extends CustomExcelCellStyle {
    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(255, 203, 192)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.DOTTED))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER)
                .excelFont(DefaultExcelFont.font("Arial",true));
    }

}