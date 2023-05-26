package com.example.demo.excel.style.custom;

import com.example.demo.excel.configurer.ExcelCellStyleConfigurer;
import com.example.demo.excel.style.align.DefaultExcelAlign;
import com.example.demo.excel.style.border.DefaultExcelBorders;
import com.example.demo.excel.style.border.ExcelBorderStyle;

public class BlueBodyStyle extends CustomExcelCellStyle {

    @Override
    public void configure(ExcelCellStyleConfigurer configurer) {
        configurer.foregroundColor(223, 235, 246)
                .excelBorders(DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN))
                .excelAlign(DefaultExcelAlign.CENTER_CENTER);
    }

}