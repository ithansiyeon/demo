package com.example.demo.utils;

import com.example.demo.excel.ExcelCellKey;
import com.example.demo.excel.ExcelRenderLocation;
import com.example.demo.excel.ExcelRenderResource;
import com.example.demo.excel.annotation.DefaultBodyStyle;
import com.example.demo.excel.annotation.DefaultHeaderStyle;
import com.example.demo.excel.annotation.ExcelColumn;
import com.example.demo.excel.annotation.ExcelColumnStyle;
import com.example.demo.excel.collection.PreCalculatedCellStyleMap;
import com.example.demo.excel.exception.InvalidExcelCellStyleException;
import com.example.demo.excel.exception.NoExcelColumnAnnotationsException;
import com.example.demo.excel.style.ExcelCellStyle;
import com.example.demo.excel.style.NoExcelCellStyle;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    //maxrows가 넘어가면 시트가 추가됨
    public static <T> void multiSheetExcelFile(List<T> dataList, Class<T> clazz, HttpServletResponse response, String fileName) throws Exception {
//        int maxrows = supplyExcelVersion.getMaxRows();
        int maxrows = 15;
        int cnt = dataList.size();
        int total = cnt > maxrows ? maxrows:cnt;
        int start = 0;
        Workbook workbook = new SXSSFWorkbook();
        ExcelRenderResource resource = prepareRenderResource(clazz,workbook);

        for(int i=1;i<(int)Math.ceil((float)(cnt+1)/maxrows)+1;i++) {
            Sheet sheet = workbook.createSheet("sheet"+i);
            Cell cell = null;
            Row row = null;
            row = sheet.createRow(0);

            List<String> dataFieldNames = resource.getDataFieldNames();
            int colNum = 0;
            //header 값 세팅
            for (String dataFieldName : dataFieldNames) {
                cell = row.createCell(colNum);
                cell.setCellValue(dataFieldName);
                cell.setCellStyle(resource.getCellStyle(dataFieldName, ExcelRenderLocation.HEADER));
                colNum++;
            }
            int rowNum = 1;
            //body 안의 값 세팅
            for(int j=start;j<total;j++) {
                Field[] declaredFields = dataList.get(j).getClass().getDeclaredFields();
                row = sheet.createRow(rowNum);
                colNum = 0;
                rowNum++;
                for (Field field : declaredFields) {
                    for (String dataFieldName : dataFieldNames) {
                        if (field.getName().equals(dataFieldName)) {
                            cell = row.createCell(colNum);
                            field.setAccessible(true);
                            if(field.get(dataList.get(j))!=null) {
                                cell.setCellValue(field.get(dataList.get(j)).toString());
                            } else {
                                cell.setCellValue("");
                            }
                            cell.setCellStyle(resource.getCellStyle(dataFieldName,ExcelRenderLocation.BODY));
                            colNum++;
                        }
                    }

                }
            }
            start = total;
            if(total + maxrows <= cnt) {
                total+=maxrows;
            } else {
                total = cnt;
            }
        }
        LocalDateTime now = LocalDateTime.now();
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName+"_"+now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".xlsx");
        workbook.write(response.getOutputStream());
        closeWorkBook(workbook);
    }

    //헤더와 바디에 있는 애노테이션 정보를 통해서 style 가져오기 위한 메소드
    public static ExcelRenderResource prepareRenderResource(Class<?> type, Workbook wb) {
        PreCalculatedCellStyleMap styleMap = new PreCalculatedCellStyleMap();
        List<String> fieldNames = new ArrayList<>();
        //객체 위에 선언 되는 header 애노테이션 타입
        ExcelColumnStyle classDefinedHeaderStyle = getHeaderExcelColumnStyle(type);
        //객체 위에 선언 되는 body 에노테이션 타입
        ExcelColumnStyle classDefinedBodyStyle = getBodyExcelColumnStyle(type);

        //객체 컬럼의 field 값의 이름과 style 정보들을 styleMap에 넣어줌
        for (Field field : getAllFields(type)) {
            if (field.isAnnotationPresent(ExcelColumn.class)) {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                styleMap.put(
                        String.class,
                        ExcelCellKey.of(field.getName(), ExcelRenderLocation.HEADER),
                        getCellStyle(decideAppliedStyleAnnotation(classDefinedHeaderStyle, annotation.headerStyle())), wb);
                Class<?> fieldType = field.getType();
                styleMap.put(
                        fieldType,
                        ExcelCellKey.of(field.getName(), ExcelRenderLocation.BODY),
                        getCellStyle(decideAppliedStyleAnnotation(classDefinedBodyStyle, annotation.bodyStyle())), wb);
                fieldNames.add(field.getName());
            }
        }

        if (styleMap.isEmpty()) {
            throw new NoExcelColumnAnnotationsException(String.format("Class %s has not @ExcelColumn at all", type));
        }
        return new ExcelRenderResource(styleMap, fieldNames);
    }

    private static void closeWorkBook(Workbook workbook) {
        try {
            if(workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //클래스의 부모 필드까지 다 가져옴
    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz)) {
            fields.addAll(Arrays.asList(clazzInClasses.getDeclaredFields()));
        }
        return fields;
    }

    //클래스의 부모 애노테이션까지 다 가져옴
    private static Annotation getAnnotation(Class<?> clazz,
                                            Class<? extends Annotation> targetAnnotation) {
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz)) {
            if (clazzInClasses.isAnnotationPresent(targetAnnotation)) {
                return clazzInClasses.getAnnotation(targetAnnotation);
            }
        }
        return null;
    }

    private static List<Class<?>> getAllClassesIncludingSuperClasses(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        while (clazz != null) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return classes;
    }

    //cellStyle 에서 Enum 값이 있는지 확인 한 후 없으면 객체를 넘겨 줌
    private static ExcelCellStyle getCellStyle(ExcelColumnStyle excelColumnStyle) {
        Class<? extends ExcelCellStyle> excelCellStyleClass = excelColumnStyle.excelCellStyleClass();
        // 1. Case of Enum
        if (excelCellStyleClass.isEnum()) {
            String enumName = excelColumnStyle.enumName();
            return findExcelCellStyle(excelCellStyleClass, enumName);
        }

        // 2. Case of Class
        try {
            return excelCellStyleClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InvalidExcelCellStyleException(e.getMessage(), e);
        }
    }

    //Enum에 있는 값을 가지고 적용된 cellstyle을 찾기 위한 메소드
    private static ExcelCellStyle findExcelCellStyle(Class<?> excelCellStyles, String enumName) {
        try {
            return (ExcelCellStyle) Enum.valueOf((Class<Enum>) excelCellStyles, enumName);
        } catch (NullPointerException e) {
            throw new InvalidExcelCellStyleException("enumName must not be null", e);
        } catch (IllegalArgumentException e) {
            throw new InvalidExcelCellStyleException(
                    String.format("Enum %s does not name %s", excelCellStyles.getName(), enumName), e);
        }
    }

    //헤더에 있는 애노테이션을 가져오기 위한 메소드
    private static ExcelColumnStyle getHeaderExcelColumnStyle(Class<?> clazz) {
        Annotation annotation = getAnnotation(clazz, DefaultHeaderStyle.class);
        if (annotation == null) {
            return null;
        }
        return ((DefaultHeaderStyle) annotation).style();
    }

    //바디에 있는 애노테이션을 가져오기 위한 메소드
    private static ExcelColumnStyle getBodyExcelColumnStyle(Class<?> clazz) {
        Annotation annotation = getAnnotation(clazz, DefaultBodyStyle.class);
        if (annotation == null) {
            return null;
        }
        return ((DefaultBodyStyle) annotation).style();
    }

    //상위에 선언된 애노테이션 보다 하위 애노테이션을 적용하기 위한 메소드
    private static ExcelColumnStyle decideAppliedStyleAnnotation(ExcelColumnStyle classAnnotation,
                                                                 ExcelColumnStyle fieldAnnotation) {
        if (fieldAnnotation.excelCellStyleClass().equals(NoExcelCellStyle.class) && classAnnotation != null) {
            return classAnnotation;
        }
        return fieldAnnotation;
    }

}
