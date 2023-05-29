package com.example.demo.excel.style;

import com.example.demo.excel.style.align.DefaultExcelAlign;
import com.example.demo.excel.style.align.ExcelAlign;
import com.example.demo.excel.style.border.DefaultExcelBorders;
import com.example.demo.excel.style.border.ExcelBorderStyle;
import com.example.demo.excel.style.color.DefaultExcelColor;
import com.example.demo.excel.style.color.ExcelColor;
import com.example.demo.excel.style.font.DefaultExcelFont;
import com.example.demo.excel.style.font.ExcelFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Example of using ExcelCellStyle as Enum
 */
public enum DefaultExcelCellStyle implements ExcelCellStyle {

	GREY_HEADER(DefaultExcelColor.rgb(217, 217, 217),
			DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER, DefaultExcelFont.font("Arial", false)),
	BLUE_HEADER(DefaultExcelColor.rgb(223, 235, 246),
			DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.CENTER_CENTER, DefaultExcelFont.font("Arial", false)),
	BODY(DefaultExcelColor.rgb(255, 255, 255),
			DefaultExcelBorders.newInstance(ExcelBorderStyle.THIN), DefaultExcelAlign.RIGHT_CENTER, DefaultExcelFont.font("Arial", false));

	private final ExcelColor backgroundColor;
	/**
	 * like CSS margin or padding rule,
	 * List<DefaultExcelBorder> represents rgb TOP RIGHT BOTTOM LEFT
	 */
	private final DefaultExcelBorders borders;
	private final ExcelAlign align;

	private final ExcelFont font;

	DefaultExcelCellStyle(ExcelColor backgroundColor, DefaultExcelBorders borders, ExcelAlign align, ExcelFont font) {
		this.backgroundColor = backgroundColor;
		this.borders = borders;
		this.align = align;
		this.font = font;
	}

	@Override
	public void apply(CellStyle cellStyle, Workbook workbook) {
		backgroundColor.applyForeground(cellStyle);
		borders.apply(cellStyle);
		align.apply(cellStyle);
		font.apply(cellStyle,workbook);
	}

}
