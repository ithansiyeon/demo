package com.example.demo.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public class NoExcelCellStyle implements ExcelCellStyle {

	@Override
	public void apply(CellStyle cellStyle, Workbook workbook) {
		// Do nothing
	}

}
