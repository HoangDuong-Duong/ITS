package com.elcom.its.shift.export;

import com.elcom.its.shift.dto.NumberOfShift;
import com.elcom.its.shift.dto.NumberOfShiftByDay;
import com.elcom.its.shift.dto.Unit;
import com.elcom.its.shift.dto.UserShiftGroupByUser;
import com.elcom.its.shift.model.UserShift;
import com.elcom.its.shift.service.impl.ExportFileImpl;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteMonthlyReportExcelFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteMonthlyReportExcelFile.class);

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

    public static int writeHeader(Sheet sheet, int rowIndex, Date startDate, Date endDate, int numOfShift, Unit unit) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        CellStyle cellStyleTitle = createTitleRow(sheet);

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("BẢNG PHÂN CÔNG CA TRỰC\n "
                + unit.getName().toUpperCase() + " THÁNG " + getMonth(startDate));

        CellRangeAddress mergedRegion;
        Row row2 = sheet.createRow(2);
        Cell cell2 = row2.createCell(0);
        cell2.setCellStyle(cellStyleTitle);
        cell2.setCellValue("STT");
        mergedRegion = new CellRangeAddress(2, 3, 0, 0);
        sheet.addMergedRegion(mergedRegion);

        Row row3 = sheet.createRow(3);
        Cell cell3 = row3.createCell(1);
        cell3.setCellStyle(cellStyleTitle);
        cell3.setCellValue("(Tên/Ngày)");

        cell3 = row2.createCell(1);
        cell3.setCellStyle(cellStyleTitle);
        cell3.setCellValue("");

        final String[] dayOfWeek = new String[]{"CN", "H", "B", "T", "N", "S", "B"};
        int indexOfFirstDay = getDayNumberOld(startDate);
        int cellIndex = 0;
        for (int i = 2; i <= getDifferenceDays(startDate, endDate) + 2; i++) {
            Cell cellDay = row3.createCell(i);
            cellDay.setCellStyle(cellStyleTitle);
            cellDay.setCellValue("" + (i - 1));
            Cell cellDayOfWeek = row2.createCell(i);
            cellDayOfWeek.setCellStyle(cellStyleTitle);
            cellDayOfWeek.setCellValue(dayOfWeek[(indexOfFirstDay - 3 + i) % 7]);
            cellIndex = i;
        }
        cellIndex++;

        for (int i = 0; i < numOfShift; i++) {
            Cell emptyCell = row2.createCell(cellIndex);
            emptyCell.setCellStyle(cellStyleTitle);
            emptyCell.setCellValue("");
            Cell cellShift = row3.createCell(cellIndex);
            cellShift.setCellStyle(cellStyleTitle);
            cellShift.setCellValue("Ca " + (i + 1));
            cellIndex++;
        }
        Cell emptyCell = row2.createCell(cellIndex);
        emptyCell.setCellStyle(cellStyleTitle);
        emptyCell.setCellValue("");
        Cell cellShift = row3.createCell(cellIndex);
        cellShift.setCellStyle(cellStyleTitle);
        cellShift.setCellValue("Ca Nghỉ");
        mergedRegion = new CellRangeAddress(0, 1, 0, cellIndex);
        sheet.addMergedRegion(mergedRegion);

        return cellIndex + 1;
    }

    private static Row getRow(Sheet sheet, int rowIndex) {
        if (sheet.getRow(rowIndex) != null) {
            return sheet.getRow(rowIndex);
        } else {
            return sheet.createRow(rowIndex);
        }
    }

    private static void writeEmptyCell(Sheet sheet, int startRow, int endRow, int startCell, int endCell) {
        CellStyle cellStyleNormal = createTitleRow(sheet);
        for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
            for (int cellIndex = startCell; cellIndex <= endCell; cellIndex++) {
                Row row = getRow(sheet, rowIndex);
                Cell cell = row.createCell(cellIndex);
                cell.setCellStyle(cellStyleNormal);
                cell.setCellValue("");
            }
        }
    }

    private static void writeZeroValueCell(Sheet sheet, int startRow, int endRow, int startCell, int endCell) {
        CellStyle cellStyleNormal = createTitleRow(sheet);
        for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
            for (int cellIndex = startCell; cellIndex <= endCell; cellIndex++) {
                Row row = getRow(sheet, rowIndex);
                Cell cell = row.createCell(cellIndex);
                cell.setCellStyle(cellStyleNormal);
                cell.setCellValue("");
            }
        }
    }

    public static int writeContent(Sheet sheet, List<UserShiftGroupByUser> listUserShiftGroupByUsers, Date startDate, Date endDate, int numOfShift) {
        CellStyle cellStyleNormal = createTitleRow(sheet);
        int rowIndex = 4;
        int userIndex = 1;
        long numsOfDay = getDifferenceDays(startDate, endDate);
        for (UserShiftGroupByUser userShiftGroupByUser : listUserShiftGroupByUsers) {
            int cellIndex = 0;
            Row row = sheet.createRow(rowIndex);
            Cell cell = row.createCell(cellIndex);
            cell.setCellStyle(cellStyleNormal);
            cell.setCellValue(userIndex);
            cellIndex++;
            cell = row.createCell(cellIndex);
            cell.setCellStyle(cellStyleNormal);
            cell.setCellValue(userShiftGroupByUser.getUsername());
            for (UserShift userShift : userShiftGroupByUser.getListUserShift()) {
                int cellContentIndex = cellIndex + getDay(userShift.getDay());
                cell = row.createCell(cellContentIndex);
                cell.setCellStyle(cellStyleNormal);
                cell.setCellValue(userShift.getViewNumsOfShift());
            }
            writeZeroValueCell(sheet, rowIndex, rowIndex, (int) numsOfDay + 3, (int) numsOfDay + numOfShift + 3);
            for (NumberOfShift numberOfShiftByUser : userShiftGroupByUser.getListNumberOfShiftByUsers()) {
                int cellContentIndex;
                if (numberOfShiftByUser.getShiftNumber() == 0) {
                    cellContentIndex = (int) numsOfDay + numOfShift + 3;
                } else {
                    cellContentIndex = (int) numsOfDay + numberOfShiftByUser.getShiftNumber() + 2;
                }
                cell = row.createCell(cellContentIndex);
                cell.setCellStyle(cellStyleNormal);
                cell.setCellValue(numberOfShiftByUser.getTotal());
            }
            rowIndex++;
            userIndex++;
        }
        return rowIndex;
    }

    public static int writeContent(Sheet sheet, int rowIndex, int numsOfShift, List<NumberOfShiftByDay> listNumberOfShiftByDay) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        CellStyle cellStyleTitle = createTitleRow(sheet);
        for (NumberOfShiftByDay numberOfShiftByDay : listNumberOfShiftByDay) {
            for (NumberOfShift numberOfShift : numberOfShiftByDay.getListNumberOfShifts()) {
                int shiftRowIndex;
                if (numberOfShift.getShiftNumber() == 0) {
                    shiftRowIndex = rowIndex + numsOfShift;
                } else {
                    shiftRowIndex = rowIndex + numberOfShift.getShiftNumber() - 1;
                }

                Row row = sheet.getRow(shiftRowIndex) != null ? sheet.getRow(shiftRowIndex) : sheet.createRow(shiftRowIndex);
                Cell cell = row.createCell(getDay(numberOfShiftByDay.getDay()) + 1);
                cell.setCellStyle(cellStyleTitle);
                cell.setCellValue(numberOfShift.getTotal());
            }
        }

        for (int i = 0; i < numsOfShift; i++) {
            Row row = sheet.getRow(rowIndex) != null ? sheet.getRow(rowIndex) : sheet.createRow(rowIndex);
            Cell cell = row.createCell(0);
            cell.setCellStyle(cellStyleTitle);
            cell.setCellValue("Ca " + (i + 1));
            Cell emptyCell = row.createCell(1);
            emptyCell.setCellStyle(cellStyleTitle);
            emptyCell.setCellValue("");
            CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 0, 1);
            sheet.addMergedRegion(mergedRegion);
            rowIndex++;
        }
        Row row = sheet.getRow(rowIndex) != null ? sheet.getRow(rowIndex) : sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyleTitle);
        cell.setCellValue("Ca Nghỉ");
        Cell emptyCell = row.createCell(1);
        emptyCell.setCellStyle(cellStyleTitle);
        emptyCell.setCellValue("");
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 0, 1);
        sheet.addMergedRegion(mergedRegion);
        rowIndex++;
        return rowIndex;
    }

    public static void writeFooter(Sheet sheet, int rowIndex, int cellIndex, int numsOfShift) {
        CellStyle footerStyle = createFooterTitle(sheet);
        CellStyle cellStyleTitle = createTitleRow(sheet);
        for (int i = rowIndex - numsOfShift - 1; i < rowIndex; i++) {
            for (int j = cellIndex - numsOfShift - 1; j < cellIndex; j++) {
                Row row = getRow(sheet, i);
                Cell cell = row.createCell(j);
                cell.setCellStyle(cellStyleTitle);
                cell.setCellValue("");
            }
        }
        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(cellIndex);
        cell.setCellStyle(footerStyle);
        cell.setCellValue("ĐỘI TRƯỞNG");
    }

    public static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex, true);
        }
    }

    private static CellStyle createStyleForHeader(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }

    private static CellStyle createStyleRow(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }

    private static CellStyle createTitleRow(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setItalic(false);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }

    private static CellStyle createTitle(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        return cellStyle;
    }

    private static CellStyle createFooterTitle(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        return cellStyle;
    }

    private static int getDayNumberOld(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    private static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private static int getDay(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    private static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }
}
