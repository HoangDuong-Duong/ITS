/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.job;

import com.elcom.its.report.model.dto.ViolationDetailDTO;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class ViolationExportExcel {

    public static final int COLUMN_INDEX_STT = 0;
    public static final int COLUMN_INDEX_TIME = 1;
    public static final int COLUMN_INDEX_POSITION = 2;
    public static final int COLUMN_INDEX_OBJECT_TYPE = 3;
    public static final int COLUMN_INDEX_PLATE = 4;
    public static final int COLUMN_INDEX_VIOLATION_TYPE = 5;
    private static CellStyle cellStyleFormatNumber = null;

    public static int writeExcel(List<ViolationDetailDTO> objectTypeStatisticList,
            String excelFilePath, int rowIndex, Sheet sheet) throws IOException {
        if (objectTypeStatisticList != null && objectTypeStatisticList.size() > 0) {
            // create CellStyle
            CellStyle cellStyle = createStyleForData(sheet);

            // Write data
            for (ViolationDetailDTO objectTypeDTO : objectTypeStatisticList) {
                // Create row
                Row row = sheet.createRow(rowIndex);
                // Write data on row
                writeBook(objectTypeDTO, row, cellStyle);
                rowIndex = rowIndex + 1;
            }
        }

        // Auto resize column witdth
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);

        return rowIndex;
    }

    // Create workbook
    public static Workbook getWorkbook(String excelFilePath) throws IOException {
        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    // Write header with format
    public static void writeHeader(Sheet sheet, int rowIndex) {
        // Create row title
        Row row = sheet.createRow(rowIndex);

        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);

        // Create row
        row = sheet.createRow(rowIndex);
        rowIndex++;

        // Create cells
        Cell cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");

        cell = row.createCell(COLUMN_INDEX_TIME);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Thời gian");

        cell = row.createCell(COLUMN_INDEX_POSITION);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Vị trí");

        cell = row.createCell(COLUMN_INDEX_OBJECT_TYPE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Loại phương tiện");

        cell = row.createCell(COLUMN_INDEX_PLATE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Biển số");

        cell = row.createCell(COLUMN_INDEX_VIOLATION_TYPE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Loại vi phạm");

        // Auto resize column witdth
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);
    }

    // Write data
    private static void writeBook(ViolationDetailDTO objectTypeDTO, Row row, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        Cell cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(row.getRowNum());

        cell = row.createCell(COLUMN_INDEX_TIME);
        cell.setCellStyle(cellStyle);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cell.setCellValue(df.format(objectTypeDTO.getStartTime()));

        cell = row.createCell(COLUMN_INDEX_POSITION);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(objectTypeDTO.getSite().getSiteName());

        cell = row.createCell(COLUMN_INDEX_OBJECT_TYPE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(objectTypeDTO.getObjectName());

        cell = row.createCell(COLUMN_INDEX_PLATE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(objectTypeDTO.getPlate());

        cell = row.createCell(COLUMN_INDEX_VIOLATION_TYPE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(objectTypeDTO.getEventName());

    }

    // Create CellStyle for header
    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeightInPoints((short) 10); // font size

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }

    // Create CellStyle for data
    private static CellStyle createStyleForData(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10); // font size

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }

    // Auto resize column width
    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }
}
