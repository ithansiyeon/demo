package com.example.demo.excel.collection;

import com.example.demo.excel.ExcelCellKey;
import com.example.demo.excel.style.ExcelCellStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

/**
 * PreCalculatedCellStyleMap
 *
 * Determines cell's style
 * In currently, PreCalculatedCellStyleMap determines {org.apache.poi.ss.usermodel.DataFormat}
 *
 */
public class PreCalculatedCellStyleMap {

	private final Map<ExcelCellKey, CellStyle> cellStyleMap = new HashMap<>();
	//fieldType 은 나중에 데이터 값에 따라서 변환 될때 쓰임
	//ex) 원화 나 날짜 기타 등등
	public void put(Class<?> fieldType, ExcelCellKey excelCellKey, ExcelCellStyle excelCellStyle, Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		excelCellStyle.apply(cellStyle, wb);
		cellStyleMap.put(excelCellKey, cellStyle);
	}

	public CellStyle get(ExcelCellKey excelCellKey) {
		return cellStyleMap.get(excelCellKey);
	}

	public boolean isEmpty() {
		return cellStyleMap.isEmpty();
	}

}
