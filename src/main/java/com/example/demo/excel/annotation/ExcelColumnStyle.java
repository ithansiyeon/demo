package com.example.demo.excel.annotation;

import com.example.demo.excel.style.ExcelCellStyle;

public @interface ExcelColumnStyle {
    Class<? extends ExcelCellStyle> excelCellStyleClass();
    String enumName() default "";

}