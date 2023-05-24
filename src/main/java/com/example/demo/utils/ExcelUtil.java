package com.example.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    public static List<Map<String, Object>> readExcel(MultipartFile file) throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Row headerRow = sheet.getRow(0);
        int columnCount = headerRow.getPhysicalNumberOfCells();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row currentRow = sheet.getRow(i);
            Map<String, Object> rowData = new HashMap<>();
            for (int j = 0; j < columnCount; j++) {
                Cell currentCell = currentRow.getCell(j);
                Cell headerCell = headerRow.getCell(j);
                String columnName = headerCell.getStringCellValue();
                Object cellValue = getCellValue(currentCell);
                rowData.put(columnName, cellValue);
            }
            data.add(rowData);
        }
        workbook.close();
        return data;
    }

    public static Object getCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        }
        return null;
    }

    public static <T> List<T> convertToVOList(List<Map<String, Object>> mapList, Class<T> voClass) {
        List<T> voList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            T vo = convertToVO(map, voClass);
            voList.add(vo);
        }
        return voList;
    }

    public static <T> T convertToVO(Map<String, Object> map, Class<T> voClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, voClass);
    }

}
