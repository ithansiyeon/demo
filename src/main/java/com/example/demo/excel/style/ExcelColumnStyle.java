package com.example.demo.excel.style;

public @interface ExcelColumnStyle {
	Class<? extends ExcelCellStyle> excelCellStyleClass();
	String enumName() default "";
}
