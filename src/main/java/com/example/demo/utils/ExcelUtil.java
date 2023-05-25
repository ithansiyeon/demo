package com.example.demo.utils;

import com.example.demo.excel.annotation.ExcelColumn;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ExcelUtil {

    private static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007;

    //Excel 파일 읽어서 list<Map<String, Object>> 형식으로 반환
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

    //엑셀의 셀 값 형식에 맞는 값 반환
    public static Object getCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if(DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
               return StringUtil.nvl(formatter.format(cell.getDateCellValue()));
            } else {
                double num = cell.getNumericCellValue();
                if(num == Math.rint(num)) {
                    return (int) num;
                } else {
                   return num;
                }
            }
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (cell.getCellType() == CellType.BLANK) {
            return "";
        }
        return null;
    }

    //List<Map<String, Object>> 형식을 VO 형식의 리스트로 반환
    public static <T> List<T> convertToVOList(List<Map<String, Object>> mapList, Class<T> voClass) {
        List<T> voList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            T vo = convertToVO(map, voClass);
            voList.add(vo);
        }
        return voList;
    }

    //Map을 객체로 변환함
    public static <T> T convertToVO(Map<String, Object> map, Class<T> voClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(map, voClass);
    }

    public static <T> void oneSheetExcelFile(List<T> dataList, Class<T> type, HttpServletResponse response, String fileName) throws Exception {
        validateData(dataList);
        int maxNum = 1000000;
        int cnt = dataList.size();
        int total = cnt > maxNum ? maxNum:cnt;
        Workbook workbook = new SXSSFWorkbook();
        System.out.println("Math.ceil(cnt/maxNum) = " + (int)Math.ceil((float)cnt/maxNum));

        for(int i=1;i<(int)Math.ceil((float)(cnt+1)/maxNum)+1;i++) {
            Sheet sheet = workbook.createSheet("sheet"+i);
            Cell cell = null;
            Row row = null;
            List<String> headerList = getHeaderName(type);
            row = sheet.createRow(0);
            for (int j=0;j<headerList.size();j++) {
                cell = row.createCell(j);
                cell.setCellValue(headerList.get(i));
            }
            Field[] declaredFields = dataList.get(i).getClass().getDeclaredFields();
            for(int j=0;j<total;j++) {
                row = sheet.createRow(j+1);
                int colCnt = 0;
                for (Field field : declaredFields) {
                    cell = row.createCell(colCnt);
                    field.setAccessible(true);
                    Object o = field.get(dataList.get(j));
                    for(int k=0;k<headerList.size();k++) {
                        if (field.getName().equals(headerList.get(k))) {
                            Object o1 = field.get(dataList.get(j));
                            cell.setCellValue(o1.toString());
                        }
                    }
                    colCnt++;
                }
            }
            total = cnt - maxNum;
        }
        LocalDateTime now = LocalDateTime.now();
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName+"_"+now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".xlsx");
        workbook.write(response.getOutputStream());
        closeWorkBook(workbook);
    }

    protected static <T> List<String> getHeaderName(Class<T> type) {
        List<String> excelHeaderNameList =  Arrays.stream(type.getDeclaredFields())
                .filter(s -> s.isAnnotationPresent(ExcelColumn.class))
                .map(s -> s.getAnnotation(ExcelColumn.class).headerName())
                .collect(Collectors.toCollection(LinkedList::new));
        if(CollectionUtils.isEmpty(excelHeaderNameList)) {
            log.error("헤더 이름이 조회되지 않아 예외 발생!");
            throw new IllegalStateException("헤더 이름이 없습니다.");
        }
        return excelHeaderNameList;
    }

    protected static void closeWorkBook(Workbook workbook) {
        try {
            if(workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T> void validateData(List<T> data) {
        int maxRows = supplyExcelVersion.getMaxRows();
        if (data.size() > maxRows) {
            throw new IllegalArgumentException(
                    String.format("This concrete ExcelFile does not support over %s rows", maxRows));
        }
    }

}
