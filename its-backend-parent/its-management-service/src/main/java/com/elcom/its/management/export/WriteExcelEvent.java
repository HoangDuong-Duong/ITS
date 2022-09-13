package com.elcom.its.management.export;

import com.elcom.its.management.dto.EventInfo;
import com.elcom.its.management.dto.HistoryDisplayScript;
import com.elcom.its.management.dto.ReportOnlineStatusDTO;
import com.elcom.its.management.enums.JobType;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class WriteExcelEvent {
    private static CellStyle cellStyleFormatNumber = null;

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

    public static void writeHeader(Sheet sheet, int rowIndex, String textHeader, String date, Workbook wb) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        CellStyle cellStyleTitle = createTitleTop(sheet);
        CellStyle styleForHeaderRight = createStyleForHeaderRight(sheet);
        CellStyle cellStyleNormal = createStyleForHeaderNoBorder(sheet);
        CellStyle cellStyleNormalNoBorder = createStyleForHeaderNoboder(sheet);
        Row row = sheet.createRow(0);
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 5, 0, 3); // row 0 col A and P
        sheet.addMergedRegion(mergedRegion);
        Cell cell = sheet.createRow(0).createCell(0);
        cell.setCellStyle(cellStyleNormal);

        Font normalFont = wb.createFont();
        normalFont.setFontName("Times New Roman");
        normalFont.setFontHeightInPoints((short) 13);

        Font underlineFont = wb.createFont();
        underlineFont.setUnderline(HSSFFont.U_SINGLE);
        underlineFont.setBold(true);
        underlineFont.setFontHeightInPoints((short) 13);
        underlineFont.setFontName("Times New Roman");

        Font italicFont = wb.createFont();
        italicFont.setItalic(true);
        italicFont.setFontHeightInPoints((short) 13);
        italicFont.setFontName("Times New Roman");

        Font boldFont = wb.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 13);
        boldFont.setFontName("Times New Roman");
        cell.setCellStyle(cellStyleNormal);
        RichTextString richString = new HSSFRichTextString("TỔNG CÔNG TY ĐẦU TƯ PHÁT TRIỂN \n ĐƯỜNG CAO TỐC VIỆT NAM \n CÔNG TY VẬN HÀNH VÀ BẢO TRÌ \n ĐƯỜNG CAO TỐC VIỆT NAM");
        richString.applyFont(0, 55, normalFont);
        richString.applyFont(55, 90, boldFont);
        richString.applyFont(90, 108, underlineFont);
        richString.applyFont(108, 110, boldFont);
        cell.setCellValue(richString);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyleNormal);
        cell = row.createCell(2);
        cell.setCellStyle(cellStyleNormal);
        cell = row.createCell(3);
        cell.setCellStyle(cellStyleNormal);

        cell = row.createCell(4);
        cell.setCellStyle(styleForHeaderRight);
        Date now = new Date();
        String time = "";
        String day = String.valueOf(now.getDate());
        String month = String.valueOf(now.getMonth() + 1);
        String year = String.valueOf(now.getYear());
        if (day.length() > 1) {
            time += " ngày " + day;
        } else {
            time += " ngày 0" + day;
        }
        if (month.length() > 1) {
            time += " tháng " + month;
        } else {
            time += " tháng 0" + month;
        }
        RichTextString richString2 = new HSSFRichTextString(" CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM \n  Độc lập - Tự do - Hạnh phúc \n \n                 Hà Nam, " + time + " năm " + (now.getYear() + 1900) + "");
        richString2.applyFont(0, 40, boldFont);
        richString2.applyFont(40, 65, underlineFont);
        richString2.applyFont(65, 66, boldFont);
        richString2.applyFont(67, 121, italicFont);
        cell.setCellValue(richString2);

        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 5, 4, 11); // row 0 col A and P
        sheet.addMergedRegion(mergedRegion);
        for (int i = 5; i <= 11; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNormal);
        }

        rowIndex = 6;
        Row row2 = sheet.createRow(6);
        Cell cell2 = row2.createCell(0);
        cell2.setCellStyle(cellStyleNormal);
        cell2.setCellValue("BÁO CÁO SỰ CỐ GIAO THÔNG THÁNG " + date.toUpperCase());
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 0, 11);
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1; i <= 11; i++) {
            cell = row2.createCell(i);
            cell.setCellStyle(cellStyleNormal);
        }

        String[] text = textHeader.split("\n");
        String newText = "";
        if (text.length == 1) {
            newText = textHeader;
        } else if (text.length > 1) {
            for (int i = 0; i < text.length; i++) {
                if (i == 0) {
                    newText += " " + text[0] + "\n";
                }
                if (i == 1) {
                    newText += "                   " + text[i] + "\n";
                }
                if (text.length == 3 && i == 2) {
                    newText += "                   " + text[i];
                }
            }
        }

        rowIndex = 7;
        int indexCol = 0;
        row = sheet.createRow(7);
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyleTitle);
        cell.setCellValue("Kính gửi : " + newText);
        mergedRegion = new CellRangeAddress(7, 10, 0, 11); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);

        rowIndex = 11;
        indexCol = 0;
        row = sheet.createRow(11);
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 2, indexCol, indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Sự cố giao thông");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, indexCol, indexCol + 2); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Nguyên nhân");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, indexCol, indexCol + 2); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Thiệt hại");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, indexCol, indexCol + 3); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi chú");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 2, indexCol, indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cellStyle = createStyleForHeader(sheet);
        rowIndex++;


        row = sheet.createRow(12);
        cell = row.createCell(0);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Loại sự cố");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 1, 1, 1); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Lý trình");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 1, 2, 2); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Thời gian");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 1, 3, 3); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(4);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Do đường");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 1, 4, 4); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(5);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Do người");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 1, 5, 5); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(6);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Do phương tiện");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 1, 6, 6); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(7);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Về người");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 7, 8); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(8);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(9);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Về tài sản (triệu đồng)");
        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 9, 10); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;
        cell = row.createCell(10);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(11);
        cell.setCellStyle(cellStyle);


        cellStyle = createStyleForHeader(sheet);
        rowIndex++;
        row = sheet.createRow(rowIndex);

        cell = row.createCell(0);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(4);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(5);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(6);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(7);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Chết");

        cell = row.createCell(8);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Bị thương");

        cell = row.createCell(9);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Cầu đường");

        cell = row.createCell(10);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Phương tiện");

        cell = row.createCell(11);
        cell.setCellStyle(cellStyle);

    }

    public static void writeFooter(Sheet sheet, int rowIndex, Workbook wb) {
        CellStyle cellStyle2 = createStyleForFooter(sheet);
        CellStyle cellStyle3 = createStyleForFooterLeft(sheet);
        CellStyle cellStyleNoBorder = createStyleForHeaderNoBorder(sheet);
        Row row = sheet.createRow(rowIndex);
        Cell cell = sheet.createRow(rowIndex).createCell(0);

        Font normalFont = wb.createFont();
        normalFont.setFontName("Times New Roman");
        normalFont.setFontHeightInPoints((short) 11);

        Font boldFont = wb.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 11);
        boldFont.setFontName("Times New Roman");

        CellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        row.setHeight((short)-1);
        cell.setCellStyle(cellStyle3);
        RichTextString richString = new HSSFRichTextString("\n Nơi nhận:\n" +
                "- Như trên;\n" +
                "- Lưu VT.");
        richString.applyFont(0, 10, boldFont);
        richString.applyFont(10, 33, normalFont);
        cell.setCellValue(richString);
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 10 , 0, 5); // row 0 col A and P
        sheet.addMergedRegion(mergedRegion);
        cell = row.createCell(1);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(2);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(3);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(4);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(5);
        cell.setCellStyle(cellStyleNoBorder);

        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 12, 6, 11); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);


        cell = row.createCell(6);
        cell.setCellValue("Q.GIÁM ĐỐC \n \n \n \n \n \n \n Ngô Văn Lợi");
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(7);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(8);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(9);
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(10);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(11);
        cell.setCellStyle(cellStyleNoBorder);


    }

    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex, true);

        }
    }

    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 13); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
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

    private static CellStyle createStyleForHeaderNoboder(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 13); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }


    private static CellStyle createStyleForHeaderNoBorder(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 13); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    public static int writeExcelBridge(List<EventInfo> eventInfoList, int rowIndex, Sheet sheet) throws IOException {
        int index = 1;
        for (EventInfo eventInfo : eventInfoList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(eventInfo, row, index, cellStyle);
            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex - 1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);


        return rowIndex;
    }

    private static void writeBook(EventInfo eventInfo, Row row, int index, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i <= 11; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i) {
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    cell.setCellValue(detectEventType(eventInfo.getEventType()));
                    break;
                case 2:
                    cell.setCellValue(eventInfo.getSite().getSiteName());
                    break;
                case 3:
                    cell.setCellValue(dateFormat.format(eventInfo.getEventDate()));
                    break;
                case 4:
                    cell.setCellValue(eventInfo.getReasonRoad());
                    break;
                case 5:
                    cell.setCellValue(eventInfo.getReasonPerson());
                    break;
                case 6:
                    cell.setCellValue(eventInfo.getReasonVehicle());
                    break;
                case 7:
                    cell.setCellValue(eventInfo.getNumberDead() != null ? String.valueOf(eventInfo.getNumberDead()) : "");
                    break;
                case 8:
                    cell.setCellValue(eventInfo.getNumberHurt() != null ? String.valueOf(eventInfo.getNumberHurt()) : "");
                    break;
                case 9:
                    cell.setCellValue(eventInfo.getFortuneRoad() != null ? String.valueOf(eventInfo.getFortuneRoad()) : "");
                    break;
                case 10:
                    cell.setCellValue(eventInfo.getFortuneVehicle() != null ? String.valueOf(eventInfo.getFortuneVehicle()) : "");
                    break;
                case 11:
                    cell.setCellValue(eventInfo.getNote());
                    break;
            }
        }
    }

    private static String detectEventType(String code) {
        if (code.equalsIgnoreCase("ACCIDENT")) {
            return "Tai nạn";
        } else if (code.equalsIgnoreCase("BROKENVEHICLE")) {
            return "Xe hỏng";
        } else {
            return "Sự cố, va chạm giao thông";
        }
    }

    private static CellStyle createStyleRow(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
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
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setItalic(true);
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

    private static CellStyle createStyleForHeaderRight(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 12); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    private static CellStyle createStyleForFooter(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 13); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }

    private static CellStyle createStyleForFooterLeft(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 13); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    private static CellStyle createTitle(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    private static CellStyle createTitleTop(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    public static int writeExcelHistory(List<HistoryDisplayScript> historyDisplayScripts, int rowIndex, Sheet sheet) throws IOException {
        int index = 0;
        for (HistoryDisplayScript historyDisplayScript : historyDisplayScripts) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBookHistory(historyDisplayScript, row, index, cellStyle,sheet);
            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex - 1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);
        return rowIndex;
    }

    public static void writeHeaderHistory(List<HistoryDisplayScript> historyDisplayScripts,Sheet sheet, int rowIndex, Workbook wb) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        Integer indexCol = 0;
        Row row = sheet.createRow(rowIndex);
        Cell cell;
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Từ ngày");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Đến ngày");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Nội dung");
        indexCol++;


        for (int i = 0; i < historyDisplayScripts.size(); i++) {
            byte[] decodedBytes = Base64.getDecoder().decode(historyDisplayScripts.get(i).getData());
            int inputImagePictureID1 = wb.addPicture(decodedBytes, Workbook.PICTURE_TYPE_PNG);
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor image = new XSSFClientAnchor();
            drawing.createPicture(image, inputImagePictureID1);
            image.setCol1(2);
            image.setCol2(3);
            image.setRow1(i+1);
            image.setRow2(i+2);
        }
    }

    private static void writeBookHistory(HistoryDisplayScript historyDisplayScript, Row row, int index, CellStyle cellStyle,Sheet sheet) {
        Workbook workbook = null;
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        for (int i = 0; i <= 1; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String startTime = "";
            String endTime = "";
            if(historyDisplayScript.getStartTime() != null){
                startTime = format.format(historyDisplayScript.getStartTime());
            }
            if(historyDisplayScript.getEndTime() != null){
                endTime = format.format(historyDisplayScript.getEndTime());
            }

            if(historyDisplayScript.getData() != null){
                switch (i) {
                    case 0:
                        cell.setCellValue(startTime);
                        break;
                    case 1:
                        cell.setCellValue(endTime);
                        break;
                }
            }
        }
    }

    public static int writeExcelActionStatus(List<ReportOnlineStatusDTO> statusDTOS, int rowIndex, Sheet sheet) throws IOException {
        int index = 1;
        for (ReportOnlineStatusDTO statusDTO : statusDTOS) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeActionStatus(statusDTO, row, cellStyle, index);
            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex - 1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);
        return rowIndex;
    }

    public static void writeHeaderActionStatus(Sheet sheet, int rowIndex) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        Integer indexCol = 0;
        Row row = sheet.createRow(rowIndex);
        Cell cell;
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Công việc");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Sự kiện");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Bắt đầu");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Kết thúc");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Từ");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Đến");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Đội xử lý");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi chú");
        indexCol++;
    }

    private static void writeActionStatus(ReportOnlineStatusDTO statusDTO, Row row,CellStyle cellStyle,Integer rowIndex) {
        Workbook workbook = null;
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        for (int i = 0; i <= 8; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);

                switch (i) {
                    case 0:
                        cell.setCellValue(rowIndex);
                        break;
                    case 1:
                        cell.setCellValue(detectJobType(statusDTO.getJobType()));
                        break;
                    case 2:
                        cell.setCellValue(statusDTO.getEventName());
                        break;
                    case 3:
                        cell.setCellValue(statusDTO.getStartTime());
                        break;
                    case 4:
                        cell.setCellValue(statusDTO.getEndTime());
                        break;
                    case 5:
                        cell.setCellValue(statusDTO.getStartSite());
                        break;
                    case 6:
                        cell.setCellValue(statusDTO.getEndSite());
                        break;
                    case 7:
                        cell.setCellValue(statusDTO.getGroupName());
                        break;
                    case 8:
                        cell.setCellValue(statusDTO.getDescription().replace("<p>","").replace("</p>", "") + "\n" + statusDTO.getProcessResult().replace("<p>","").replace("</p>", ""));
                        break;
                }
        }
    }

    private static String detectJobType(String jobType){
        return JobType.parse(jobType).description();
    }
}
