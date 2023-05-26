package com.example.demo.excel;

import com.example.demo.excel.collection.PreCalculatedCellStyleMap;
import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;

public class ExcelRenderResource {
	private PreCalculatedCellStyleMap styleMap;
	private List<String> dataFieldNames;

	public ExcelRenderResource(PreCalculatedCellStyleMap styleMap, List<String> dataFieldNames) {
		this.styleMap = styleMap;
		this.dataFieldNames = dataFieldNames;
	}

	public CellStyle getCellStyle(String dataFieldName, ExcelRenderLocation excelRenderLocation) {
		return styleMap.get(ExcelCellKey.of(dataFieldName, excelRenderLocation));
	}

	public List<String> getDataFieldNames() {
		return dataFieldNames;
	}

}
