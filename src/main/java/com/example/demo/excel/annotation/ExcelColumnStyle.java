package com.example.demo.excel.annotation;

import com.example.demo.excel.style.ExcelCellStyle;

//@Target(ElementType.FIELD)
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ExcelColumnStyle {
//
//}
public @interface ExcelColumnStyle {

    Class<? extends ExcelCellStyle> excelCellStyleClass();

    String enumName() default "";

}