package com.example.demo.excel.style.custom;

import com.example.demo.excel.configurer.ExcelCellStyleConfigurer;
import com.example.demo.excel.style.ExcelCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class CustomExcelCellStyle implements ExcelCellStyle {

	private final ExcelCellStyleConfigurer configurer;

	public CustomExcelCellStyle() {
		configurer = new ExcelCellStyleConfigurer();
		configure(configurer);
	}

	public abstract void configure(ExcelCellStyleConfigurer configurer);

	@Override
	public void apply(CellStyle cellStyle, Workbook workbook) {
		configurer.configure(cellStyle, workbook);
	}

}
