package com.elcom.its.management.export;

import com.elcom.its.management.dto.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class WriteExcelProperty {
    private static CellStyle cellStyleFormatNumber = null;

//    public static void main(String[] args) throws IOException {
//        final List<Book> books = getBooks();
//        final String excelFilePath = "C:/demo/books.xlsx";
//        writeExcel(books, excelFilePath);
//    }

    public static int writeExcelBridge(List<CommonProperty> commonPropertyList, int rowIndex, Sheet sheet) throws IOException {
        int index =1;
        for (CommonProperty commonProperty : commonPropertyList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            ObjectMapper objectMapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(df);
            String tmp = objectMapper.writeValueAsString(commonProperty.getData());
            JsonNode jsonNode = objectMapper.readTree(tmp);
            Bridge bridge = objectMapper.readValue(tmp, Bridge.class);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(commonProperty,bridge, row,index,cellStyle);
            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex-1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);


        return rowIndex;
    }

    public static int writeExcelDrain(List<CommonProperty> commonPropertyList, int rowIndex, Sheet sheet) throws IOException {
        int index =1;
        for (CommonProperty commonProperty : commonPropertyList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            ObjectMapper objectMapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(df);
            String tmp = objectMapper.writeValueAsString(commonProperty.getData());
            Drain data = objectMapper.readValue(tmp, Drain.class);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(commonProperty,data, row,index,cellStyle);

            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex-1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);


        // Auto resize column witdth


        return rowIndex;
    }

    public static int writeExcelTrench(List<CommonProperty> commonPropertyList, int rowIndex, Sheet sheet) throws IOException {
        int index =1;
        for (CommonProperty commonProperty : commonPropertyList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            ObjectMapper objectMapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(df);
            String tmp = objectMapper.writeValueAsString(commonProperty.getData());
            Trench data = objectMapper.readValue(tmp, Trench.class);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(commonProperty,data, row,index,cellStyle);

            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex-1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);


        return rowIndex;
    }

    public static int writeExcelFence(List<CommonProperty> commonPropertyList, int rowIndex, Sheet sheet) throws IOException {
        int index =1;
        for (CommonProperty commonProperty : commonPropertyList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            ObjectMapper objectMapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(df);
            String tmp = objectMapper.writeValueAsString(commonProperty.getData());
            Fence data = objectMapper.readValue(tmp, Fence.class);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(commonProperty,data, row,index,cellStyle);

            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex-1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);

        // Auto resize column witdth


        return rowIndex;
    }

    public static int writeExcelElectrical(List<CommonProperty> commonPropertyList, int rowIndex, Sheet sheet) throws IOException {
        int index =1;
        for (CommonProperty commonProperty : commonPropertyList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            ObjectMapper objectMapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(df);
            String tmp = objectMapper.writeValueAsString(commonProperty.getData());
            ElectricalCabinets data = objectMapper.readValue(tmp, ElectricalCabinets.class);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(commonProperty,data, row,index,cellStyle);

            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex-1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);


        return rowIndex;
    }

    public static int writeExcelAuxiliaryEquipment(List<CommonProperty> commonPropertyList, int rowIndex, Sheet sheet) throws IOException {
        int index =1;
        for (CommonProperty commonProperty : commonPropertyList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            ObjectMapper objectMapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(df);
            String tmp = objectMapper.writeValueAsString(commonProperty.getData());
            AuxiliaryEquipment data = objectMapper.readValue(tmp, AuxiliaryEquipment.class);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(commonProperty,data, row,index,cellStyle);

            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex-1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);
        return rowIndex;
    }

    public static int writeExcelTrafficSigns(List<CommonProperty> commonPropertyList, int rowIndex, Sheet sheet) throws IOException {
        int index =1;
        for (CommonProperty commonProperty : commonPropertyList) {
            // Create row
            Row row = sheet.createRow(rowIndex);
            ObjectMapper objectMapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            objectMapper.setDateFormat(df);
            String tmp = objectMapper.writeValueAsString(commonProperty.getData());
            TrafficSigns data = objectMapper.readValue(tmp, TrafficSigns.class);
            CellStyle cellStyle = createStyleRow(sheet);
            // Write data on row
            writeBook(commonProperty,data, row,index,cellStyle);

            index++;
            rowIndex++;
        }
        int numberOfColumn = sheet.getRow(rowIndex-1).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);


        return rowIndex;
    }

    private static void writeBook(CommonProperty commonProperty,Bridge bridge, Row row, int index, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        for (int i= 0;i<=20;i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i){
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    cell.setCellValue(bridge.getName());
                    break;
                case 2:
                    cell.setCellValue(commonProperty.getKm());
                    break;
                case 3:
                    cell.setCellValue(commonProperty.getM());
                    break;
                case 4:
                    cell.setCellValue(commonProperty.getPosition());
                    break;
                case 5:
                    cell.setCellValue(bridge.getLength());
                    break;
                case 6:
                    cell.setCellValue(bridge.getWith());
                    break;
                case 7:
                    cell.setCellValue(bridge.getDiagram());
                    break;
                case 8:
                    cell.setCellValue(bridge.getRhythm());
                    break;
                case 9:
                    cell.setCellValue(bridge.getMeters());
                    break;
                case 10:
                    cell.setCellValue(bridge.getPillar());
                    break;
                case 11:
                    cell.setCellValue(bridge.getBearingType());
                    break;
                case 12:
                    cell.setCellValue(bridge.getBearingQuantity());
                    break;
                case 13:
                    cell.setCellValue(bridge.getExpansionJointsType());
                    break;
                case 14:
                    cell.setCellValue(bridge.getExpansionJointsQuantity());
                    break;
                case 15:
                    cell.setCellValue(bridge.getBridgeRailingLength());
                    break;
                case 16:
                    cell.setCellValue(bridge.getAcreageNew());
                    break;
                case 17:
                    cell.setCellValue(bridge.getAcreage());
                    break;
                case 18:
                    cell.setCellValue(bridge.getAcreagePillar());
                    break;
                case 19:
                    cell.setCellValue(bridge.getNote());
                    break;
                case 20:
                    cell.setCellValue(bridge.getStatus().description());
                    break;

            }
        }



        // Create cell formula
        // totalMoney = price * quantity
//        cell = row.createCell(COLUMN_INDEX_TOTAL, CellType.FORMULA);
//        cell.setCellStyle(cellStyleFormatNumber);
//        int currentRow = row.getRowNum() + 1;
//        String columnPrice = CellReference.convertNumToColString(COLUMN_INDEX_PRICE);
//        String columnQuantity = CellReference.convertNumToColString(COLUMN_INDEX_QUANTITY);
//        cell.setCellFormula(columnPrice + currentRow + "*" + columnQuantity + currentRow);
    }

    private static void writeBook(CommonProperty commonProperty, Trench data, Row row, int index, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        for (int i= 0;i<=8;i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i){
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    cell.setCellValue(commonProperty.getKm());
                    break;
                case 2:
                    cell.setCellValue(commonProperty.getM());
                    break;
                case 3:
                    cell.setCellValue(data.getPositionStart());
                    break;
                case 4:
                    cell.setCellValue(data.getPositionEnd());
                    break;
                case 5:
                    cell.setCellValue(data.getDirectionCode());
                    break;
                case 6:
                    cell.setCellValue(data.getLength());
                    break;
                case 7:
                    cell.setCellValue(data.getNote());
                    break;
                case 8:
                    cell.setCellValue(data.getStatus().description());
                    break;
                default:
                    break;

            }
        }



        // Create cell formula
        // totalMoney = price * quantity
//        cell = row.createCell(COLUMN_INDEX_TOTAL, CellType.FORMULA);
//        cell.setCellStyle(cellStyleFormatNumber);
//        int currentRow = row.getRowNum() + 1;
//        String columnPrice = CellReference.convertNumToColString(COLUMN_INDEX_PRICE);
//        String columnQuantity = CellReference.convertNumToColString(COLUMN_INDEX_QUANTITY);
//        cell.setCellFormula(columnPrice + currentRow + "*" + columnQuantity + currentRow);
    }

    private static void writeBook(CommonProperty commonProperty, Drain data, Row row, int index, CellStyle cellStyle) {
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        for (int i = 0; i <= 8; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i) {
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    cell.setCellValue(commonProperty.getName());
                    break;
                case 2:
                    cell.setCellValue(commonProperty.getKm());
                    break;
                case 3:
                    cell.setCellValue(commonProperty.getM());
                    break;
                case 4:
                    cell.setCellValue(commonProperty.getPosition());
                    break;
                case 5:
                    cell.setCellValue(data.getAperture());
                    break;
                case 6:
                    cell.setCellValue(data.getBridgeRailingLength());
                    break;
                case 7:
                    cell.setCellValue(data.getNote());
                    break;
                case 8:
                    cell.setCellValue(data.getStatus().description());
                    break;

            }
        }
    }

    private static void writeBook(CommonProperty commonProperty, Fence data, Row row, int index, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        for (int i= 0;i<=8;i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i){
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    cell.setCellValue(commonProperty.getKm());
                    break;
                case 2:
                    cell.setCellValue(commonProperty.getM());
                    break;
                case 3:
                    cell.setCellValue(data.getPositionStart());
                    break;
                case 4:
                    cell.setCellValue(data.getPositionEnd());
                    break;
                case 5:
                    cell.setCellValue(data.getUnit());
                    break;
                case 6:
                    cell.setCellValue(data.getLength());
                    break;
                case 7:
                    cell.setCellValue(data.getNote());
                    break;
                case 8:
                    cell.setCellValue(data.getStatus().description());
                    break;
                default:
                    break;

            }
        }
    }

    private static void writeBook(CommonProperty commonProperty, ElectricalCabinets data, Row row, int index, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        for (int i= 0;i<=11;i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i){
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    cell.setCellValue(commonProperty.getKm());
                    break;
                case 2:
                    cell.setCellValue(commonProperty.getM());
                    break;
                case 3:
                    cell.setCellValue(data.getSite());
                    break;
                case 4:
                    cell.setCellValue(data.getSubstation());
                    break;
                case 5:
                    cell.setCellValue(data.getLowVoltageStation());
                    break;
                case 6:
                    cell.setCellValue(data.getAts());
                    break;
                case 7:
                    cell.setCellValue(data.getDistributionCabinet());
                    break;
                case 8:
                    cell.setCellValue(data.getChargingStation());
                    break;
                case 9:
                    cell.setCellValue(data.getLightingControl());
                    break;
                case 10:
                    cell.setCellValue(data.getNote());
                    break;
                case 11:
                    cell.setCellValue(data.getStatus().description());
                    break;
                default:
                    break;

            }
        }
    }

    private static void writeBook(CommonProperty commonProperty, AuxiliaryEquipment data, Row row, int index, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        for (int i= 0;i<=9;i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i){
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    cell.setCellValue(commonProperty.getKm());
                    break;
                case 2:
                    cell.setCellValue(commonProperty.getM());
                    break;
                case 3:
                    cell.setCellValue(data.getType().description());
                    break;
                case 4:
                    cell.setCellValue(data.getSite());
                    break;
                case 5:
                    cell.setCellValue(data.getUnit());
                    break;
                case 6:
                    cell.setCellValue(data.getQuantity());
                    break;
                case 7:
                    cell.setCellValue(data.getNameCamera());
                    break;
                case 8:
                    cell.setCellValue(data.getNote());
                    break;
                case 9:
                    cell.setCellValue(data.getStatus().description());
                    break;
                default:
                    break;

            }
        }
    }

    private static void writeBook(CommonProperty commonProperty, TrafficSigns data, Row row, int index, CellStyle cellStyle) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short)BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            //Create CellStyle
            Workbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        for (int i= 0;i<=11;i++){
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            switch (i){
                case 0:
                    cell.setCellValue(index);
                    break;
                case 1:
                    cell.setCellValue(commonProperty.getKm());
                    break;
                case 2:
                    cell.setCellValue(commonProperty.getM());
                    break;
                case 3:
                    cell.setCellValue(data.getPosition());
                    break;
                case 4:
                    cell.setCellValue(data.getDirectionCode());
                    break;
                case 5:
                    cell.setCellValue(data.getContent());
                    break;
                case 6:
                    cell.setCellValue(data.getSize());
                    break;
                case 7:
                    cell.setCellValue(data.getAcreage());
                    break;
                case 8:
                    cell.setCellValue(data.getPillar());
                    break;
                case 9:
                    cell.setCellValue(data.getAcreagePillar());
                    break;
                case 10:
                    cell.setCellValue(data.getNote());
                    break;
                case 11:
                    cell.setCellValue(data.getStatus().description());
                    break;
                default:
                    break;

            }
        }
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
    public static void writeHeaderBridge(Sheet sheet, int rowIndex) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Danh s??ch c???u qu???n l??");
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,0,20); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=20;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        row = sheet.createRow(rowIndex+1);
        for (int i = 0;i<=20;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        rowIndex = rowIndex+3;

        // create CellStyle


        // Create row
        int indexCol=0;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

//        sheet.autoSizeColumn(0,true);

        // Create cells
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T??n c???u");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("KM");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("M");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("L?? tr??nh");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Chi???u d??i to??n c???u (m)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("B??? r???ng c???u (m)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("S?? ????? \n (nh???p / m??t)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("K???t c???u");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex,indexCol,indexCol+1); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tr??? c???u \n (c??i)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("G???i c???u \n (c??i)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol+1); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Khe co gi??n \n (m)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol+1); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Chi???u d??i lan can \n (m)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Di???n t??ch m???i c???u \n (m2)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol+1); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Di???n t??ch tr??? c???u \n (m2)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi ch??");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tr???ng th??i");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+2,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;


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
        cell = row.createCell(8);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(11);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(12);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(13);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(14);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(15);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(16);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(17);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(18);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(19);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(20);
        cell.setCellStyle(cellStyle);


        cell = row.createCell(8);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("BTDUL");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex,8,9); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);


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
        cell = row.createCell(8);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Nh???p");
        cell = row.createCell(9);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("M??t");

        cell = row.createCell(10);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(11);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Lo???i");
        cell = row.createCell(12);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("S??? l????ng");
        cell = row.createCell(13);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Lo???i");
        rowIndex++;
        cell = row.createCell(14);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("S??? l?????ng");

        cell = row.createCell(15);
        cell.setCellStyle(cellStyle);

        cell = row.createCell(16);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("M???i");
        cell = row.createCell(17);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("C??");
        cell = row.createCell(18);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(19);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(20);
        cell.setCellStyle(cellStyle);

//        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);
    }

    public static void writeHeaderTrench(Sheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);

        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Danh s??ch r??nh h??? qu???n l??");
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,0,8); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=8;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        row = sheet.createRow(rowIndex+1);
        for (int i = 0;i<=8;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        rowIndex = rowIndex+2;

        // Create row
        rowIndex++;
        row = sheet.createRow(rowIndex);
        int indexCol=0;
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("KM");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("M");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("L?? tr??nh");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex,indexCol,indexCol+1); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Chi???u");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Chi???u d??i (m)");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi ch??");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tr???ng th??i");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);

        indexCol =0;
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T??? Km");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("?????n Km");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);

//        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);
    }

    public static void writeHeaderFence(Sheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);

        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Danh s??ch h??ng r??o qu???n l??");
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,0,8); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=8;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        row = sheet.createRow(rowIndex+1);
        for (int i = 0;i<=8;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        rowIndex = rowIndex+3;
        // Create row
        row = sheet.createRow(rowIndex);
        int indexCol=0;
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("KM");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("M");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("L?? tr??nh");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex,indexCol,indexCol+1); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("????n v???");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Chi???u d??i");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi ch??");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tr???ng th??i");
        mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,indexCol,indexCol); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        indexCol++;

        indexCol =0;
        rowIndex++;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T??? Km");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("?????n Km");
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(indexCol++);
        cell.setCellStyle(cellStyle);

//        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);
    }

    public static void writeHeaderDrain(Sheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);
        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Danh s??ch c???ng qu???n l??");
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,0,8); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=8;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        row = sheet.createRow(rowIndex+1);
        for (int i = 0;i<=8;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        rowIndex = rowIndex+3;


        // Create row
        int indexCol =0;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        indexCol++;

        // Create cells
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("H???ng m???c");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("KM");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("M");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("L?? tr??nh");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Kh???u ?????");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Chi???u d??i lan can tr??n c???ng (m)");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi ch??");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tr???ng th??i");
        indexCol++;

//        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);
    }

    public static void writeHeaderElectrical(Sheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);
        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Danh s??ch t??? ??i???n qu???n l??");
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,0,11); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=11;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        row = sheet.createRow(rowIndex+1);
        for (int i = 0;i<=11;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        rowIndex = rowIndex+3;

        // Create row
        row = sheet.createRow(rowIndex);
        int indexCol=0;
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("KM");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("M");
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("V??? tr??");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tr???m bi???n ??p");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T??? h??? th??? tr???m bi???n ??p");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T??? ATS");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T??? ph??n ph???i");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T??? ??i???n tr???m thu ph??");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T??? ??i???u khi???n chi???u s??ng");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi ch??");
        indexCol++;


        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tr???ng th??i");
        indexCol++;
//        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);
    }

    public static void writeHeaderAuxiliaryEquipment(Sheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);
        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Danh s??ch thi???t b??? ph??? tr??? qu???n l??");
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,0,9); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=9;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        row = sheet.createRow(rowIndex+1);
        for (int i = 0;i<=9;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        rowIndex = rowIndex+3;

        // Create row

        int indexCol =0;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("KM");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("M");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Lo???i thi???t b???");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("V??? tr??");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("????n v??? t??nh");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("S??? l?????ng");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("T??n camera");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi ch??");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tr???ng th??i");
        indexCol++;

//        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);
    }

    public static void writeHeaderTrafficSigns(Sheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);
        Row row = sheet.createRow(rowIndex);
        Cell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Danh s??ch bi???n b??o qu???n l??");
        CellRangeAddress mergedRegion = new CellRangeAddress(rowIndex,rowIndex+1,0,11); // row 0 col A and H
        sheet.addMergedRegion(mergedRegion);
        for (int i = 1;i<=11;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        row = sheet.createRow(rowIndex+1);
        for (int i = 0;i<=11;i++) {
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }

        rowIndex = rowIndex+3;

        // Create row

        int indexCol =0;
        row = sheet.createRow(rowIndex);
        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("STT");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("KM");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("M");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("L?? tr??nh");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("V??? tr??");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("N???i dung bi???n");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("K??ch th?????c");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Di???n t??ch bi???n");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("S??? c???t");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Di???n t??ch c???t");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ghi ch??");
        indexCol++;

        cell = row.createCell(indexCol);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tr???ng th??i");
        indexCol++;

//        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
//        autosizeColumn(sheet, numberOfColumn);
    }


    // Create CellStyle for header
    private static CellStyle createStyleForHeader(Sheet sheet) {
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
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }

    private static CellStyle createStyleRow(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
//        font.setBold(true);
        font.setFontHeightInPoints((short) 12); // font size
        font.setColor(IndexedColors.BLACK.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
//        cellStyle.setAlignment(HorizontalAlignment.CENTER);
//        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }

    // Write footer
//    private static void writeFooter(Sheet sheet, int rowIndex) {
//        // Create row
//        Row row = sheet.createRow(rowIndex);
//        Cell cell = row.createCell(COLUMN_INDEX_TOTAL, CellType.FORMULA);
//        cell.setCellFormula("SUM(E2:E6)");
//    }

    // Auto resize column width
    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex,true);

        }
    }

}
