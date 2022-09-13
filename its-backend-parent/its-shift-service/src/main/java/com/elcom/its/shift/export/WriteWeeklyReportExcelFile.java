package com.elcom.its.shift.export;

import com.elcom.its.shift.dto.Stage;
import com.elcom.its.shift.dto.Unit;
import com.elcom.its.shift.dto.UserOnShiftByStage;
import com.elcom.its.shift.dto.WeeklyReportGroupByShift;
import com.elcom.its.shift.dto.WeeklyShiftReportContent;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteWeeklyReportExcelFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteWeeklyReportExcelFile.class);

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

    public static int writeHeader(Sheet sheet, Unit unit, List<Stage> listStage, int numsOfShift, Date startDate, Date endDate) {
        CellStyle cellHeaderStyle = createStyleForHeader(sheet);
        CellStyle cellStyleTitleRow = createStyleForTitleRow(sheet);
        CellStyle cellStyleNormal = createTitle(sheet);
        CellStyle cellFooterStyle = createFooterStyle(sheet);
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellHeaderStyle);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
        SimpleDateFormat dayMonthDateFormat = new SimpleDateFormat("dd/MM");
        cell.setCellValue("BẢNG PHÂN CÔNG CA TRỰC " + unit.getName().toUpperCase()
                + "\n(Từ ngày " + simpleDateFormat.format(startDate) + " đến " + simpleDateFormat.format(endDate) + ")"
        );

        Row row2 = getRow(sheet, 2);
        Row row3 = getRow(sheet, 3);

        Cell cell20 = row2.createCell(0);
        cell20.setCellStyle(cellStyleTitleRow);
        cell20.setCellValue("Thời gian");
        CellRangeAddress mergedRegion = new CellRangeAddress(2, 3, 0, 0);
        sheet.addMergedRegion(mergedRegion);

        Cell cell21 = row2.createCell(1);
        cell21.setCellStyle(cellStyleTitleRow);
        cell21.setCellValue("Ca");

        mergedRegion = new CellRangeAddress(2, 3, 1, 1);
        sheet.addMergedRegion(mergedRegion);

        Cell cell22 = row2.createCell(2);
        cell22.setCellStyle(cellStyleTitleRow);
        cell22.setCellValue("Vị trí");

        int rowIndex = 4;
        for (Date day = startDate; day.getTime() <= endDate.getTime(); day = getNextday(day)) {
            int firstRowIndex = rowIndex;
            Row dayRow = getRow(sheet, rowIndex);
            Cell dayCell = dayRow.createCell(0);
            dayCell.setCellStyle(cellStyleTitleRow);
            dayCell.setCellValue(getDayOfWeek(day) + "\n" + dayMonthDateFormat.format(day));
            for (int i = 1; i <= numsOfShift; i++) {
                Row shiftRow = getRow(sheet, rowIndex);
                Cell shiftCell = shiftRow.createCell(1);
                shiftCell.setCellStyle(cellStyleTitleRow);
                shiftCell.setCellValue("Ca " + i);
                rowIndex++;
            }

            Row shiftRow = getRow(sheet, rowIndex);
            Cell shiftCell = shiftRow.createCell(1);
            shiftCell.setCellStyle(cellStyleTitleRow);
            shiftCell.setCellValue("Nghỉ");

            mergedRegion = new CellRangeAddress(firstRowIndex, rowIndex, 0, 0);
            sheet.addMergedRegion(mergedRegion);
            rowIndex++;

        }
        int cellIndex = 2;
        for (Stage stage : listStage) {
            Cell shiftCell = row3.createCell(cellIndex);
            shiftCell.setCellStyle(cellStyleTitleRow);
            shiftCell.setCellValue(stage.getStageName());
            cellIndex++;
        }
        cellIndex--;
        createMergeRegion(sheet, 0, 1, 0, cellIndex, 0, 0);
        if (cellIndex > 2) {
            createMergeRegion(sheet, 2, 2, 2, cellIndex, 2, 2);
        }

        writeEmptyCell(sheet, 4, rowIndex-1, 2, cellIndex);
        cellIndex++;
        Row footerRow = getRow(sheet, rowIndex);
        Cell footerCell = footerRow.createCell(cellIndex);
        footerCell.setCellStyle(cellFooterStyle);
        footerCell.setCellValue("ĐỘI TRƯỞNG");
        return rowIndex + 1;
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

    static void createMergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCell, int lastCell, int importantRow, int importtantCell) {
        CellStyle cellStyleTitleRow = createStyleForTitleRow(sheet);
        for (int i = firstRow; i <= lastRow; i++) {
            for (int j = firstCell; j <= lastCell; j++) {
                if (i == importantRow && j == importantRow) {
                    continue;
                }
                Row row = getRow(sheet, i);
                Cell emptyCell = row.createCell(j);
                emptyCell.setCellStyle(cellStyleTitleRow);
                emptyCell.setCellValue("");
            }
        }
        CellRangeAddress mergedRegion = new CellRangeAddress(firstRow, lastRow, firstCell, lastCell);
        sheet.addMergedRegion(mergedRegion);
    }

    private static Row getRow(Sheet sheet, int index) {
        if (sheet.getRow(index) != null) {
            return sheet.getRow(index);
        } else {
            return sheet.createRow(index);
        }
    }

    public static String getDayOfWeek(Date date) {
        String[] dayOfWeek = new String[]{"Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayIndex = cal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek[dayIndex - 1];
    }

    public static Date getNextday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    public static void writeContent(Sheet sheet, int numsOfShift, List<WeeklyShiftReportContent> listContents, List<Stage> listStages, Date startTime) {
        CellStyle cellStyleTitleRow = createTitleRow(sheet);
        for (WeeklyShiftReportContent content : listContents) {
            for (WeeklyReportGroupByShift contentByShift : content.getListWeeklyReportGroupByShifts()) {
                for (UserOnShiftByStage userOnShiftByStage : contentByShift.getListUserOnShiftByStages()) {
                    int rowIndex = getRowIndex(startTime, numsOfShift, contentByShift.getShiftNumber(), content.getDay());
                    int cellIndex = getCellIndex(listStages, userOnShiftByStage.getStageCode());
                    Row row = getRow(sheet, rowIndex);
                    Cell cell = row.createCell(cellIndex);
                    cell.setCellStyle(cellStyleTitleRow);
                    cell.setCellValue(userOnShiftByStage.getListUser());

                }
            }
        }

    }

    private static int getRowIndex(Date startDate, int numsOfShift, int shiftNumber, Date day) {
        if (shiftNumber == 0) {
            return 3 + (numsOfShift + 1) * (int) getDifferenceDays(startDate, day) + (numsOfShift + 1);
        } else {
            return 3 + (numsOfShift + 1) * (int) getDifferenceDays(startDate, day) + shiftNumber;
        }

    }

    private static int getCellIndex(List<Stage> listStages, String stageCode) {
        int index = 0;
        for (Stage stage : listStages) {
            if (stage.getCode().equalsIgnoreCase(stageCode)) {
                break;
            }
            index++;
        }
        return index + 2;
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

    private static CellStyle createStyleForTitleRow(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
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
    
    private static CellStyle createFooterStyle(Sheet sheet) {
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
