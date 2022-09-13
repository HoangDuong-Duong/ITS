/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.job;

import com.elcom.its.report.model.dto.report.AggTroubleInfo;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 *
 * @author Admin
 */
@Component
public class TroubleReportExportExcel {

    public static final int COLUMN_INDEX_STT = 0;
    public static final int COLUMN_INDEX_DAY = 1;
    public static final int COLUMN_INDEX_HOUR = 2;
    public static final int COLUMN_INDEX_POSITION = 3;
    public static final int COLUMN_INDEX_DIRECTION = 4;
    public static final int COLUMN_INDEX_CONTENT = 5;
    public static final int COLUMN_INDEX_PROCESS = 6;
    public static final int COLUMN_INDEX_TOTALTIME = 7;

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

    public static void setColumnWidthHeight(Sheet sheet) {
        sheet.setColumnWidth(COLUMN_INDEX_STT, (5 * 256));
        sheet.setColumnWidth(COLUMN_INDEX_DAY, (12 * 256));
        sheet.setColumnWidth(COLUMN_INDEX_HOUR, (18 * 256));
        sheet.setColumnWidth(COLUMN_INDEX_POSITION, (16 * 256));
        sheet.setColumnWidth(COLUMN_INDEX_DIRECTION, (10 * 256));
        sheet.setColumnWidth(COLUMN_INDEX_CONTENT, (37 * 256));
        sheet.setColumnWidth(COLUMN_INDEX_PROCESS, (37 * 256));
        sheet.setColumnWidth(COLUMN_INDEX_TOTALTIME, (10 * 256));
    }

    public static int writeHeader(Sheet sheet, String month) {
        setColumnWidthHeight(sheet);
        int rowIndex = 0;
        mergeRegion(sheet, 0, 0, 0, COLUMN_INDEX_TOTALTIME);
        Row row = getRow(sheet, rowIndex);
        CellStyle titleCellStyle = createStyleForTitle(sheet);
        CellStyle headerCellStyle = createStyleForHeader(sheet);
        Cell cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellStyle(headerCellStyle);
        cell.setCellValue("BẢNG TỔNG HỢP SỐ GIỜ ĐVH ĐẢM BẢO ATGT TRONG THÁNG " + month);
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellStyle(titleCellStyle);
        cell.setCellValue("STT");

        cell = row.createCell(COLUMN_INDEX_DAY);
        cell.setCellStyle(titleCellStyle);
        cell.setCellValue("Ngày");

        cell = row.createCell(COLUMN_INDEX_HOUR);
        cell.setCellStyle(titleCellStyle);
        cell.setCellValue("Giờ");

        cell = row.createCell(COLUMN_INDEX_POSITION);
        cell.setCellStyle(titleCellStyle);
        cell.setCellValue("Lý trình");

        cell = row.createCell(COLUMN_INDEX_DIRECTION);
        cell.setCellStyle(titleCellStyle);
        cell.setCellValue("Hướng");

        cell = row.createCell(COLUMN_INDEX_CONTENT);
        cell.setCellStyle(titleCellStyle);
        cell.setCellValue("Nội dung");

        cell = row.createCell(COLUMN_INDEX_PROCESS);
        cell.setCellStyle(titleCellStyle);
        cell.setCellValue("Xử lý");

        cell = row.createCell(COLUMN_INDEX_TOTALTIME);
        cell.setCellStyle(titleCellStyle);
        cell.setCellValue("Số giờ");
        return rowIndex + 1;
    }

    public static int writeContent(Sheet sheet, List<AggTroubleInfo> listAggTroubleInfos, int rowIndex) {
        CellStyle contentCellStyle = createStyleForData(sheet);
        CellStyle contentItalicCellStyle = createStyleForItalicData(sheet);
        CellStyle titleCellStyle = createStyleForTitle(sheet);
        CellStyle headerCellStyle = createStyleForHeader(sheet);
        int dataIndex = 1;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        long totalTime = 0;
        for (AggTroubleInfo aggTroubleInfo : listAggTroubleInfos) {
            Row row = sheet.createRow(rowIndex);

            Cell cell = row.createCell(COLUMN_INDEX_STT);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(dataIndex);

            cell = row.createCell(COLUMN_INDEX_DAY);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(simpleDateFormat.format(aggTroubleInfo.getStartTime()));

            cell = row.createCell(COLUMN_INDEX_HOUR);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(getTimeContent(aggTroubleInfo.getStartTime(), aggTroubleInfo.getEndTime()));

            cell = row.createCell(COLUMN_INDEX_POSITION);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(aggTroubleInfo.getPosition());

            cell = row.createCell(COLUMN_INDEX_DIRECTION);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(aggTroubleInfo.getDirectionCode());

            cell = row.createCell(COLUMN_INDEX_CONTENT);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue(aggTroubleInfo.getContent());

            cell = row.createCell(COLUMN_INDEX_PROCESS);
            cell.setCellStyle(contentItalicCellStyle);
            cell.setCellValue(aggTroubleInfo.getProcessedContent());

            cell = row.createCell(COLUMN_INDEX_TOTALTIME);
            cell.setCellStyle(contentCellStyle);
            cell.setCellValue("");

            if (aggTroubleInfo.getEndTime() != null) {
                long processTime = aggTroubleInfo.getEndTime().getTime() / (60 * 1000) - aggTroubleInfo.getStartTime().getTime() / (60 * 1000);
                if (processTime < 0) {
                    dataIndex++;
                    rowIndex++;
                    continue;
                }
                cell = row.createCell(COLUMN_INDEX_TOTALTIME);
                cell.setCellStyle(contentCellStyle);
                cell.setCellValue(convertMillisecondsToTime(processTime));
                totalTime += processTime;
            }

            dataIndex++;
            rowIndex++;
        }
        mergeRegion(sheet, rowIndex, rowIndex, 0, COLUMN_INDEX_PROCESS);
        Row row = getRow(sheet, rowIndex);
        Cell cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellStyle(headerCellStyle);
        cell.setCellValue("Tổng");
        cell = row.createCell(COLUMN_INDEX_TOTALTIME);
        cell.setCellStyle(contentCellStyle);
        cell.setCellValue(convertMillisecondsToTime(totalTime));
        rowIndex++;
        autosizeRow(sheet, rowIndex);
        return rowIndex;
    }

    private static void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstColumn, int lastColumn) {
        CellRangeAddress mergedRegion = new CellRangeAddress(firstRow, lastRow, firstColumn, lastColumn);
        sheet.addMergedRegion(mergedRegion);
        writeEmptyCell(sheet, firstRow, lastRow, firstColumn, lastColumn);
    }

    private static Row getRow(Sheet sheet, int index) {
        if (sheet.getRow(index) != null) {
            return sheet.getRow(index);
        } else {
            return sheet.createRow(index);
        }
    }

    private static void writeEmptyCell(Sheet sheet, int startRow, int endRow, int startCell, int endCell) {
        CellStyle cellStyleNormal = createStyleForData(sheet);
        for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
            for (int cellIndex = startCell; cellIndex <= endCell; cellIndex++) {
                Row row = getRow(sheet, rowIndex);
                Cell cell = row.createCell(cellIndex);
                cell.setCellStyle(cellStyleNormal);
                cell.setCellValue("");
            }
        }
    }

    private static String getTimeContent(Date startTime, Date endTime) {
        String returnString = startTime.getHours() + "h" + startTime.getMinutes() + "'";
        if (endTime != null) {
            returnString += " đến " + endTime.getHours() + "h" + endTime.getMinutes() + "'";
        }
        return returnString;
    }

    private static String convertMillisecondsToTime(long totalMinutes) {
        long minutes = totalMinutes % 60;
        long totalHours = totalMinutes / 60;
        long hours = totalHours % 24;
        long day = totalHours / 24;
        String returnString = minutes + "'";
        if (hours > 0) {
            returnString = hours + "h" + returnString;
        }
        if (day > 0) {
            returnString = day + "ng" + returnString;
        }
        return returnString;
    }

    private static CellStyle createStyleForTitle(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 12); // font size
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private static CellStyle createStyleForHeader(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private static CellStyle createStyleForData(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 12); // font size

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private static CellStyle createStyleForItalicData(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 12); // font size
        font.setItalic(true);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }

    private static void autosizeRow(Sheet sheet, int lastRow) {
        for (int rowIndex = 0; rowIndex < lastRow; rowIndex++) {
            getRow(sheet, rowIndex).setHeight((short) -1);
        }
    }
}
