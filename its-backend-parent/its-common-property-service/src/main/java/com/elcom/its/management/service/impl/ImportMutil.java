package com.elcom.its.management.service.impl;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.DeviceStatus;
import com.elcom.its.management.enums.PropertyType;
import com.elcom.its.management.enums.TypeEquipment;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ImportMutil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportMutil.class);
    @Async("import")
    public CompletableFuture<List<CommonProperty>> listBridge(Sheet sheet){
        List<CommonProperty> commonPropertyList = new ArrayList<CommonProperty>();
        try {
            if (sheet != null) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    CommonProperty commonProperty = new CommonProperty();
                    Bridge bridge = new Bridge();
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        cellIdx = currentCell.getColumnIndex();
                        if(cellIdx==0){
                            if(currentCell.getCellTypeEnum() == CellType.STRING){
                                try {
                                    int stt = Integer.valueOf(currentCell.getStringCellValue());
                                    if(stt<=0){
                                        break;
                                    }
                                } catch (Exception ex){
                                    break;
                                }
                            } else {
                                if(currentCell.getNumericCellValue()<=0){
                                    break;
                                }
                            }
                        }
                        switch (cellIdx) {
                            case 0:
                                break;
                            case 1:
                                bridge.setName(currentCell.getStringCellValue());
                                commonProperty.setName(currentCell.getStringCellValue());
                                break;
                            case 2:
                                commonProperty.setKm((int) currentCell.getNumericCellValue());
                                break;
                            case 3:
                                commonProperty.setM((int) currentCell.getNumericCellValue());
                                commonProperty.setPositionM((long) (commonProperty.getKm() * 1000 + commonProperty.getM()));

                                break;
                            case 4:
                                commonProperty.setKmEnd((int) currentCell.getNumericCellValue());
                                break;
                            case 5:
                                commonProperty.setMEnd((int) currentCell.getNumericCellValue());
                                commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM() +" - Km" + commonProperty.getKmEnd() + " + " +commonProperty.getMEnd());
                                break;
                            case 6:
                                bridge.setLength((float) currentCell.getNumericCellValue());
                                break;
                            case 7:
                                bridge.setWith((float) currentCell.getNumericCellValue());
                                break;
                            case 8:
                                bridge.setDiagram(currentCell.getStringCellValue());
                                break;
                            case 9:
                                bridge.setRhythm((int) currentCell.getNumericCellValue());
                                break;
                            case 10:
                                bridge.setMeters((float) currentCell.getNumericCellValue());
                                break;
                            case 11:
                                bridge.setPillar((int) currentCell.getNumericCellValue());
                                break;
                            case 12:
                                bridge.setBearingType(currentCell.getStringCellValue());
                                break;
                            case 13:
                                bridge.setBearingQuantity((int) currentCell.getNumericCellValue());
                                break;
                            case 14:
                                bridge.setExpansionJointsType(currentCell.getStringCellValue());
                                break;
                            case 15:
                                bridge.setExpansionJointsQuantity((float) currentCell.getNumericCellValue());
                                break;
                            case 16:
                                bridge.setBridgeRailingLength((float) currentCell.getNumericCellValue());
                                break;
                            case 17:
                                bridge.setAcreageNew((float) currentCell.getNumericCellValue());
                                break;
                            case 18:
                                bridge.setAcreage((float) currentCell.getNumericCellValue());
                                break;
                            case 19:
                                bridge.setAcreagePillar((float) currentCell.getNumericCellValue());
                                break;
                            case 20:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    bridge.setNote(currentCell.getStringCellValue());
                                    commonProperty.setNote(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    bridge.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                    commonProperty.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                }
                                break;
                            case 21:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                    bridge.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                } else {
                                    String type = currentCell.getStringCellValue();
                                    type = type.toLowerCase();
                                    if(type.contains("đang sử dụng")){
                                        commonProperty.setStatus(DeviceStatus.RUNNING);
                                        bridge.setStatus(DeviceStatus.RUNNING);
                                    } else if(type.contains("sửa chữa")){
                                        commonProperty.setStatus(DeviceStatus.MAINTAIN);
                                        bridge.setStatus(DeviceStatus.MAINTAIN);
                                    } else if(type.contains("hỏng")){
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        bridge.setStatus(DeviceStatus.STOP);
                                    } else if(type.contains("thay thế")){
                                        commonProperty.setStatus(DeviceStatus.REPLACED);
                                        bridge.setStatus(DeviceStatus.REPLACED);
                                    } else if(type.contains("cài đặt")){
                                        commonProperty.setStatus(DeviceStatus.SETUP);
                                        bridge.setStatus(DeviceStatus.SETUP);
                                    } else {
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        bridge.setStatus(DeviceStatus.STOP);
                                    }
                                }
                                break;
                            case 22:
                                commonProperty.setImageUrl(currentCell.getStringCellValue());
                                bridge.setImageUrl(currentCell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    if(cellIdx==0){
                        continue;
                    }
                    commonProperty.setData(bridge);
                    commonProperty.setType(PropertyType.BRIDGE);
                    commonPropertyList.add(commonProperty);
                }
            }
            LOGGER.info("load bridge");
            return CompletableFuture.completedFuture(commonPropertyList);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("load bridge false");
            return CompletableFuture.completedFuture(commonPropertyList);
        }
    }

    @Async("import")
    public CompletableFuture<List<CommonProperty>> listDrain(Sheet sheet) {
        List<CommonProperty> commonPropertyList = new ArrayList<CommonProperty>();
        try {
            if (sheet != null) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    CommonProperty commonProperty = new CommonProperty();
                    Drain drain = new Drain();
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        cellIdx = currentCell.getColumnIndex();
                        if(cellIdx==0){
                            if(currentCell.getCellTypeEnum() == CellType.STRING){
                                try {
                                    int stt = Integer.valueOf(currentCell.getStringCellValue());
                                    if(stt<=0){
                                        break;
                                    }
                                } catch (Exception ex){
                                    break;
                                }
                            } else {
                                if(currentCell.getNumericCellValue()<=0){
                                    break;
                                }
                            }
                        }
                        switch (cellIdx) {
                            case 0:
                                break;
                            case 1:
                                drain.setCategories(currentCell.getStringCellValue());
                                commonProperty.setName(currentCell.getStringCellValue());
                                break;
                            case 2:
                                commonProperty.setKm((int) currentCell.getNumericCellValue());
                                break;
                            case 3:
                                commonProperty.setM((int) currentCell.getNumericCellValue());
                                commonProperty.setPositionM((long) (commonProperty.getKm() * 1000 + commonProperty.getM()));
                                commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM());
                                break;
                            case 4:
                                commonProperty.setKmEnd((int) currentCell.getNumericCellValue());
                                break;
                            case 5:
                                commonProperty.setMEnd((int) currentCell.getNumericCellValue());
                                commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM() +" - Km" + commonProperty.getKmEnd() + " + " +commonProperty.getMEnd());
                                break;
                            case 6:
                                drain.setAperture(currentCell.getStringCellValue());
                                break;
                            case 7:
                                drain.setBridgeRailingLength((float) currentCell.getNumericCellValue());
                                break;
                            case 8:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    drain.setNote(currentCell.getStringCellValue());
                                    commonProperty.setNote(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    drain.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                    commonProperty.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                }
                                break;
                            case 9:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                    drain.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                } else {
                                    String type = currentCell.getStringCellValue();
                                    type = type.toLowerCase();
                                    if(type.contains("đang sử dụng")){
                                        commonProperty.setStatus(DeviceStatus.RUNNING);
                                        drain.setStatus(DeviceStatus.RUNNING);
                                    } else if(type.contains("sửa chữa")){
                                        commonProperty.setStatus(DeviceStatus.MAINTAIN);
                                        drain.setStatus(DeviceStatus.MAINTAIN);
                                    } else if(type.contains("hỏng")){
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        drain.setStatus(DeviceStatus.STOP);
                                    } else if(type.contains("thay thế")){
                                        commonProperty.setStatus(DeviceStatus.REPLACED);
                                        drain.setStatus(DeviceStatus.REPLACED);
                                    } else if(type.contains("cài đặt")){
                                        commonProperty.setStatus(DeviceStatus.SETUP);
                                        drain.setStatus(DeviceStatus.SETUP);
                                    } else {
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        drain.setStatus(DeviceStatus.STOP);
                                    }
                                }
                                break;
                            case 10:
                                commonProperty.setImageUrl(currentCell.getStringCellValue());
                                drain.setImageUrl(currentCell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    if(cellIdx==0){
                        continue;
                    }
                    commonProperty.setData(drain);
                    commonProperty.setType(PropertyType.DRAIN);
                    commonPropertyList.add(commonProperty);
                }
            }
            LOGGER.info("load drain");
            return CompletableFuture.completedFuture(commonPropertyList);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("load drain false");
            return CompletableFuture.completedFuture(commonPropertyList);
        }
    }

    @Async("import")
    public CompletableFuture<List<CommonProperty>> listTrench(Sheet sheet) {
        List<CommonProperty> commonPropertyList = new ArrayList<CommonProperty>();
        try {
            if (sheet != null) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    CommonProperty commonProperty = new CommonProperty();
                    Trench trench = new Trench();
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        cellIdx = currentCell.getColumnIndex();
                        if(cellIdx==0){
                            if(currentCell.getCellTypeEnum() == CellType.STRING){
                                try {
                                    int stt = Integer.valueOf(currentCell.getStringCellValue());
                                    if(stt<=0){
                                        break;
                                    }
                                } catch (Exception ex){
                                    break;
                                }
                            } else {
                                if(currentCell.getNumericCellValue()<=0){
                                    break;
                                }
                            }
                        }
                        switch (cellIdx) {
                            case 0:
                                break;
                            case 1:
                                commonProperty.setKm((int) currentCell.getNumericCellValue());
                                break;
                            case 2:
                                commonProperty.setM((int) currentCell.getNumericCellValue());
                                commonProperty.setPositionM((long) (commonProperty.getKm() * 1000 + commonProperty.getM()));
                                break;
                            case 3:
                                commonProperty.setKmEnd((int) currentCell.getNumericCellValue());
                                break;
                            case 4:
                                commonProperty.setMEnd((int) currentCell.getNumericCellValue());
                                commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM() +" - Km" + commonProperty.getKmEnd() + " + " +commonProperty.getMEnd());
                                break;
                            case 5:
                                trench.setDirectionCode(currentCell.getStringCellValue());
                                commonProperty.setName("Rãnh hở chiều " + currentCell.getStringCellValue());
                                break;
                            case 6:
                                trench.setLength((float) currentCell.getNumericCellValue());
                                break;
                            case 7:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    trench.setNote(currentCell.getStringCellValue());
                                    commonProperty.setNote(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    trench.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                    commonProperty.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                }
                                break;
                            case 8:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                    trench.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                } else {
                                    String type = currentCell.getStringCellValue();
                                    type = type.toLowerCase();
                                    if(type.contains("đang sử dụng")){
                                        commonProperty.setStatus(DeviceStatus.RUNNING);
                                        trench.setStatus(DeviceStatus.RUNNING);
                                    } else if(type.contains("sửa chữa")){
                                        commonProperty.setStatus(DeviceStatus.MAINTAIN);
                                        trench.setStatus(DeviceStatus.MAINTAIN);
                                    } else if(type.contains("hỏng")){
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        trench.setStatus(DeviceStatus.STOP);
                                    } else if(type.contains("thay thế")){
                                        commonProperty.setStatus(DeviceStatus.REPLACED);
                                        trench.setStatus(DeviceStatus.REPLACED);
                                    } else if(type.contains("cài đặt")){
                                        commonProperty.setStatus(DeviceStatus.SETUP);
                                        trench.setStatus(DeviceStatus.SETUP);
                                    } else {
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        trench.setStatus(DeviceStatus.STOP);
                                    }
                                }
                                break;
                            case 9:
                                commonProperty.setImageUrl(currentCell.getStringCellValue());
                                trench.setImageUrl(currentCell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    if(cellIdx==0){
                        continue;
                    }
                    commonProperty.setData(trench);
                    commonProperty.setType(PropertyType.TRENCH);
                    commonPropertyList.add(commonProperty);
                }
            }
            LOGGER.info("load trench");
            return CompletableFuture.completedFuture(commonPropertyList);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("load trench false");
            return CompletableFuture.completedFuture(commonPropertyList);
        }
    }

    @Async("import")
    public CompletableFuture<List<CommonProperty>> listTrafficSigns(Sheet sheet) {
        List<CommonProperty> commonPropertyList = new ArrayList<CommonProperty>();
        try {
            if (sheet != null) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
//                    if (rowNumber == 0 || rowNumber == 1 || rowNumber == 2 ) {
//                        rowNumber++;
//                        continue;
//                    }
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    CommonProperty commonProperty = new CommonProperty();
                    TrafficSigns data = new TrafficSigns();
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        cellIdx = currentCell.getColumnIndex();
                        if(cellIdx==0){
                            if(currentCell.getCellTypeEnum() == CellType.STRING){
                                try {
                                    int stt = Integer.valueOf(currentCell.getStringCellValue());
                                    if(stt<=0){
                                        break;
                                    }
                                } catch (Exception ex){
                                    break;
                                }
                            } else {
                                if(currentCell.getNumericCellValue()<=0){
                                    break;
                                }
                            }
                        }
                        switch (cellIdx) {
                            case 0:
                                break;
                            case 1:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setKm((int) currentCell.getNumericCellValue());
                                }else {
                                    commonProperty.setKm(commonPropertyList.get(commonPropertyList.size()-1).getKm());
                                }
                                break;
                            case 2:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setM((int) currentCell.getNumericCellValue());
                                    commonProperty.setPositionM((long) (commonProperty.getKm() * 1000 + commonProperty.getM()));
                                    commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM());
                                }
                                break;

                            case 3:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                    commonProperty.setKmEnd((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 4:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                    commonProperty.setMEnd((int) currentCell.getNumericCellValue());
                                    commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                    commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM() +" - Km" + commonProperty.getKmEnd() + " + " +commonProperty.getMEnd());
                                } else {
                                    commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                    commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM());
                                }
                                break;
                            case 5:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setDirectionCode(currentCell.getStringCellValue());
                                }

                                break;
                            case 6:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setContent(currentCell.getStringCellValue());
                                    commonProperty.setName(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setContent("Biển "+ String.valueOf(currentCell.getNumericCellValue()));
                                    commonProperty.setName("Biển "+  String.valueOf(currentCell.getNumericCellValue()));
                                }

                                break;
                            case 7:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setSize(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setSize(String.valueOf(currentCell.getNumericCellValue()));
                                }
                                break;
                            case 8:
                               if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setAcreage((float) currentCell.getNumericCellValue());
                                }
                                break;
                            case 9:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setPillar((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 10:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setAcreagePillar((float) currentCell.getNumericCellValue());
                                }
                                break;
                            case 11:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setNote(currentCell.getStringCellValue());
                                    commonProperty.setNote(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                    commonProperty.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                }

                                break;
                            case 12:
                                 if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                  commonProperty.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                  data.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                } else {
                                     String type = currentCell.getStringCellValue();
                                     type = type.toLowerCase();
                                     if(type.contains("đang sử dụng")){
                                         commonProperty.setStatus(DeviceStatus.RUNNING);
                                         data.setStatus(DeviceStatus.RUNNING);
                                     } else if(type.contains("sửa chữa")){
                                         commonProperty.setStatus(DeviceStatus.MAINTAIN);
                                         data.setStatus(DeviceStatus.MAINTAIN);
                                     } else if(type.contains("hỏng")){
                                         commonProperty.setStatus(DeviceStatus.STOP);
                                         data.setStatus(DeviceStatus.STOP);
                                     } else if(type.contains("thay thế")){
                                         commonProperty.setStatus(DeviceStatus.REPLACED);
                                         data.setStatus(DeviceStatus.REPLACED);
                                     } else if(type.contains("cài đặt")){
                                         commonProperty.setStatus(DeviceStatus.SETUP);
                                         data.setStatus(DeviceStatus.SETUP);
                                     } else {
                                         commonProperty.setStatus(DeviceStatus.STOP);
                                         data.setStatus(DeviceStatus.STOP);
                                     }
                                }
                                break;
                            case 13:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setImagesUrl(currentCell.getStringCellValue());
                                    commonProperty.setImageUrl(currentCell.getStringCellValue());
                                }
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    if(cellIdx==0){
                        continue;
                    }
                    commonProperty.setData(data);
                    commonProperty.setType(PropertyType.TRAFFIC_SIGNS);
                    commonPropertyList.add(commonProperty);
                }
            }
            LOGGER.info("load traffic signs");
            return CompletableFuture.completedFuture(commonPropertyList);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("load traffic signs false");
            return CompletableFuture.completedFuture(commonPropertyList);
        }
    }

    @Async("import")
    public CompletableFuture<List<CommonProperty>> listFence(Sheet sheet) {
        List<CommonProperty> commonPropertyList = new ArrayList<CommonProperty>();
        try {
            if (sheet != null) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
//                    if (rowNumber == 0 || rowNumber == 1 || rowNumber == 2) {
//                        rowNumber++;
//                        continue;
//                    }
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    CommonProperty commonProperty = new CommonProperty();
                    Fence data = new Fence();
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        cellIdx = currentCell.getColumnIndex();
                        if(cellIdx==0){
                            if(currentCell.getCellTypeEnum() == CellType.STRING){
                                try {
                                    int stt = Integer.valueOf(currentCell.getStringCellValue());
                                    if(stt<=0){
                                        break;
                                    }
                                } catch (Exception ex){
                                    break;
                                }
                            } else {
                                if(currentCell.getNumericCellValue()<=0){
                                    break;
                                }
                            }
                        }
                        switch (cellIdx) {
                            case 0:
                                break;
                            case 1:
                                commonProperty.setKm((int) currentCell.getNumericCellValue());
                                break;
                            case 2:
                                commonProperty.setM((int) currentCell.getNumericCellValue());
                                commonProperty.setPositionM((long) (commonProperty.getKm() * 1000 + commonProperty.getM()));
                                break;
                            case 3:
                                commonProperty.setKmEnd((int) currentCell.getNumericCellValue());
                                break;
                            case 4:
                                commonProperty.setMEnd((int) currentCell.getNumericCellValue());
                                commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM() +" - Km" + commonProperty.getKmEnd() + " + " +commonProperty.getMEnd());
                                break;
                            case 5:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setUnit(currentCell.getStringCellValue());
                                    commonProperty.setName("Hàng rào");
                                }
                                break;
                            case 6:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setLength(Float.valueOf(currentCell.getStringCellValue()));
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setLength((float) currentCell.getNumericCellValue());
                                }
                                break;
                            case 7:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setNote(currentCell.getStringCellValue());
                                    commonProperty.setNote(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                    commonProperty.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                }

                                break;
                            case 8:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                    data.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                } else {
                                    String type = currentCell.getStringCellValue();
                                    type = type.toLowerCase();
                                    if(type.contains("đang sử dụng")){
                                        commonProperty.setStatus(DeviceStatus.RUNNING);
                                        data.setStatus(DeviceStatus.RUNNING);
                                    } else if(type.contains("sửa chữa")){
                                        commonProperty.setStatus(DeviceStatus.MAINTAIN);
                                        data.setStatus(DeviceStatus.MAINTAIN);
                                    } else if(type.contains("hỏng")){
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        data.setStatus(DeviceStatus.STOP);
                                    } else if(type.contains("thay thế")){
                                        commonProperty.setStatus(DeviceStatus.REPLACED);
                                        data.setStatus(DeviceStatus.REPLACED);
                                    } else if(type.contains("cài đặt")){
                                        commonProperty.setStatus(DeviceStatus.SETUP);
                                        data.setStatus(DeviceStatus.SETUP);
                                    } else {
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        data.setStatus(DeviceStatus.STOP);
                                    }
                                }
                                break;
                            case 9:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setImagesUrl(currentCell.getStringCellValue());
                                    commonProperty.setImageUrl(currentCell.getStringCellValue());
                                }
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    if(cellIdx==0){
                        continue;
                    }
                    commonProperty.setData(data);
                    commonProperty.setType(PropertyType.FENCE);
                    commonPropertyList.add(commonProperty);
                }
            }
            LOGGER.info("load fence");
            return CompletableFuture.completedFuture(commonPropertyList);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("load fence false");
            return CompletableFuture.completedFuture(commonPropertyList);
        }
    }

    @Async("import")
    public CompletableFuture<List<CommonProperty>> listElectricalCabinets(Sheet sheet) {
        List<CommonProperty> commonPropertyList = new ArrayList<CommonProperty>();
        try {
            if (sheet != null) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
//                    if (rowNumber == 0 || rowNumber == 1 || rowNumber == 2) {
//                        rowNumber++;
//                        continue;
//                    }
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    CommonProperty commonProperty = new CommonProperty();
                    ElectricalCabinets data = new ElectricalCabinets();
                    commonProperty.setName("Tủ điện");
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        cellIdx = currentCell.getColumnIndex();
                        if(cellIdx==0){
                            if(currentCell.getCellTypeEnum() == CellType.STRING){
                                try {
                                    int stt = Integer.valueOf(currentCell.getStringCellValue());
                                    if(stt<=0){
                                        break;
                                    }
                                } catch (Exception ex){
                                    break;
                                }
                            } else {
                                if(currentCell.getNumericCellValue()<=0){
                                    break;
                                }
                            }
                        }
                        switch (cellIdx) {
                            case 0:
                                break;
                            case 1:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setKm((int) currentCell.getNumericCellValue());
                                } else {
                                    commonProperty.setKm(Integer.valueOf( currentCell.getStringCellValue()));
                                }
                                break;
                            case 2:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setM((int) currentCell.getNumericCellValue());
                                    commonProperty.setPositionM((long) (commonProperty.getKm() * 1000 + commonProperty.getM()));
                                } else {
                                    commonProperty.setM(Integer.valueOf( currentCell.getStringCellValue()));
                                    commonProperty.setPositionM((long) (commonProperty.getKm() * 1000 + commonProperty.getM()));
                                }
                                break;
                            case 3:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                    commonProperty.setKmEnd((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 4:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                    commonProperty.setMEnd((int) currentCell.getNumericCellValue());
                                    commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                    commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM() +" - Km" + commonProperty.getKmEnd() + " + " +commonProperty.getMEnd());
                                } else {
                                    commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                    commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM());
                                }
                             break;
                            case 5:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setSite(currentCell.getStringCellValue());
                                } else {
                                    data.setSite(String.valueOf(currentCell.getNumericCellValue()));
                                }

                                break;
                            case 6:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setSubstation(0);
                                } else {
                                    data.setSubstation((int) currentCell.getNumericCellValue());
                                }

                                break;
                            case 7:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setLowVoltageStation(0);
                                } else {
                                    data.setLowVoltageStation((int) currentCell.getNumericCellValue());
                                }

                                break;
                            case 8:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setAts(0);
                                } else {
                                    data.setAts((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 9:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setDistributionCabinet(0);
                                } else {
                                    data.setDistributionCabinet((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 10:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setChargingStation(0);
                                } else {
                                    data.setChargingStation((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 11:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setLightingControl(0);
                                } else {
                                    data.setLightingControl((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 12:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setQuantity(0);
                                } else {
                                    data.setQuantity((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 13:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setUnit(currentCell.getStringCellValue());
                                } else {
                                    data.setUnit(String.valueOf(currentCell.getNumericCellValue()));
                                }
                                break;
                            case 14:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setNote(currentCell.getStringCellValue());
                                    commonProperty.setNote(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                    commonProperty.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                }

                                break;
                            case 15:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                    data.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                } else {
                                    String type = currentCell.getStringCellValue();
                                    type = type.toLowerCase();
                                    if(type.contains("đang sử dụng")){
                                        commonProperty.setStatus(DeviceStatus.RUNNING);
                                        data.setStatus(DeviceStatus.RUNNING);
                                    } else if(type.contains("sửa chữa")){
                                        commonProperty.setStatus(DeviceStatus.MAINTAIN);
                                        data.setStatus(DeviceStatus.MAINTAIN);
                                    } else if(type.contains("hỏng")){
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        data.setStatus(DeviceStatus.STOP);
                                    } else if(type.contains("thay thế")){
                                        commonProperty.setStatus(DeviceStatus.REPLACED);
                                        data.setStatus(DeviceStatus.REPLACED);
                                    } else if(type.contains("cài đặt")){
                                        commonProperty.setStatus(DeviceStatus.SETUP);
                                        data.setStatus(DeviceStatus.SETUP);
                                    } else {
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        data.setStatus(DeviceStatus.STOP);
                                    }
                                }
                                break;
                            case 16:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setImagesUrl(currentCell.getStringCellValue());
                                    commonProperty.setImageUrl(currentCell.getStringCellValue());
                                }
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    if(cellIdx==0){
                        continue;
                    }
                    commonProperty.setData(data);
                    commonProperty.setType(PropertyType.ELECTRICAL_CABINETS);
                    commonPropertyList.add(commonProperty);
                }
            }
            LOGGER.info("load ElectricalCabinets");
            return CompletableFuture.completedFuture(commonPropertyList);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("load ElectricalCabinets false");
            return CompletableFuture.completedFuture(commonPropertyList);
        }
    }

    @Async("import")
    public CompletableFuture<List<CommonProperty>> listAuxiliaryEquipment(Sheet sheet) {
        List<CommonProperty> commonPropertyList = new ArrayList<CommonProperty>();
        try {
            if (sheet != null) {
                Iterator<Row> rows = sheet.iterator();
                int rowNumber = 0;
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    // skip header
//                    if (rowNumber == 0 || rowNumber==1 || rowNumber==2) {
//                        rowNumber++;
//                        continue;
//                    }
                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    CommonProperty commonProperty = new CommonProperty();
                    AuxiliaryEquipment data = new AuxiliaryEquipment();
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        cellIdx = currentCell.getColumnIndex();
                        if(cellIdx==0){
                            if(currentCell.getCellTypeEnum() == CellType.STRING){
                                try {
                                    int stt = Integer.valueOf(currentCell.getStringCellValue());
                                    if(stt<=0){
                                        break;
                                    }
                                } catch (Exception ex){
                                    break;
                                }
                            } else {
                                if(currentCell.getNumericCellValue()<=0){
                                    break;
                                }
                            }
                        }
                        switch (cellIdx) {
                            case 0:
                                break;
                            case 1:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setKm((int) currentCell.getNumericCellValue());
                                } else {
                                    commonProperty.setKm(Integer.valueOf( currentCell.getStringCellValue()));
                                }
                                break;
                            case 2:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setM((int) currentCell.getNumericCellValue());
                                    commonProperty.setPositionM((long) (commonProperty.getKm() * 1000 + commonProperty.getM()));
                                } else {
                                    commonProperty.setM(Integer.valueOf( currentCell.getStringCellValue()));
                                    commonProperty.setPositionM((long) (commonProperty.getKm() * 1000 + commonProperty.getM()));
                                }
                                break;
                            case 3:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                    commonProperty.setKmEnd((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 4:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                    commonProperty.setMEnd((int) currentCell.getNumericCellValue());
                                    commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                    commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM() +" - Km" + commonProperty.getKmEnd() + " + " +commonProperty.getMEnd());
                                } else {
                                    commonProperty.setPositionMEnd((long) (commonProperty.getKmEnd() * 1000 + commonProperty.getMEnd()));
                                    commonProperty.setPosition("Km"+commonProperty.getKm()+" + "+commonProperty.getM());
                                }
                                break;
                            case 5:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    String type = currentCell.getStringCellValue();
                                    type = type.toLowerCase();
                                    if(type.contains("chống sét")){
                                        data.setType(TypeEquipment.LIGHTNING_CONDUCTOR);
                                        commonProperty.setName(TypeEquipment.LIGHTNING_CONDUCTOR.description());
                                    } else if(type.contains("pin")){
                                        data.setType(TypeEquipment.SOLAR_BATTERY);
                                        commonProperty.setName(TypeEquipment.SOLAR_BATTERY.description());
                                    } else if(type.contains("lưu điện")){
                                        data.setType(TypeEquipment.UPS);
                                        commonProperty.setName(TypeEquipment.UPS.description());
                                    } else if(type.contains("màn hình")){
                                        data.setType(TypeEquipment.SCREEN);
                                        commonProperty.setName(TypeEquipment.SCREEN.description());
                                    } else{
                                        data.setType(TypeEquipment.UNKNOW);
                                        commonProperty.setName(TypeEquipment.UNKNOW.description());
                                    }
                                }

                                break;
                            case 6:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setSite(currentCell.getStringCellValue());
                                } else  if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setSite(String.valueOf(currentCell.getNumericCellValue()));
                                }

                                break;
                            case 7:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setUnit(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setUnit(String.valueOf(currentCell.getNumericCellValue()));
                                }

                                break;
                            case 8:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setQuantity(0);
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setQuantity((int) currentCell.getNumericCellValue());
                                }
                                break;
                            case 9:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setNameCamera(currentCell.getStringCellValue());
                                } else {
                                    data.setNameCamera(String.valueOf(currentCell.getNumericCellValue()));
                                }
                                break;
                            case 10:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setNote(currentCell.getStringCellValue());
                                    commonProperty.setNote(currentCell.getStringCellValue());
                                } else if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    data.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                    commonProperty.setNote(String.valueOf(currentCell.getNumericCellValue()));
                                }

                                break;
                            case 11:
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    commonProperty.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                    data.setStatus(DeviceStatus.parse((int) currentCell.getNumericCellValue()));
                                } else {
                                    String type = currentCell.getStringCellValue();
                                    type = type.toLowerCase();
                                    if(type.contains("đang sử dụng")){
                                        commonProperty.setStatus(DeviceStatus.RUNNING);
                                        data.setStatus(DeviceStatus.RUNNING);
                                    } else if(type.contains("sửa chữa")){
                                        commonProperty.setStatus(DeviceStatus.MAINTAIN);
                                        data.setStatus(DeviceStatus.MAINTAIN);
                                    } else if(type.contains("hỏng")){
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        data.setStatus(DeviceStatus.STOP);
                                    } else if(type.contains("thay thế")){
                                        commonProperty.setStatus(DeviceStatus.REPLACED);
                                        data.setStatus(DeviceStatus.REPLACED);
                                    } else if(type.contains("cài đặt")){
                                        commonProperty.setStatus(DeviceStatus.SETUP);
                                        data.setStatus(DeviceStatus.SETUP);
                                    } else {
                                        commonProperty.setStatus(DeviceStatus.STOP);
                                        data.setStatus(DeviceStatus.STOP);
                                    }
                                }
                                break;
                            case 12:
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    data.setImagesUrl(currentCell.getStringCellValue());
                                    commonProperty.setImageUrl(currentCell.getStringCellValue());
                                }
                                break;
                            default:
                                break;
                        }
                        cellIdx++;
                    }
                    if(cellIdx==0){
                        continue;
                    }
                    commonProperty.setData(data);
                    commonProperty.setType(PropertyType.AUXILIARY_EQUIPMENT);
                    commonPropertyList.add(commonProperty);
                }
            }
            LOGGER.info("load AuxiliaryEquipment");
            return CompletableFuture.completedFuture(commonPropertyList);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.info("load AuxiliaryEquipment false");
            return CompletableFuture.completedFuture(commonPropertyList);
        }
    }

}
