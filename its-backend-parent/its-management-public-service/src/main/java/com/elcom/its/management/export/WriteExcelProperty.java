package com.elcom.its.management.export;

import com.elcom.its.management.dto.AccidentReport;
import com.elcom.its.management.dto.Category;
import com.elcom.its.management.dto.EventInfo;
import com.groupdocs.conversion.internal.a.a.C;
import com.groupdocs.conversion.internal.a.a.Ce;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WriteExcelProperty {
    private static CellStyle cellStyleFormatNumber = null;

    public static Workbook getWorkbook(String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new HSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    public static Workbook getWorkbookXSS(String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new XSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

//    public static void writeHeader(Sheet sheet, int rowIndex, String textHeader, String date, Workbook wb) {
//        CellStyle cellStyle = createStyleForHeader(sheet);
//        CellStyle cellStyleNoBorder = createStyleForHeaderNoBorder(sheet);
//        CellStyle cellStyleTitle = createTitleKG(sheet);
//        CellStyle styleForHeaderRight = createStyleForHeaderRight(sheet);
//        CellStyle dateStyle = createStyleForHeaderNoBorderNoBold(sheet);
//        CellStyle cellStyleNormal = createTitle(sheet);
//        Row row = sheet.createRow(0);
//        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 8, 0, 3); // row 0 col A and P
//        sheet.addMergedRegion(mergedRegion);
//        Cell cell = sheet.createRow(0).createCell(0);
//        cell.setCellStyle(cellStyle);
//
//
//        Font normalFont = wb.createFont();
//        normalFont.setFontName("Times New Roman");
//        normalFont.setFontHeightInPoints((short) 13);
//
//        Font underlineFont = wb.createFont();
//        underlineFont.setUnderline(HSSFFont.U_SINGLE);
//        underlineFont.setBold(true);
//        underlineFont.setFontHeightInPoints((short) 13);
//        underlineFont.setFontName("Times New Roman");
//
//        Font boldFont = wb.createFont();
//        boldFont.setBold(true);
//        boldFont.setFontHeightInPoints((short) 13);
//        boldFont.setFontName("Times New Roman");
//        cell.setCellStyle(cellStyleNoBorder);
//        RichTextString richString = new HSSFRichTextString("C??NG TY CP V???N H??NH V?? B???O TR?? \n ???????NG CAO T???C VI???T NAM \n TRUNG T??M ??I???U H??NH ???????NG \n CAO T???C C???U GI??? - NINH B??NH \n S???:         / VEC O&M.CGNB-??VH ");
//        richString.applyFont(0, 55, normalFont);
//        richString.applyFont(56, 88, boldFont);
//        richString.applyFont(88, 112, underlineFont);
//        richString.applyFont(112, 115, boldFont);
//        richString.applyFont(116, 146, normalFont);
//        cell.setCellValue(richString);
//        cell = row.createCell(1);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(2);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(3);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(4);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(5);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(6);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(7);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(8);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(9);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(10);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(11);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(12);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(13);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(14);
//        cell.setCellStyle(cellStyleNoBorder);
//        cell = row.createCell(15);
//        cell.setCellStyle(cellStyleNoBorder);
//
//        cell = row.createCell(4);
//        cell.setCellStyle(styleForHeaderRight);
//        RichTextString richString2 = new HSSFRichTextString(" C???NG H??A X?? H???I CH??? NGH??A VI???T NAM \n  ?????c l???p - T??? do - H???nh ph??c");
//        richString2.applyFont(0, 40, boldFont);
//        richString2.applyFont(40, 65, underlineFont);
//        richString2.applyFont(65, 66, boldFont);
//        cell.setCellValue(richString2);
//
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 8, 4, 15); // row 0 col A and P
//        sheet.addMergedRegion(mergedRegion);
//        for (int i = 5; i <= 15; i++) {
//            cell = row.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//
//        rowIndex = 9;
//        Row row2 = sheet.createRow(9);
//        Cell cell2 = row2.createCell(0);
//        cell2.setCellStyle(cellStyleNoBorder);
//        cell2.setCellValue("B??O C??O ?????NH K??? TAI N???N GIAO TH??NG");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 0, 15);
//        sheet.addMergedRegion(mergedRegion);
//        for (int i = 1; i <= 15; i++) {
//            cell = row2.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//        // row 0 col A and P
//
//        rowIndex = 10;
//        Row row3 = sheet.createRow(10);
//        Cell cell3 = row3.createCell(0);
//        cell3.setCellStyle(dateStyle);
//        cell3.setCellValue(date);
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 0, 15);
//        sheet.addMergedRegion(mergedRegion);
//        for (int i = 1; i <= 15; i++) {
//            cell = row3.createCell(i);
//            cell.setCellStyle(dateStyle);
//        }
//
//        String[] text = textHeader.split("\n");
//        String newText = "";
//        if(text.length == 1){
//            newText = textHeader;
//        }
//        else if(text.length>1){
//            for (int i = 0; i < text.length; i++) {
//                if (i == 0) {
//                    newText += " " + text[0] + "\n";
//                }
//                if (i == 1) {
//                    newText += "                            " + text[i] + "\n";
//                }
//                if(text.length==3 && i == 2){
//                    newText += "      " + text[i];
//                }
//            }
//        }
//
//        rowIndex = 11;
//        Row row4 = sheet.createRow(11);
//        Cell cell4 = row4.createCell(0);
//        cell4.setCellStyle(cellStyleNormal);
//        cell4.setCellValue("K??nh g???i : " + newText);
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 3, 0, 15); // row 0 col A and P
//        sheet.addMergedRegion(mergedRegion);
//        for (int i = 1; i <= 15; i++) {
//            cell = row4.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//
//        Row row15 = sheet.createRow(12);
//        Cell cell15 = row15.createCell(15);
//        cell15.setCellStyle(cellStyleNoBorder);
//
//        Row row5 = sheet.createRow(12);
//        Cell cell5 = row5.createCell(0);
//        cell5.setCellStyle(cellStyleNormal);
//        for (int i = 1; i <= 15; i++) {
//            cell = row5.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//        Row row6 = sheet.createRow(13);
//        Cell cell6 = row6.createCell(0);
//        cell6.setCellStyle(cellStyleNoBorder);
//        for (int i = 1; i <= 15; i++) {
//            cell = row6.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//        Row row7 = sheet.createRow(14);
//        Cell cell7 = row7.createCell(0);
//        cell7.setCellStyle(cellStyleNormal);
//        for (int i = 1; i <= 15; i++) {
//            cell = row7.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//
//
//
//        rowIndex = 15;
//
//        int indexCol = 0;
//        row = sheet.createRow(rowIndex);
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("STT");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 4, indexCol, indexCol); // row 0 col A and H
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;
//
//
//
//        Row row8 = sheet.createRow(16);
//        Cell cell8 = row8.createCell(0);
//        cell8.setCellStyle(cellStyle);
//        for (int i = 1; i <= 15; i++) {
//            cell = row8.createCell(i);
//            cell.setCellStyle(cellStyle);
//        }
//        Row row9 = sheet.createRow(17);
//        Cell cell9 = row9.createCell(0);
//        cell9.setCellStyle(cellStyle);
//        for (int i = 1; i <= 15; i++) {
//            cell = row9.createCell(i);
//            cell.setCellStyle(cellStyle);
//        }
//        Row row10 = sheet.createRow(18);
//        Cell cell10 = row10.createCell(0);
//        cell10.setCellStyle(cellStyle);
//        for (int i = 1; i <= 15; i++) {
//            cell = row10.createCell(i);
//            cell.setCellStyle(cellStyle);
//        }
//
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("T??n ???????ng");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 4, indexCol, indexCol); // row 0 col A and H
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;
//
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("S??? v??? tai n???n");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 3, indexCol, indexCol + 4); // row 0 col A and H
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//
//
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Nguy??n nh??n");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 3, indexCol, indexCol + 3); // row 0 col A and H
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Thi???t h???i v??? \n ng?????i");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex +3, indexCol, indexCol + 1); // row 0 col A and H
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Thi???t h???i v??? t??i \n s???n (tri???u \n ?????ng)");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex +3, indexCol, indexCol + 1); // row 0 col A and H
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Ghi ch??");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 4, indexCol, indexCol); // row 0 col A and H
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;
//
//
//        cellStyle = createStyleForHeader(sheet);
//        rowIndex = 19;
//        row = sheet.createRow(rowIndex);
//        cell = row.createCell(0);
//        cell.setCellStyle(cellStyle);
//
//        cell = row.createCell(1);
//        cell.setCellStyle(cellStyle);
//
//        cell = row.createCell(2);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Va \n ch???m");
//
//        cell = row.createCell(3);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("??t \n nghi??m \n tr???ng");
//        cell = row.createCell(4);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Nghi??m \n tr???ng");
//        cell = row.createCell(5);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("R???t \n nghi??m \n tr???ng");
//        cell = row.createCell(6);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("?????c \n bi???t \n nghi??m \n tr???ng");
//        cell = row.createCell(7);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Do \n ng?????i \n l??i");
//        cell = row.createCell(8);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Do \n ph????ng \n ti???n");
//        cell = row.createCell(9);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Do \n ???????ng");
//        cell = row.createCell(10);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Nguy??n \n nh??n \n kh??c");
//        cell = row.createCell(11);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Ch???t");
//        cell = row.createCell(12);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("B??? \n th????ng");
//        cell = row.createCell(13);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("C???u \n ???????ng");
//        cell = row.createCell(14);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Ph????ng \n ti???n");
//        cell = row.createCell(15);
//        cell.setCellStyle(cellStyle);
//        autosizeColumn(sheet, 15);
//
//    }

    public static void writeHeaderBCDKTNGT(Sheet sheet, int rowIndex, String textHeader, String date, Workbook wb) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        CellStyle cellStyleNoBorder = createStyleForHeaderNoBorder(sheet);
        CellStyle cellStyleColor = createStyleForHeaderColor(sheet);
        CellStyle cellStyleTitle = createTitleKG(sheet);
        CellStyle cellBoldTitle = createBoldStyle(sheet);
        CellStyle styleForHeaderRight = createStyleForHeaderRight(sheet);
        CellStyle dateStyle = createTitleNoBorder(sheet);
        CellStyle cellStyleNormal = createTitle(sheet);
        CellStyle cellBoldNoTitle = createBoldNoTitle(sheet);
        CellStyle cellBoldRight = createBoldRight(sheet);
        CellStyle cellBoldNoBorder = createBoldNoBorder(sheet);
        Row row = sheet.createRow(rowIndex++);
        Cell cell1 = row.createCell(0);
        sheet.setDefaultColumnStyle(1,cellStyleNormal);
        row = sheet.createRow(rowIndex++);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyleNormal);
        cell.setCellValue("C??NG TY CP V???N H??NH V?? B???O TR?? ???????NG CAO T???C VI???T NAM");
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 7); //A-H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=7;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNoBorder);
        }
        cell = row.createCell(8);
        cell.setCellStyle(cellStyleNormal);
        cell.setCellValue("C???NG H??A X?? H???I CH??? NGH??A VI???T NAM");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 8, 15); //I-P
        sheet.addMergedRegion(mergedRegion);
        for (int i = 9;i<=15;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNoBorder);
        }

        row = sheet.createRow(rowIndex++);

        Font normalFont = wb.createFont();
        normalFont.setFontName("Times New Roman");
        normalFont.setFontHeightInPoints((short) 13);

        Font underlineFont = wb.createFont();
        underlineFont.setUnderline(HSSFFont.U_SINGLE);
        underlineFont.setBold(true);
        underlineFont.setFontHeightInPoints((short) 13);
        underlineFont.setFontName("Times New Roman");

        Font boldFont = wb.createFont();
        boldFont.setBold(true);
        boldFont.setFontHeightInPoints((short) 13);
        boldFont.setFontName("Times New Roman");

        cell = row.createCell(0);
        cell.setCellStyle(cellStyleNormal);
        RichTextString richString = new HSSFRichTextString("TRUNG T??M ??I???U H??NH ???????NG CAO T???C C???U GI??? - NINH B??NH");
        richString.applyFont(0, 8, boldFont);
        richString.applyFont(8, 28, underlineFont);
        richString.applyFont(28, 53, boldFont);
        cell.setCellValue(richString);

        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 7); //A-H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=7;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNoBorder);
        }
        cell = row.createCell(8);
        cell.setCellStyle(cellStyleNormal);
        richString = new HSSFRichTextString("?????c l???p - T??? do - H???nh ph??c");
        richString.applyFont(0, 1, boldFont);
        richString.applyFont(1, 26, underlineFont);
        richString.applyFont(26, 27, boldFont);
        cell.setCellValue(richString);

        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 8, 15); //A-H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=7;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNoBorder);
        }
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellStyle(cellStyleNormal);
        cell.setCellValue("S???:           /BC - VECO&M CGNB.??VH");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 7);
        sheet.addMergedRegion(mergedRegion);
//        for (int i = 1; i <= 7; i++) {
//            cell = row.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
        row = sheet.createRow(rowIndex++);

        Row row2 = sheet.createRow(rowIndex++);
        Cell cell2 = row2.createCell(0);
        cell2.setCellStyle(cellStyleColor);
        cell2.setCellValue("B??O C??O ?????NH K??? TAI N???N GIAO TH??NG");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 15);
        sheet.addMergedRegion(mergedRegion);
//        for (int i = 1; i <= 15; i++) {
//            cell = row2.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
        // row 0 col A and P
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellStyle(cellStyleNormal);
        cell.setCellValue(date);
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 15);
        sheet.addMergedRegion(mergedRegion);

        row = sheet.createRow(rowIndex++);

        textHeader= textHeader.trim();
        int count=1;
        String check= textHeader;
        while (check.indexOf("\n")>=0){
            count++;
            check=check.substring(check.indexOf("\n")+1);
        }
//        cell.setCellValue("K??nh g???i");
        cell = row.createCell(0);
        cell.setCellStyle(cellBoldRight);
        cell.setCellValue("K??nh g???i:");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-2+count, 0, 6);
        sheet.addMergedRegion(mergedRegion);

        cell = row.createCell(7);
        cell.setCellStyle(cellBoldNoTitle);
        cell.setCellValue(textHeader);
        textHeader="                      "+textHeader;
        textHeader=textHeader.replace("\n","\n                        ");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-2+count, 7, 15);
        sheet.addMergedRegion(mergedRegion);
        cell = row.createCell(16);
        cell.setCellStyle(cellStyleNormal);
        if(count>1){
            for (int i=1;i<count;i++){
                row = sheet.createRow(rowIndex++);
                cell = row.createCell(0);
                cell.setCellStyle(cellBoldRight);
                cell = row.createCell(7);
                cell.setCellStyle(cellBoldNoTitle);
                cell = row.createCell(16);
                cell.setCellStyle(cellStyleNormal);
            }
        }
//            cell = row.createCell(0);
//            cell.setCellStyle(dateStyle);
//            cell.setCellValue("K??nh g???i");
//            mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1+count, 0, 6);
//            sheet.addMergedRegion(mergedRegion);
//
//            cell = row.createCell(7);
//            cell.setCellStyle(dateStyle);
//            cell.setCellValue(textHeader);
//            mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1+count, 7, 15);
//            sheet.addMergedRegion(mergedRegion);
////            int countCell=1;
//
////            while (textHeader.indexOf("\n")>=0){
////                String text = textHeader.substring(0, textHeader.indexOf("\n"));
////                if(countCell==1){
////                    cell = row.createCell(7);
////                    cell.setCellStyle(cellBoldNoTitle);
////                    cell.setCellValue(text);
////                    mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 7, 15);
////                    sheet.addMergedRegion(mergedRegion);
////                }else {
////                    row=sheet.createRow(rowIndex++);
//////                    cell = row.createCell(0);
//////                    cell.setCellStyle(cellBoldNoBorder);
//////                    cell.setCellValue("");
//////                    mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 6);
//////                    sheet.addMergedRegion(mergedRegion);
//////                    for (int i=1;i<=6;i++){
//////                        cell = row.createCell(i);
//////                        cell.setCellStyle(cellBoldNoBorder);
//////                    }
////                    cell = row.createCell(7);
////                    cell.setCellStyle(dateStyle);
////                    cell.setCellValue(text);
////                    mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 7, 15);
////                    sheet.addMergedRegion(mergedRegion);
////                    for (int i=8;i<=15;i++){
////                        cell = row.createCell(i);
////                        cell.setCellStyle(cellBoldNoBorder);
////                    }
////                }
////                textHeader=textHeader.substring(textHeader.indexOf("\n")+1);
////                countCell++;
////            }
//            row=sheet.createRow(rowIndex++);
//            cell = row.createCell(0);
//            cell.setCellStyle(cellBoldNoBorder);
//            cell.setCellValue("");
//            mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 6);
//            sheet.addMergedRegion(mergedRegion);
//            for (int i=1;i<=6;i++){
//                cell = row.createCell(i);
//                cell.setCellStyle(cellBoldNoBorder);
//            }
//            cell = row.createCell(7);
//            cell.setCellStyle(cellBoldNoBorder);
//            cell.setCellValue(textHeader);
//            mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 7, 15);
//            sheet.addMergedRegion(mergedRegion);
//            for (int i=8;i<=15;i++){
//                cell = row.createCell(i);
//                cell.setCellStyle(cellBoldNoBorder);
//            }
//        }

//        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 15);
//        sheet.addMergedRegion(mergedRegion);
//        for (int i = 1; i <= 15; i++) {
//            cell = row.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }

        row = sheet.createRow(rowIndex++);
//        cell = row.createCell(0);
//        cell.setCellStyle(cellStyleColor);
//        cell.setCellValue("K??nh g???i:       - C???c Qu???n l?? ???????ng b??? I;");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 0, 15);
//        sheet.addMergedRegion(mergedRegion);

        row = sheet.createRow(rowIndex++);
        cell = row.createCell(0);
        cell.setCellStyle(cellBoldNoTitle);
        cell.setCellValue("???????NG CAO T???C C???U GI??? - NINH B??NH");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, 0, 15);
        sheet.addMergedRegion(mergedRegion);
//        for (int i = 1; i <= 15; i++) {
//            cell = row.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//
//        rowIndex = 10;
//        Row row3 = sheet.createRow(10);
//        Cell cell3 = row3.createCell(0);
//        cell3.setCellStyle(dateStyle);
//        cell3.setCellValue(date);
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 0, 15);
//        sheet.addMergedRegion(mergedRegion);
//        for (int i = 1; i <= 15; i++) {
//            cell = row3.createCell(i);
//            cell.setCellStyle(dateStyle);
//        }
//
//        String[] text = textHeader.split("\n");
//        String newText = "";
//        if(text.length == 1){
//            newText = textHeader;
//        }
//        else if(text.length>1){
//            for (int i = 0; i < text.length; i++) {
//                if (i == 0) {
//                    newText += " " + text[0] + "\n";
//                }
//                if (i == 1) {
//                    newText += "                            " + text[i] + "\n";
//                }
//                if(text.length==3 && i == 2){
//                    newText += "      " + text[i];
//                }
//            }
//        }
//
//        rowIndex = 11;
//        Row row4 = sheet.createRow(11);
//        Cell cell4 = row4.createCell(0);
//        cell4.setCellStyle(cellStyleNormal);
//        cell4.setCellValue("K??nh g???i : " + newText);
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 3, 0, 15); // row 0 col A and P
//        sheet.addMergedRegion(mergedRegion);
//        for (int i = 1; i <= 15; i++) {
//            cell = row4.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//
//        Row row15 = sheet.createRow(12);
//        Cell cell15 = row15.createCell(15);
//        cell15.setCellStyle(cellStyleNoBorder);
//
//        Row row5 = sheet.createRow(12);
//        Cell cell5 = row5.createCell(0);
//        cell5.setCellStyle(cellStyleNormal);
//        for (int i = 1; i <= 15; i++) {
//            cell = row5.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//        Row row6 = sheet.createRow(13);
//        Cell cell6 = row6.createCell(0);
//        cell6.setCellStyle(cellStyleNoBorder);
//        for (int i = 1; i <= 15; i++) {
//            cell = row6.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//        Row row7 = sheet.createRow(14);
//        Cell cell7 = row7.createCell(0);
//        cell7.setCellStyle(cellStyleNormal);
//        for (int i = 1; i <= 15; i++) {
//            cell = row7.createCell(i);
//            cell.setCellStyle(cellStyleNoBorder);
//        }
//
//
//

        int indexCol = 0;
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex , indexCol, indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;



//        row = sheet.createRow(rowIndex++);
//        cell = row.createCell(0);
//        cell8.setCellStyle(cellStyle);
//        for (int i = 1; i <= 15; i++) {
//            cell = row8.createCell(i);
//            cell.setCellStyle(cellStyle);
//        }
//        Row row9 = sheet.createRow(17);
//        Cell cell9 = row9.createCell(0);
//        cell9.setCellStyle(cellStyle);
//        for (int i = 1; i <= 15; i++) {
//            cell = row9.createCell(i);
//            cell.setCellStyle(cellStyle);
//        }
//        Row row10 = sheet.createRow(18);
//        Cell cell10 = row10.createCell(0);
//        cell10.setCellStyle(cellStyle);
//        for (int i = 1; i <= 15; i++) {
//            cell = row10.createCell(i);
//            cell.setCellStyle(cellStyle);
//        }

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("L?? tr??nh");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex , indexCol, indexCol);
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T???nh th??nh");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex , indexCol, indexCol);
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("V??? tr??");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex , indexCol, indexCol);
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Th???i gian x???y ra");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex -1, indexCol, indexCol+1); //  col E and F
        sheet.addMergedRegion(mergedRegion);
        indexCol++;
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("H???u qu???");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex-1, indexCol, indexCol + 3); // row 0 col A and H
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
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;


//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Nguy??n nh??n");
//        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex -1, indexCol, indexCol + 3); // row 0 col A and H
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;
//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Thi???t h???i ?????c \n t??nh \n (Tri???u ?????ng)");
        row.setHeight((short) 1000);
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex -1, indexCol, indexCol + 1); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Nguy??n \n nh??n theo \n c??ng an \n x??c ?????nh");
        sheet.autoSizeColumn(12,true);
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex , indexCol, indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Nguy??n \n nh??n theo \n c??ng an \n x??c ?????nh");
//        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex , indexCol, indexCol);
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Th??ng tin ti???p nh???n");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex , indexCol, indexCol);
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);

        cell.setCellValue("Hi???n tr???ng \n ???????ng \n tr?????c khi \n x???y ra tai \n n???n");
        sheet.autoSizeColumn(14,true);
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex , indexCol, indexCol);
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ki???n ngh??? x??? l??");
        mergedRegion = new CellRangeAddress(rowIndex-1, rowIndex , indexCol, indexCol);
        sheet.addMergedRegion(mergedRegion);
        indexCol++;



        indexCol = 0;
        row = sheet.createRow(rowIndex++);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Gi???");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ng??y");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("S??? ng?????i ch???t");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("S??? ng?????i b??? th????ng");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("S??? ph????ng ti???n h?? h???ng");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("K???t c???u thi???t h???i");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("C???u ???????ng");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ph????ng ti???n");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
//        cell = row.createCell(indexCol++);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("C???u ???????ng");
//        cell = row.createCell(indexCol++);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("C???u ???????ng");
//        cell = row.createCell(indexCol++);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("C???u ???????ng");

//        cell = row.createCell(indexCol);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Ghi ch??");
//        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 4, indexCol, indexCol); // row 0 col A and H
//        sheet.addMergedRegion(mergedRegion);
//        indexCol++;
//
//
//
//        cellStyle = createStyleForHeader(sheet);
//        rowIndex = 19;
//        row = sheet.createRow(rowIndex);
//        cell = row.createCell(0);
//        cell.setCellStyle(cellStyle);
//
//        cell = row.createCell(1);
//        cell.setCellStyle(cellStyle);
//
//        cell = row.createCell(2);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Va \n ch???m");
//
//        cell = row.createCell(3);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("??t \n nghi??m \n tr???ng");
//        cell = row.createCell(4);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Nghi??m \n tr???ng");
//        cell = row.createCell(5);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("R???t \n nghi??m \n tr???ng");
//        cell = row.createCell(6);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("?????c \n bi???t \n nghi??m \n tr???ng");
//        cell = row.createCell(7);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Do \n ng?????i \n l??i");
//        cell = row.createCell(8);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Do \n ph????ng \n ti???n");
//        cell = row.createCell(9);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Do \n ???????ng");
//        cell = row.createCell(10);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Nguy??n \n nh??n \n kh??c");
//        cell = row.createCell(11);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Ch???t");
//        cell = row.createCell(12);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("B??? \n th????ng");
//        cell = row.createCell(13);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("C???u \n ???????ng");
//        cell = row.createCell(14);
//        cell.setCellStyle(cellStyle);
//        cell.setCellValue("Ph????ng \n ti???n");
//        cell = row.createCell(15);
//        cell.setCellStyle(cellStyle);
//        autosizeColumn(sheet, 15);

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
        RichTextString richString = new HSSFRichTextString("\n N??i nh???n:\n" +
                "- Nh?? tr??n;\n" +
                "- L??u VT. ");
        richString.applyFont(0, 10, boldFont);
        richString.applyFont(10, 33, normalFont);
        cell.setCellValue(richString);
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 10 , 0, 8); // row 0 col A and P
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
        cell = row.createCell(6);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(7);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(8);
        cell.setCellStyle(cellStyleNoBorder);

        mergedRegion = new CellRangeAddress(rowIndex, rowIndex + 12, 9, 15); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);

        cell = row.createCell(9);
        cell.setCellValue("\n Q.GI??M ?????C \n \n \n \n \n \n \n Ng?? V??n L???i");
        cell.setCellStyle(cellStyle2);
        cell = row.createCell(10);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(11);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(12);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(13);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(14);
        cell.setCellStyle(cellStyleNoBorder);
        cell = row.createCell(15);
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
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
//        cellStyle.setBorderBottom(BorderStyle.NONE);
//        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    private static CellStyle createStyleForHeaderColor(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.RED.getIndex());
        font.setBold(true);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        Font font = sheet.getWorkbook().createFont();
//        font.setFontName("Times New Roman");
//        font.setBold(true);
//        font.setFontHeightInPoints((short) 13); // font size
//        font.setColor(IndexedColors.RED.getIndex()); // text color
//
//        // Create CellStyle
//        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
//        cellStyle.setWrapText(true);
//        cellStyle.setFont(font);
//        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellStyle.setBorderTop(BorderStyle.NONE);
//        cellStyle.setBorderRight(BorderStyle.NONE);
//        cellStyle.setBorderBottom(BorderStyle.NONE);
//        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    private static CellStyle createStyleForHeaderNoBorderNoBold(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color
        font.setItalic(true);

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
    private static CellStyle createStyleForHeaderRight(Sheet sheet) {
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
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.NONE);
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
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    private static CellStyle createStyleForFooterLeft(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 11); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    public static int writeExcelBridge(List<AccidentReport> accidentReportList, int rowIndex, Sheet sheet) throws IOException {
        int index = 1;
        for (AccidentReport accidentReport : accidentReportList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(accidentReport, row, index, cellStyle);
            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex - 1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);


        return rowIndex;
    }

    public static int writeExcelEventInfo(List<EventInfo> accidentReportList, int rowIndex, Sheet sheet, Date start) throws IOException {
        int index = 1;
        int numberDead=0;
        int numberHurt=0;
        int numberStructure=0;
        int numberVehicle =0;
        float fortuneRoad =0;
        float fortuneVehicle =0;
        for (EventInfo eventInfo : accidentReportList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(eventInfo, row, index, cellStyle);
            if(eventInfo.getNumberHurt()!=null) {
                numberHurt += eventInfo.getNumberHurt();
            }
            if(eventInfo.getNumberDead()!=null) {
                numberDead += eventInfo.getNumberDead();
            }
            if(eventInfo.getFortuneRoad()!=null) {
                fortuneRoad += eventInfo.getFortuneRoad();
            }
            if(eventInfo.getFortuneVehicle()!=null) {
                fortuneVehicle += eventInfo.getFortuneVehicle();
            }

            if(eventInfo.getDamageStructure()!=null) {
                numberStructure += eventInfo.getDamageStructure();
            }
            if(eventInfo.getDamageVehicle()!=null){
                numberVehicle+=eventInfo.getDamageVehicle();
            }

            index++;
            rowIndex++;
        }
        sheet.autoSizeColumn(5,true);
//        sheet.autoSizeColumn(6,true);
//        sheet.autoSizeColumn(7,true);
        Row row = sheet.createRow(rowIndex);
        CellStyle cellStyle = createStyleRow(sheet);
        writeBook(accidentReportList.size(),numberDead,numberHurt,numberStructure,numberVehicle,fortuneRoad,fortuneVehicle, row, cellStyle,start);
        index++;
        rowIndex++;

//        int numberOfColumn = sheet.getRow(rowIndex - 1).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);


        return rowIndex;
    }

    public static int writeExcelFoot(List<Category> categories, int rowIndex, Sheet sheet) throws IOException {
        Row row = sheet.createRow(rowIndex);
        CellStyle cellStyle = createStyleRow(sheet);
        CellStyle cellStyleNoBorder = createStyleForHeaderNoBorder(sheet);
        CellStyle cellStyleNormal = createTitle(sheet);
        CellStyle cellStyleItalic= createTitleIn(sheet);
        CellStyle cellBoldTitleFull = createBoldStyle(sheet);
        CellStyle cellBoldTitle= createBoldNoTitle(row.getSheet());

//        CellStyle cellBoldTitle = createBoldStyle(sheet);
        rowIndex++;
        row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyleNormal);
        for (int i = 1;i<=11;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNormal);
        }
        Date date = new Date();
        cell = row.createCell(12);
        cell.setCellStyle(cellStyleItalic);
        String day = "";
        if(date.getDate()<10){
            day = "0"+date.getDate();
        }
        int month = date.getMonth()+1;
        int year = date.getYear()+1900;
        cell.setCellValue("H?? Nam, ng??y " +day+" th??ng " +month+" n??m "+year);
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex, rowIndex, 12, 15); //A-H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 13;i<=15;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNoBorder);
        }
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);

        cell = row.createCell(1);
        cell.setCellStyle(cellBoldTitleFull);
        cell.setCellValue("Ng?????i l???p");
        mergedRegion = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 4);
        row.getSheet().addMergedRegion(mergedRegion);
        for (int i = 2;i<=4;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNoBorder);
        }
        for (int i = 5;i<=11;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNormal);
        }
        cell = row.createCell(12);
        cell.setCellStyle(cellBoldTitleFull);
        cell.setCellValue("GI??M ?????C");
        mergedRegion = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 12, 15);
        row.getSheet().addMergedRegion(mergedRegion);
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(0);
        rowIndex++;
        row = sheet.createRow(rowIndex);
        Category creator = new Category();
        Category sign = new Category();
        for (Category tmp: categories
             ) {
            if(tmp.getCode().equals("CREATOR")){
                creator=tmp;
            }else {
                sign=tmp;
            }
        }
        writeBookCreator(creator,row);
        writeBookSign(sign,row);
        rowIndex++;


//        int numberOfColumn = sheet.getRow(rowIndex - 1).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);


        return rowIndex;
    }

    private static void writeBookCreator(Category category, Row row) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        CellStyle cellStyleNoBorder = createStyleForHeaderNoBorder(row.getSheet());
        CellStyle cellBoldTitle= createBoldStyle(row.getSheet());
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellBoldTitle);
        cell = row.createCell(1);
        cell.setCellStyle(cellBoldTitle);
        cell.setCellValue(category.getName());
        CellRangeAddress mergedRegion = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 1, 4);
        row.getSheet().addMergedRegion(mergedRegion);
        for (int i = 2;i<=4;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyleNoBorder);
        }
        for (int i = 5;i<=11;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellBoldTitle);
        }

    }

    private static void writeBookSign(Category category, Row row) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        CellStyle cellBoldTitle= createBoldStyle(row.getSheet());
//        Cell cell = row.createCell(0);
        Cell cell = row.createCell(12);
        cell.setCellStyle(cellBoldTitle);
        cell.setCellValue(category.getName());
        CellRangeAddress mergedRegion = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 12, 15);
        row.getSheet().addMergedRegion(mergedRegion);

    }

    private static void writeBook(AccidentReport accidentReport, Row row, int index, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        for (int i = 0; i <= 15; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i) {
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    cell.setCellValue(accidentReport.getNameRoad());
                    break;
                case 2:
                    cell.setCellValue(accidentReport.getNumberAccident());
                    break;
                case 3:
                    cell.setCellValue(accidentReport.getLowPriority());
                    break;
                case 4:
                    cell.setCellValue(accidentReport.getMediumPriority());
                    break;
                case 5:
                    cell.setCellValue(accidentReport.getSeriousPriority());
                    break;
                case 6:
                    cell.setCellValue(accidentReport.getMaxSeriousPriority());
                    break;
                case 7:
                    cell.setCellValue(accidentReport.getReasonPerson());
                    break;
                case 8:
                    cell.setCellValue(accidentReport.getReasonVehicle());
                    break;
                case 9:
                    cell.setCellValue(accidentReport.getReasonRoad());
                    break;
                case 10:
                    cell.setCellValue(accidentReport.getReasonOther());
                    break;
                case 11:
                    cell.setCellValue(accidentReport.getNumberDead());
                    break;
                case 12:
                    cell.setCellValue(accidentReport.getNumberHurt());
                    break;
                case 13:
                    cell.setCellValue(accidentReport.getFortuneRoad());
                    break;
                case 14:
                    cell.setCellValue(accidentReport.getFortuneVehicle());
                    break;
                case 15:
                    cell.setCellValue("");
                    break;
            }
        }
    }

    private static void writeBook(EventInfo eventInfo, Row row, int index, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        DateFormat hh = new SimpleDateFormat("HH");
        DateFormat mm = new SimpleDateFormat("mm");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i <= 15; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i) {
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    String position = eventInfo.getSite().getSiteName()+" "+eventInfo.getDirection();
                    if(eventInfo.getEndSite()!=null){
                        position += " - " + eventInfo.getEndSite().getSiteName()+" "+eventInfo.getDirection();
                    }
                    cell.setCellValue(position);
                    break;
                case 2:
                    cell.setCellValue(eventInfo.getSite().getProvinceName());
                    break;
                case 3:
                    if(eventInfo.getLane()!=null && eventInfo.getLane()>0) {
                        cell.setCellValue("L??n "+eventInfo.getLane());
                    }else {
                        cell.setCellValue("");
                    }
                    break;
                case 4:
                    if(eventInfo.getDateReceived()!=null) {
                        cell.setCellValue(hh.format(eventInfo.getDateReceived()) + "h" + mm.format(eventInfo.getDateReceived()));
                    }else {
                        cell.setCellValue(hh.format(eventInfo.getEventDate()) + "h" + mm.format(eventInfo.getEventDate()));
                    }
                    break;
                case 5:
                    if(eventInfo.getDateReceived()!=null) {
                        cell.setCellValue(df.format(eventInfo.getDateReceived()));
                    }else {
                        cell.setCellValue(df.format(eventInfo.getEventDate()));
                    }
                    break;
                case 6:
                    if(eventInfo.getNumberDead()!=null) {
                        cell.setCellValue(eventInfo.getNumberDead());
                    }else {
                        cell.setCellValue("0");
                    }
//                    cell.setCellValue(eventInfo.getNumberDead());
                    break;
                case 7:
                    if(eventInfo.getNumberHurt()!=null) {
                        cell.setCellValue(eventInfo.getNumberHurt());
                    }else {
                        cell.setCellValue("0");
                    }
//                    cell.setCellValue(eventInfo.getNumberHurt());
                    break;
                case 8:
                    if(eventInfo.getDamageVehicle()!=null) {
                        cell.setCellValue(eventInfo.getDamageVehicle());
                    }else {
                        cell.setCellValue("0");
                    }
                    break;
                case 9:
                    if(eventInfo.getDamageStructure()!=null) {
                        cell.setCellValue(eventInfo.getDamageStructure());
                    }else {
                        cell.setCellValue("0");
                    }
                    break;
                case 10:
                    if(eventInfo.getFortuneRoad()!=null) {
                        cell.setCellValue(eventInfo.getFortuneRoad());
                    }else {
                        cell.setCellValue("Ch??a ?????c t??nh ???????c");
                    }
                    break;
                case 11:
                    if(eventInfo.getFortuneVehicle()!=null) {
                        cell.setCellValue(eventInfo.getFortuneVehicle());
                    }else {
                        cell.setCellValue("Ch??a ?????c t??nh ???????c");
                    }
                    break;
                case 12:
                    cell.setCellValue(eventInfo.getReasonPolice());
                    break;
                case 13:
                    cell.setCellValue(eventInfo.getReportMethod());
                    break;
                case 14:
                    cell.setCellValue(eventInfo.getState());
                    break;
                case 15:
                    cell.setCellValue(eventInfo.getProcess());
                    break;
            }
        }
    }

    private static void writeBook(Integer size, Integer numberDead, Integer numberHurt, Integer numberStructure,
                                  Integer numberVehicle,Float fortuneRoad,Float fortuneVehicle, Row row, CellStyle cellStyle,
                                    Date start) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        CellStyle cellBoldTitle= createBoldFullStyle(row.getSheet());
        DateFormat hh = new SimpleDateFormat("HH");
        DateFormat mm = new SimpleDateFormat("mm");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellBoldTitle);
        int month = start.getMonth()+1;
        int year = start.getYear()+1900;
        cell.setCellValue("C???ng th??ng "+month+"/"+year+": C?? " +size+ " v??? TNGT");
        CellRangeAddress mergedRegion = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 5); //A-F
        row.getSheet().addMergedRegion(mergedRegion);
        for (int i = 1;i<=5;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellBoldTitle);
        }
        cell = row.createCell(6);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(numberDead);
        cell = row.createCell(7);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(numberHurt);
        cell = row.createCell(8);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(numberVehicle);
        cell = row.createCell(9);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(numberStructure);
        cell = row.createCell(10);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(fortuneRoad);
        cell = row.createCell(11);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(fortuneVehicle);
        cell = row.createCell(12);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(13);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(14);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(15);
        cell.setCellStyle(cellStyle);


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
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
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
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellStyle.setBorderTop(BorderStyle.NONE);
//        cellStyle.setBorderRight(BorderStyle.NONE);
//        cellStyle.setBorderBottom(BorderStyle.NONE);
//        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    private static CellStyle createTitleIn(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setItalic(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellStyle.setBorderTop(BorderStyle.NONE);
//        cellStyle.setBorderRight(BorderStyle.NONE);
//        cellStyle.setBorderBottom(BorderStyle.NONE);
//        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }
    private static CellStyle createTitleNoBorder(Sheet sheet) {
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
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }
    private static CellStyle createBoldNoTitle(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellStyle.setBorderTop(BorderStyle.NONE);
//        cellStyle.setBorderRight(BorderStyle.NONE);
//        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setLeftBorderColor(IndexedColors.WHITE.getIndex());
        return cellStyle;
    }
    private static CellStyle createBoldNoTitleNoBorder(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellStyle.setBorderTop(BorderStyle.NONE);
//        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }
    private static CellStyle createBoldNoBorder(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.NONE);
        cellStyle.setBorderBottom(BorderStyle.NONE);
        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    private static CellStyle createBoldRight(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
//        cellStyle.setBorderTop(BorderStyle.NONE);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
//        cellStyle.setBorderBottom(BorderStyle.NONE);
//        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }
    private static CellStyle createTitleKG(Sheet sheet) {
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

    private static CellStyle createBoldStyle(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
//        cellStyle.setBorderTop(BorderStyle.NONE);
//        cellStyle.setBorderRight(BorderStyle.NONE);
//        cellStyle.setBorderBottom(BorderStyle.NONE);
//        cellStyle.setBorderLeft(BorderStyle.NONE);
        return cellStyle;
    }

    private static CellStyle createBoldFullStyle(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short) 13);
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }
}
