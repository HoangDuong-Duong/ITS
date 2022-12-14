/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.EventStatus;
import com.elcom.its.management.enums.EventType;
import com.elcom.its.management.enums.JobType;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.management.model.ActionHistory;
import com.elcom.its.management.model.EventFile;
import com.elcom.its.management.model.File;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.service.*;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.itextpdf.text.List;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.jsoup.Jsoup;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
//import com.spire.pdf.FileFormat;
//import com.spire.pdf.PdfDocument;


/**
 * @author Admin
 */
@Service
public class EventFileServiceImpl implements EventFileService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EventFileServiceImpl.class);
    @Autowired
    private EventService eventService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private UserService userService;
    @Autowired
    private DeviceStatusService deviceStatusService;
    @Autowired
    private EventInfoService eventInfoService;
    @Autowired
    private HotlineService hotlineService;
    @Autowired
    private ReportDeviceService reportDeviceService;
    @Value("${url.image}")
    private String urlImage;
    @Value("${url.caotoc}")
    private String caotoc;
    @Autowired
    private RabbitMQClient rabbitMQClient;
    @Autowired
    private JobService jobService;
    @Autowired
    private CalendarService calendarService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public EventFile createFileEventHistory(String eventId, String uuid) throws Exception {
        Document document = new Document(PageSize.A4, 30f, 15f, 20f, 20f);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat datePasser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat time = new SimpleDateFormat("HH-mm-ss");
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Long sizeFile = new Long(0);
        try {
            EventResponseDTO eventResponseDTO = eventService.historyEvent(eventId);
            if (eventResponseDTO != null && eventResponseDTO.getData() != null && !eventResponseDTO.getData().isEmpty()) {
                Map<String, User> mapUser = getAllUser();
                List<EventDTO> eventDTO = eventResponseDTO.getData();
                EventDTO data = eventDTO.get(eventDTO.size() - 1);
                Date now = new Date();
                String fileName = "B??o c??o x??? l?? s??? ki???n-" + data.getKey() + "-" + date.format(now) + " " + time.format(now) + ".pdf";
                String urlFile = "" + fileName;
                Path pathh = Paths.get(urlFile);
                PdfWriter.getInstance(document, new FileOutputStream(urlFile));
                document.open();
                Font font1 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font2 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font3 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font1.setSize(12);
                font2.setSize(15);
                font3.setSize(21);
                BaseFont baseFont = BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font boldText = new Font(baseFont, 12);
                Paragraph tieuDe = new Paragraph("H??? TH???NG GI??M S??T CAO T???C", font2);
                tieuDe.setAlignment(Element.ALIGN_RIGHT);
                Paragraph space = new Paragraph("         ");
                try {
                    Image image1 = Image.getInstance(new URL(caotoc));
                    image1.scaleToFit(80f, 80f);
                    image1.setAlignment(Element.ALIGN_LEFT);
                    tieuDe.add(image1);
                } catch (IOException ex) {
                    Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }

                Paragraph tenVanBan = new Paragraph("B??O C??O QU?? TR??NH X??? L?? S??? KI???N ", font3);
                tenVanBan.setAlignment(Element.ALIGN_CENTER);
                Paragraph infoEvent = new Paragraph("I. Th??ng tin s??? ki???n", boldText);
                Paragraph paragraph = new Paragraph("D??? li???u k???t xu???t l??c: " + timeFormat.format(now) + " - " + dateFormat.format(now), font1);
                Paragraph timeStart = new Paragraph("Th???i gian ph??t hi???n: " + timeFormat.format(datePasser.parse(data.getStartTime())) + " - " + dateFormat.format(datePasser.parse(data.getStartTime())), font1);
                Paragraph address1 = new Paragraph("V??? tr?? ph??t hi???n: " + data.getSite().getSiteName(), font1);
                Paragraph address2;
                if (data.getSiteCorrect() == null) {
                    address2 = new Paragraph("V??? tr?? ch??nh x??c: " + data.getSite().getSiteName(), font1);
                } else {
                    address2 = new Paragraph("V??? tr?? ch??nh x??c: " + data.getSiteCorrect().getSiteName(), font1);
                }
                Paragraph typeEvent = new Paragraph("Lo???i s??? ki???n: " + data.getEventName(), font1);
                Paragraph sourceName;
                if (StringUtil.isNullOrEmpty(data.getSourceName())) {
                    sourceName = new Paragraph("Ngu???n ph??t hi???n: " + data.getObjectCreate(), font1);
                } else {
                    sourceName = new Paragraph("Ngu???n ph??t hi???n: " + data.getSourceName(), font1);
                }
                Paragraph status = new Paragraph("Tr???ng th??i: " + data.getEventStatus().description(), font1);
                Paragraph note;
                if (StringUtil.isNullOrEmpty(data.getNote())) {
                    note = new Paragraph("Ghi ch??: " + "", font1);
                } else {
                    note = new Paragraph("Ghi ch??: " + data.getNote(), font1);
                }
                Paragraph image = new Paragraph("H??nh ???nh s??? ki???n: ", font1);

                document.add(tieuDe);
                document.add(space);
                document.add(tenVanBan);
                document.add(space);
                document.add(infoEvent);
                document.add(paragraph);
                document.add(timeStart);
                document.add(address1);
                document.add(address2);
                document.add(typeEvent);
                document.add(sourceName);
                document.add(status);
                document.add(note);
                document.add(image);
                document.add(space);
                String imageUrl = data.getImageUrl();
                while (imageUrl != null && imageUrl.indexOf(",") > 0) {
                    try {
                        String url = imageUrl.substring(0, imageUrl.indexOf(","));
                        Image imageEvent;
                        if (url.startsWith("http")) {
                            imageEvent = Image.getInstance(new URL(url));
                        } else {
                            imageEvent = Image.getInstance(new URL(urlImage + url));
                        }
//                        imageEvent.scaleToFit(200f, 200f);
                        imageEvent.scaleAbsolute(400f, 200f);
                        imageEvent.setSpacingBefore(5f);
                        imageEvent.setAlignment(Element.ALIGN_CENTER);
                        PdfPTable tableImageEvent = new PdfPTable(1);
                        PdfPCell imageEventCell = new PdfPCell();
                        imageEventCell.addElement(imageEvent);
                        imageEventCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        imageEventCell.setBorder(Rectangle.NO_BORDER);
                        imageEventCell.setPaddingLeft(-1f);
                        tableImageEvent.addCell(imageEventCell);
                        tableImageEvent.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tableImageEvent.setSpacingAfter(3f);
                        tableImageEvent.setSpacingBefore(3f);
                        document.add(tableImageEvent);
                        imageUrl = imageUrl.substring(imageUrl.indexOf(",") + 1);
                    } catch (IOException ex) {
                        Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (!StringUtil.isNullOrEmpty(imageUrl)) {
                    try {
                        Image imageEvent;
                        if (imageUrl.startsWith("http")) {
                            imageEvent = Image.getInstance(new URL(imageUrl));
                        } else {
                            imageEvent = Image.getInstance(new URL(urlImage + imageUrl));
                        }
                        imageEvent.scaleAbsolute(400f, 200f);
                        imageEvent.setSpacingBefore(5f);
                        imageEvent.setAlignment(Element.ALIGN_CENTER);
                        PdfPTable tableImageEvent = new PdfPTable(1);
                        PdfPCell imageEventCell = new PdfPCell();
                        imageEventCell.addElement(imageEvent);
                        imageEventCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        imageEventCell.setBorder(Rectangle.NO_BORDER);
                        imageEventCell.setPaddingLeft(-1f);
                        tableImageEvent.addCell(imageEventCell);
                        tableImageEvent.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tableImageEvent.setSpacingAfter(3f);
                        tableImageEvent.setSpacingBefore(3f);
                        document.add(tableImageEvent);
                    } catch (IOException ex) {
                        Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                image.setAlignment(Element.ALIGN_CENTER);

                PdfPTable tableHistoryEvent = new PdfPTable(1);
                PdfPCell historyEvent = new PdfPCell(new Phrase("II. L???ch s??? x??? l?? s??? ki???n", boldText));
                historyEvent.setHorizontalAlignment(Element.ALIGN_LEFT);
                historyEvent.setBorder(Rectangle.NO_BORDER);
                historyEvent.setPaddingLeft(-1f);
                tableHistoryEvent.addCell(historyEvent);
                tableHistoryEvent.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableHistoryEvent.setSpacingAfter(10f);
                tableHistoryEvent.setSpacingBefore(5f);
                document.add(tableHistoryEvent);
                PdfPTable table = new PdfPTable(4);
                PdfPCell cell = new PdfPCell(new Phrase("Thao t??c", boldText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderWidth(1);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Tr???ng th??i", boldText));
                cell.setBorderWidth(1);
                cell.setPadding(3f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Th???i gian", boldText));
                cell.setBorderWidth(1);
                cell.setPadding(3f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Ng?????i th???c hi???n", boldText));
                cell.setBorderWidth(1);
                cell.setPadding(3f);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("T???o m???i s??? ki???n", font1));
                cell.setBorderWidth(1);
                cell.setBorderWidthTop(0);
                cell.setPadding(3f);
                table.addCell(cell);
                if (data.getEvent().code() == EventType.MANUAL_INPUT.code()) {
                    cell = new PdfPCell(new Phrase("??ang x??? l??", font1));
                    cell.setBorderWidth(1);
                    cell.setBorderWidthTop(0);
                    cell.setBorderWidthLeft(0);
                    cell.setPadding(3f);
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("Ch??a xem", font1));
                    cell.setBorderWidth(1);
                    cell.setBorderWidthTop(0);
                    cell.setBorderWidthLeft(0);
                    cell.setPadding(3f);
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Phrase(timeFormat.format(datePasser.parse(data.getStartTime())) + " - " + dateFormat.format(datePasser.parse(data.getStartTime())), font1));
                cell.setPadding(3f);
                table.addCell(cell);
                if (data.getEvent().code() == EventType.MANUAL_INPUT.code()) {
                    cell = new PdfPCell(new Phrase(mapUser.get(data.getModifiedBy()).getFullName(), font1));
                    cell.setBorderWidth(1);
                    cell.setBorderWidthTop(0);
                    cell.setBorderWidthLeft(0);
                    cell.setPadding(3f);
                    table.addCell(cell);
                } else {
                    cell = new PdfPCell(new Phrase("H??? th???ng", font1));
                    cell.setBorderWidth(1);
                    cell.setBorderWidthTop(0);
                    cell.setBorderWidthLeft(0);
                    cell.setPadding(3f);
                    table.addCell(cell);
                }
                for (EventDTO eventDTO1 : eventDTO) {
                    if (eventDTO1.getEvent().code() == EventType.MANUAL_INPUT.code()) {
                        if (eventDTO1.getEventStatus().code() == EventStatus.PROCESSING.code()) {
                            if (eventDTO1.getModifiedAction() != null && !eventDTO1.getModifiedAction().isEmpty()) {
                                cell = new PdfPCell(new Phrase(eventDTO1.getModifiedAction(), font1));
                                cell.setBorderWidth(1);
                                cell.setBorderWidthTop(0);
                                cell.setPadding(3f);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(eventDTO1.getEventStatus().description(), font1));
                                cell.setBorderWidth(1);
                                cell.setBorderWidthTop(0);
                                cell.setBorderWidthLeft(0);
                                cell.setPadding(3f);
                                table.addCell(cell);
                                cell = new PdfPCell(new Phrase(timeFormat.format(datePasser.parse(eventDTO1.getModifiedDate())) + " - " + dateFormat.format(datePasser.parse(eventDTO1.getModifiedDate())), font1));
                                cell.setPadding(3f);
                                cell.setBorderWidth(1);
                                cell.setBorderWidthTop(0);
                                cell.setBorderWidthLeft(0);
                                table.addCell(cell);
                                if (eventDTO1.getModifiedBy() == null || eventDTO1.getModifiedBy().isEmpty()) {
                                    cell = new PdfPCell(new Phrase("Th??m tay", font1));
                                    cell.setBorderWidth(1);
                                    cell.setBorderWidthTop(0);
                                    cell.setBorderWidthLeft(0);
                                    cell.setPadding(3f);
                                    table.addCell(cell);
                                } else {
                                    cell = new PdfPCell(new Phrase(mapUser.get(eventDTO1.getModifiedBy()).getFullName(), font1));
                                    cell.setPadding(3f);
                                    cell.setBorderWidth(1);
                                    cell.setBorderWidthTop(0);
                                    cell.setBorderWidthLeft(0);
                                    table.addCell(cell);
                                }

                            }
                        }
                        if (eventDTO1.getEventStatus().code() == EventStatus.PROCESSED.code()) {
                            cell = new PdfPCell(new Phrase(eventDTO1.getModifiedAction(), font1));
                            cell.setBorderWidth(1);
                            cell.setBorderWidthTop(0);
                            cell.setPadding(3f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(eventDTO1.getEventStatus().description(), font1));
                            cell.setBorderWidth(1);
                            cell.setBorderWidthTop(0);
                            cell.setBorderWidthLeft(0);
                            cell.setPadding(3f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(timeFormat.format(datePasser.parse(eventDTO1.getModifiedDate())) + " - " + dateFormat.format(datePasser.parse(eventDTO1.getModifiedDate())), font1));
                            cell.setBorderWidth(1);
                            cell.setBorderWidthTop(0);
                            cell.setBorderWidthLeft(0);
                            cell.setPadding(3f);
                            table.addCell(cell);
                            if (eventDTO1.getModifiedBy() == null || eventDTO1.getModifiedBy().isEmpty()) {
                                cell = new PdfPCell(new Phrase("Th??m tay", font1));
                                cell.setBorderWidth(1);
                                cell.setBorderWidthTop(0);
                                cell.setBorderWidthLeft(0);
                                cell.setPadding(3f);
                                table.addCell(cell);
                            } else {
                                cell = new PdfPCell(new Phrase(mapUser.get(eventDTO1.getModifiedBy()).getFullName(), font1));
                                cell.setPadding(3f);
                                cell.setBorderWidth(1);
                                cell.setBorderWidthTop(0);
                                cell.setBorderWidthLeft(0);
                                table.addCell(cell);
                            }

                        }
                    } else {
                        if (eventDTO1.getEventStatus().code() != EventStatus.NOT_SEEN.code()) {
                            cell = new PdfPCell(new Phrase(eventDTO1.getModifiedAction(), font1));
                            cell.setBorderWidth(1);
                            cell.setBorderWidthTop(0);
                            cell.setPadding(3f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(eventDTO1.getEventStatus().description(), font1));
                            cell.setBorderWidth(1);
                            cell.setBorderWidthTop(0);
                            cell.setBorderWidthLeft(0);
                            cell.setPadding(3f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(timeFormat.format(datePasser.parse(eventDTO1.getModifiedDate())) + " - " + dateFormat.format(datePasser.parse(eventDTO1.getModifiedDate())), font1));
                            cell.setBorderWidth(1);
                            cell.setBorderWidthTop(0);
                            cell.setBorderWidthLeft(0);
                            cell.setPadding(3f);
                            table.addCell(cell);
                            cell = new PdfPCell(new Phrase(mapUser.get(eventDTO1.getModifiedBy()).getFullName(), font1));
                            cell.setBorderWidth(1);
                            cell.setBorderWidthTop(0);
                            cell.setBorderWidthLeft(0);
                            cell.setPadding(3f);
                            table.addCell(cell);
                        }

                    }

                }
                document.add(table);
                PdfPTable tableHistoryJobp = new PdfPTable(1);
                PdfPCell historyJob = new PdfPCell(new Phrase("III. Danh s??ch c??ng vi???c", boldText));
                historyJob.setHorizontalAlignment(Element.ALIGN_LEFT);
                historyJob.setBorder(Rectangle.NO_BORDER);
                historyJob.setPaddingLeft(-1f);
                tableHistoryJobp.addCell(historyJob);
                tableHistoryJobp.setHorizontalAlignment(Element.ALIGN_LEFT);
                tableHistoryJobp.setSpacingAfter(10f);
                document.add(tableHistoryJobp);
                PdfPTable tableJob = new PdfPTable(6);
                float[] widths = new float[]{3f, 3f, 3f, 3f, 2f, 3f};
                tableJob.setWidths(widths);
                cell = new PdfPCell(new Phrase("C??ng vi???c", boldText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tableJob.addCell(cell);
                cell = new PdfPCell(new Phrase("?????i", boldText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3f);
                tableJob.addCell(cell);
                cell = new PdfPCell(new Phrase("Ng?????i x??? l??", boldText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3f);
                tableJob.addCell(cell);
                cell = new PdfPCell(new Phrase("Ng??y h???t h???n", boldText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3f);
                tableJob.addCell(cell);
                cell = new PdfPCell(new Phrase("M???c ?????", boldText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3f);
                tableJob.addCell(cell);
                cell = new PdfPCell(new Phrase("Tr???ng th??i", boldText));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(3f);
                tableJob.addCell(cell);

                List<Job> jobs = jobService.findByEventIdReport(data.getParentId());
                Map<String, Unit> mapGroup = getAllGroup();
                for (Job job : jobs) {
                    String startTime;
                    String dateEnd;
                    if (job.getEndTime() != null) {
                        startTime = timeFormat.format(job.getEndTime());
                        dateEnd = " - " + dateFormat.format(job.getEndTime());
                    } else {
                        startTime = "";
                        dateEnd = "";
                    }

                    cell = new PdfPCell(new Phrase(JobType.parse(job.getJobType()).description(), font1));
                    cell.setPadding(3f);
                    tableJob.addCell(cell);
                    cell = new PdfPCell(new Phrase(mapGroup.get(job.getGroupId()).getName(), font1));
                    cell.setPadding(3f);
                    tableJob.addCell(cell);
                    if (StringUtil.isNullOrEmpty(job.getUserIds())) {
                        cell = new PdfPCell(new Phrase("", font1));
                        cell.setPadding(3f);
                        tableJob.addCell(cell);
                    } else {
                        String[] users = job.getUserIds().split(",");
                        for (String user : users) {
                            Paragraph paragraphCell = new Paragraph(mapUser.get(user).getFullName(), font1);
                            cell.setPadding(3f);
                            cell.addElement(paragraphCell);
                        }
                        tableJob.addCell(cell);
                    }
                    if (job.getEndTime() == null) {
                        cell = new PdfPCell(new Phrase("", font1));
                        cell.setPadding(3f);
                        tableJob.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Phrase(startTime + dateEnd, font1));
                        cell.setPadding(3f);
                        tableJob.addCell(cell);
                    }
                    cell = new PdfPCell(new Phrase(job.getPriority().description(), font1));
                    cell.setPadding(3f);
                    tableJob.addCell(cell);
                    cell = new PdfPCell(new Phrase(job.getStatus().description(), font1));
                    cell.setPadding(3f);
                    tableJob.addCell(cell);
                    cell = new PdfPCell();
                    cell.setColspan(6);
                    for (ActionHistory actionHistory : job.getActionHistory()) {
                        Paragraph createJob = new Paragraph(actionHistory.getActorName() + " " + actionHistory.getActionName() + " " + timeFormat.format(actionHistory.getActionTime()) + "-" + dateFormat.format(actionHistory.getActionTime()), font1);
                        cell.addElement(createJob);
                        if (!StringUtil.isNullOrEmpty(actionHistory.getContent())) {
                            org.jsoup.nodes.Document doc = Jsoup.parse(actionHistory.getContent());
                            String text = doc.text();
                            Paragraph content = new Paragraph(text, font1);
                            cell.addElement(content);
                        }
                        if (actionHistory.getActionFiles() != null && !actionHistory.getActionFiles().isEmpty()) {
                            for (File file : actionHistory.getActionFiles()) {
                                if (file.getFileType().contains("image")) {
                                    try {
                                        Image imageJob = Image.getInstance(new URL(file.getFileDownloadUri()));
                                        imageJob.scaleAbsolute(200f, 200f);
                                        imageJob.setSpacingBefore(10f);
                                        cell.addElement(imageJob);
                                    } catch (IOException ex) {
                                        Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                    }
                    cell.setPadding(3f);
                    tableJob.addCell(cell);
                }
                document.add(tableJob);
                java.io.File path = new java.io.File(urlFile);
                document.close();
                sizeFile = Files.size(pathh);
                LOGGER.info("send upload");
                String link = uploadFile(path);
                LOGGER.info("upload success");
                System.out.println(link);
                EventFile eventFile = new EventFile();
                eventFile.setSize(sizeFile.intValue());
                eventFile.setEventId(eventId);
                eventFile.setFileType("application/pdf");
                eventFile.setId(UUID.randomUUID().toString());
                eventFile.setFileName(fileName);
                eventFile.setUploadTime(now);
                eventFile.setCreateBy(uuid);
                eventFile.setCreateDate(now);
                eventFile.setStartTime(now);
                eventFile.setFileUrl(link);


                saveSaveFile(eventFile);
                Map<String, Object> param = putBodyParam(uuid, link, eventId);
                handleSendNotifyToDevice(param);
                LOGGER.info("export done");

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
//        EventFile eventFile = new EventFile(UUID.randomUUID().toString(),datePasser.parse(), violationHistory.getParentId(), "pdf", "Th??ng tin s??? vi???c", fileName, sizeFile.intValue());
        return new EventFile();
    }

    @Override
    public EventFile createFileEventInfo(String eventId, String uuid) throws Exception {
        Document document = new Document(PageSize.A4, 30f, 15f, 20f, 20f);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat datePasser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Path pathh = Paths.get(urlFile);
        Long sizeFile = new Long(0);
        try {
            EventResponseDTO eventResponseDTO = eventService.historyEvent(eventId);
            if (eventResponseDTO != null && eventResponseDTO.getData() != null && !eventResponseDTO.getData().isEmpty()) {
                Map<String, User> mapUser = getAllUser();
                List<EventDTO> eventDTO = eventResponseDTO.getData();
                EventDTO data = eventDTO.get(eventDTO.size() - 1);
                Date now = new Date();
                String fileName = "Th??ng tin s??? ki???n" + data.getKey() + ".pdf";
                String urlFile = "" + fileName;
                Path pathh = Paths.get(urlFile);
                PdfWriter.getInstance(document, new FileOutputStream(urlFile));
                document.open();
                Font font1 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font2 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font3 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font1.setSize(12);
                font2.setSize(15);
                font3.setSize(21);
                BaseFont baseFont = BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font boldText = new Font(baseFont, 12);
                Paragraph tieuDe = new Paragraph("H??? TH???NG GI??M S??T CAO T???C", font2);
                tieuDe.setAlignment(Element.ALIGN_RIGHT);
                Paragraph space = new Paragraph("         ");
                try {
                    Image image1 = Image.getInstance(new URL(caotoc));
                    image1.scaleToFit(80f, 80f);
                    image1.setAlignment(Element.ALIGN_LEFT);
                    tieuDe.add(image1);
                } catch (IOException ex) {
                    Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                Paragraph tenVanBan = new Paragraph("TH??NG TIN S??? KI???N PH??T HI???N", font3);
                tenVanBan.setAlignment(Element.ALIGN_CENTER);
                Paragraph infoEvent = new Paragraph("Th??ng tin s??? ki???n", boldText);
                Paragraph paragraph = new Paragraph("D??? li???u k???t xu???t l??c: " + timeFormat.format(now) + " - " + dateFormat.format(now), font1);
                Paragraph timeStart = new Paragraph("Th???i gian ph??t hi???n: " + timeFormat.format(datePasser.parse(data.getStartTime())) + " - " + dateFormat.format(datePasser.parse(data.getStartTime())), font1);
                Paragraph address1 = new Paragraph("V??? tr?? ph??t hi???n: " + data.getSite().getSiteName(), font1);
                Paragraph address2;
                if (data.getSiteCorrect() == null) {
                    address2 = new Paragraph("V??? tr?? ch??nh x??c: " + data.getSite().getSiteName(), font1);
                } else {
                    address2 = new Paragraph("V??? tr?? ch??nh x??c: " + data.getSiteCorrect().getSiteName(), font1);
                }

                Paragraph typeEvent = new Paragraph("Lo???i s??? ki???n: " + data.getEventName(), font1);
                Paragraph sourceName;
                if (StringUtil.isNullOrEmpty(data.getSourceName())) {
                    sourceName = new Paragraph("Ngu???n ph??t hi???n: " + data.getObjectCreate(), font1);
                } else {
                    sourceName = new Paragraph("Ngu???n ph??t hi???n: " + data.getSourceName(), font1);
                }
                Paragraph status = new Paragraph("Tr???ng th??i: " + data.getEventStatus().description(), font1);
                Paragraph image = new Paragraph("H??nh ???nh s??? ki???n: ", font1);

                document.add(tieuDe);
                document.add(space);
                document.add(tenVanBan);
                document.add(space);
                document.add(infoEvent);
                document.add(paragraph);
                document.add(timeStart);
                document.add(address1);
                document.add(address2);
                document.add(typeEvent);
                document.add(sourceName);
                document.add(status);
                document.add(image);
                document.add(space);
                String imageUrl = data.getImageUrl();
                while (imageUrl != null && imageUrl.indexOf(",") > 0) {
                    try {
                        String url = imageUrl.substring(0, imageUrl.indexOf(","));
                        Image imageEvent;
                        if (url.startsWith("http")) {
                            imageEvent = Image.getInstance(new URL(url));
                        } else {
                            imageEvent = Image.getInstance(new URL(urlImage + url));
                        }
                        imageEvent.scaleToFit(400f, 400f);
                        imageEvent.setAlignment(Element.ALIGN_CENTER);
                        document.add(imageEvent);
                        imageUrl = imageUrl.substring(imageUrl.indexOf(",") + 1);
                    } catch (IOException ex) {
                        Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (!StringUtil.isNullOrEmpty(imageUrl)) {
                    try {
                        Image imageEvent;
                        if (imageUrl.startsWith("http")) {
                            imageEvent = Image.getInstance(new URL(imageUrl));
                        } else {
                            imageEvent = Image.getInstance(new URL(urlImage + imageUrl));
                        }
                        imageEvent.scaleToFit(400f, 400f);
                        imageEvent.setAlignment(Element.ALIGN_CENTER);
                        document.add(imageEvent);
                    } catch (IOException ex) {
                        Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println(fileName);
                java.io.File path = new java.io.File(urlFile);
                document.close();
                sizeFile = Files.size(pathh);
                String link = uploadFile(path);
                System.out.println(link);
                EventFile eventFile = new EventFile();
                eventFile.setSize(sizeFile.intValue());
                eventFile.setEventId(eventId);
                eventFile.setFileType("application/pdf");
                eventFile.setId(UUID.randomUUID().toString());
                eventFile.setFileName(fileName);
                eventFile.setUploadTime(now);
                eventFile.setCreateBy(uuid);
                eventFile.setCreateDate(now);
                eventFile.setStartTime(now);
                eventFile.setFileUrl(link);
                saveSaveFile(eventFile);
                LOGGER.info("export done");

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new EventFile();
    }

    @Override
    public EventFile createReportEvent(String eventId, String uuid) throws Exception {
        Document document = new Document(PageSize.A4, 70f, 30f, 45f, 40f);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat datePasser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat time = new SimpleDateFormat("HH-mm-ss");
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Long sizeFile = new Long(0);
        try {
            Response eventInfo = eventInfoService.getEventInfoByEventId(eventId);
            if (eventInfo != null && eventInfo.getData() != null) {
                CategoryResponseDTO categoryResponseDTO = eventService.getPrinted("PRINTED");
                Category category = new Category();
                for (Category tmp : categoryResponseDTO.getData()
                ) {
                    if (tmp.getCode().equals("SIGNER")) {
                        category = tmp;
                    }
                }
                ObjectMapper mapperCrowd = new ObjectMapper();
                mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mapperCrowd.setDateFormat(df1);
                mapperCrowd.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                String message = mapperCrowd.writeValueAsString(eventInfo.getData());

                EventInfo data = mapperCrowd.readValue(message, EventInfo.class);
                Date now = new Date();
                String fileName = "B??o c??o nhanh tai n???n giao th??ng-" + data.getEventKey() + "-" + date.format(now) + " " + time.format(now) + ".pdf";
                String urlFile = "" + fileName;
                Path pathh = Paths.get(urlFile);
                PdfWriter.getInstance(document, new FileOutputStream(urlFile));
                document.open();
                Font font1 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font2 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font3 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font underline = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font12 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font12.setSize(12);
                underline.setStyle(Font.UNDERLINE);
                Font font11 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font11.setSize(11);
                Font font115 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font115.setSize(11.5f);
                font1.setSize(13);
                font2.setSize(15);
                font3.setSize(21);
                underline.setSize(13);
                BaseFont baseFont = BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font boldText = new Font(baseFont, 12);
                Font boldText11 = new Font(baseFont, 11);
                Font boldText13 = new Font(baseFont, 13);
                Font boldText14 = new Font(baseFont, 14);
                Font iText = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                iText.setSize(13);
                iText.setStyle(Font.ITALIC);
                Paragraph space = new Paragraph("  ", font1);
                document.add(space);
                float[] columnWidths = {4f, 5f};
                PdfPTable tableHistoryEvent = new PdfPTable(columnWidths);
                PdfPCell congty = new PdfPCell(new Phrase("C??NG TY CP V???N H??NH V?? B???O TR??", font12));
                congty.setHorizontalAlignment(Element.ALIGN_CENTER);
                congty.setBorder(Rectangle.NO_BORDER);
//                congty.setPaddingLeft(-1f);
                tableHistoryEvent.addCell(congty);

                PdfPCell conghoa = new PdfPCell(new Phrase("C???NG H??A X?? H???I CH??? NGH??A VI???T NAM", boldText));
                conghoa.setHorizontalAlignment(Element.ALIGN_CENTER);
                conghoa.setBorder(Rectangle.NO_BORDER);
//                conghoa.setPaddingLeft(-1f);
                tableHistoryEvent.addCell(conghoa);

                PdfPCell caotoc = new PdfPCell(new Phrase("???????NG CAO T???C VI???T NAM", font12));
                caotoc.setHorizontalAlignment(Element.ALIGN_CENTER);
                caotoc.setBorder(Rectangle.NO_BORDER);
//                caotoc.setPaddingLeft(-1f);
                tableHistoryEvent.addCell(caotoc);

                PdfPCell doclap = new PdfPCell(new Phrase("?????c l???p - T??? do - H???nh ph??c", underline));
                doclap.setHorizontalAlignment(Element.ALIGN_CENTER);
                doclap.setBorder(Rectangle.NO_BORDER);
//                doclap.setPaddingLeft(-1f);
                tableHistoryEvent.addCell(doclap);

                PdfPCell dieuhanh = new PdfPCell(new Phrase("TRUNG T??M ??I???U H??NH ???????NG", boldText13));
                dieuhanh.setHorizontalAlignment(Element.ALIGN_CENTER);
                dieuhanh.setBorder(Rectangle.NO_BORDER);
//                dieuhanh.setPaddingLeft(-1f);
                tableHistoryEvent.addCell(dieuhanh);

                PdfPCell nb = new PdfPCell(new Phrase("", boldText13));
                nb.setHorizontalAlignment(Element.ALIGN_CENTER);
                Paragraph paragraph = new Paragraph();
                nb.setBorder(Rectangle.NO_BORDER);
//                nb.setPaddingLeft(-1f);
                tableHistoryEvent.addCell(nb);
                PdfPCell under = new PdfPCell();
                Phrase phaser = new Phrase("CAO T???", boldText13);
                paragraph.add(phaser);
                Phrase phaser2 = new Phrase("C C???U GI??? - NI", underline);
                paragraph.add(phaser2);
                Phrase phaser3 = new Phrase("NH B??NH", boldText13);
                paragraph.add(phaser3);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                paragraph.setSpacingBefore(-4f);
                under.setHorizontalAlignment(Element.ALIGN_CENTER);
                under.setBorder(Rectangle.NO_BORDER);
                under.addElement(paragraph);
                under.setPaddingTop(-2f);
                tableHistoryEvent.addCell(under);
                tableHistoryEvent.addCell(nb);

                PdfPCell so = new PdfPCell(new Phrase("        S??? :          /BC-VECO&M CGNB.??VH", font115));
                so.setHorizontalAlignment(Element.ALIGN_CENTER);
                so.setBorder(Rectangle.NO_BORDER);
//                congty.setPaddingLeft(-1f);
                tableHistoryEvent.addCell(so);

                Date date1 = new Date();
                DateFormat dateDf = new SimpleDateFormat("dd");
                String dateString = "              H?? Nam, ng??y " + dateDf.format(date1);
                dateDf = new SimpleDateFormat("MM");
                dateString += " th??ng " + dateDf.format(date1);
                dateDf = new SimpleDateFormat("yyyy");
                dateString += " n??m " + dateDf.format(date1);

                PdfPCell address = new PdfPCell(new Phrase(dateString, iText));
                address.setHorizontalAlignment(Element.ALIGN_CENTER);
                address.setBorder(Rectangle.NO_BORDER);
//                conghoa.setPaddingLeft(-1f);
                tableHistoryEvent.addCell(address);

                tableHistoryEvent.addCell(nb);


                tableHistoryEvent.setHorizontalAlignment(Element.ALIGN_RIGHT);
                tableHistoryEvent.setSpacingAfter(10f);
                tableHistoryEvent.setSpacingBefore(5f);
                tableHistoryEvent.setWidthPercentage(100);
                document.add(tableHistoryEvent);
                Paragraph space1 = new Paragraph("  ", font1);
//                document.add(space1);
//                document.add(space);
                Paragraph infoEvent = new Paragraph("B??O C??O NHANH TAI N???N GIAO TH??NG", boldText14);
                infoEvent.setAlignment(Element.ALIGN_CENTER);
                document.add(infoEvent);
                document.add(space);
                int index = 0;
                int count = 0;
                String dear = data.getDear();
                float[] columnWidthDears = {4f, 5f};
                PdfPTable tableDear = new PdfPTable(columnWidthDears);
//                congty.setPaddingLeft(-1f);
                while (dear.indexOf("\n") > 0) {
                    String text = dear.substring(0, dear.indexOf("\n"));
                    text = text.trim();
                    Paragraph reason;
                    if (index == 0) {
                        PdfPCell dearCell = new PdfPCell(new Phrase("K??nh g???i:     ", font1));
                        dearCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        dearCell.setBorder(Rectangle.NO_BORDER);
//                congty.setPaddingLeft(-1f);
                        tableDear.addCell(dearCell);
                    } else {
                        PdfPCell dearCell = new PdfPCell(new Phrase("", font1));
                        dearCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        dearCell.setBorder(Rectangle.NO_BORDER);
                        tableDear.addCell(dearCell);
                    }
                    PdfPCell dearCell = new PdfPCell(new Phrase(text, font1));
                    dearCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    dearCell.setBorder(Rectangle.NO_BORDER);
                    tableDear.addCell(dearCell);

                    dear = dear.substring(dear.indexOf("\n") + 1);
                    index++;
                }
                if (index == 0) {
                    PdfPCell dearCell = new PdfPCell(new Phrase("K??nh g???i:  ", font1));
                    dearCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    dearCell.setBorder(Rectangle.NO_BORDER);
//                congty.setPaddingLeft(-1f);
                    tableDear.addCell(dearCell);
                } else {
                    PdfPCell dearCell = new PdfPCell(new Phrase("", font1));
                    dearCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    dearCell.setBorder(Rectangle.NO_BORDER);
                    tableDear.addCell(dearCell);
                }
                PdfPCell dearCell = new PdfPCell(new Phrase(dear, font1));
                dearCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                dearCell.setBorder(Rectangle.NO_BORDER);
                tableDear.addCell(dearCell);

//                Paragraph kinhgui = new Paragraph("K??nh g???i:  - C???c Qu???n l?? ???????ng b??? I;", font1);
//                kinhgui.setAlignment(Element.ALIGN_CENTER);
//                Paragraph chicuc = new Paragraph("                          - Chi c???c Qu???n l?? ???????ng b??? I.6.", font1);
//                chicuc.setAlignment(Element.ALIGN_CENTER);
                Paragraph email = new Paragraph(data.getEmail(), iText);
                email.setAlignment(Element.ALIGN_CENTER);
//                email.setPaddingTop(-15f);
                email.setSpacingBefore(-5f);
                document.add(tableDear);
                document.add(email);
                Paragraph tt = new Paragraph("       " + data.getTitle(), font1);
                tt.setAlignment(Element.ALIGN_JUSTIFIED);
                tt.setLeading(15f);
//                tt.setPaddingTop(-21f);
                tt.setSpacingBefore(5f);
//                tt.setSpacingAfter(-5f);
                document.add(tt);
                Paragraph head1 = new Paragraph("       1. Th??ng tin ph??t hi???n tai n???n:", boldText);
                head1.setAlignment(Element.ALIGN_LEFT);
                head1.setPaddingTop(5f);
                head1.setSpacingBefore(5f);
                head1.setLeading(15f);
                document.add(head1);
                Paragraph src = new Paragraph();
                src.add(new Phrase("       1.1. Ngu???n th??ng tin ph??t hi???n tai n???n:", iText));
                src.setAlignment(Element.ALIGN_LEFT);
                Phrase phaserSrc = new Phrase(" " + data.getObjectName() + ".", font1);
                src.add(phaserSrc);
                src.setPaddingTop(5f);
                src.setSpacingBefore(5f);
                src.setLeading(15f);

                document.add(src);
                Paragraph timesrc = new Paragraph();
                timesrc.add(new Phrase("       1.2. Th???i gian nh???n ???????c th??ng tin:", iText));
                timesrc.setAlignment(Element.ALIGN_LEFT);
                timesrc.setLeading(15f);
                dateDf = new SimpleDateFormat("HH");
                String timeSrc1 = dateDf.format(data.getDateReceived());
                dateDf = new SimpleDateFormat("mm");
                timeSrc1 += "h" + dateDf.format(data.getDateReceived()) + "' ng??y ";
                timeSrc1 += dateFormat.format(data.getDateReceived());
                Phrase phaserTime = new Phrase(" " + timeSrc1 + ".", font1);
                timesrc.add(phaserTime);
                timesrc.setPaddingTop(5f);
                timesrc.setSpacingBefore(5f);
                document.add(timesrc);
                Paragraph pp = new Paragraph();
                pp.add(new Phrase("       1.3. Ph????ng ph??p b??o tin:", iText));
                pp.setAlignment(Element.ALIGN_LEFT);
                if (!StringUtil.isNullOrEmpty(data.getReportMethod())) {
                    Phrase phaserpp = new Phrase(" " + data.getReportMethod() + ".", font1);
                    pp.add(phaserpp);
                }
                pp.setPaddingTop(5f);
                pp.setSpacingBefore(5f);
                pp.setLeading(15f);
                document.add(pp);

                //p2
                Paragraph head2 = new Paragraph("       2. Th???i gian, ?????a ??i???m x???y ra tai n???n:", boldText);
                head2.setAlignment(Element.ALIGN_LEFT);
                head2.setPaddingTop(5f);
                head2.setSpacingAfter(-5f);
//                head2.setSpacingBefore(5f);
//                head2.setLeading(15f);
                document.add(head2);
                dateDf = new SimpleDateFormat("HH");
                String timeEvent = dateDf.format(data.getDateReceived());
                dateDf = new SimpleDateFormat("mm");
                timeEvent += "h" + dateDf.format(data.getDateReceived()) + "' ng??y ";
                timeEvent += dateFormat.format(data.getDateReceived());
                Paragraph timeCurrent = new Paragraph("        - Th???i gian: " + timeEvent + ".", font1);
                timeCurrent.setAlignment(Element.ALIGN_LEFT);
                timeCurrent.setPaddingTop(3f);
                timeCurrent.setSpacingBefore(3f);
                document.add(timeCurrent);
                String addText = data.getSite().getSiteName();
                if (!StringUtil.isNullOrEmpty(data.getDirection())) {
                    if (data.getDirection().equals("HN-NB")) {
                        addText += " h?????ng H?? N???i - Ninh B??nh, ";
                    } else {
                        addText += " h?????ng Ninh B??nh - H?? N???i, ";
                    }
                }
                addText += "thu???c ?????a ph???n x??/th??? tr???n " + data.getSite().getWardName() + " - huy???n/th??? x?? " + data.getSite().getDistrictName() + " - ";
                addText += data.getSite().getProvinceName() + ".";
                String hn = data.getSite().getProvinceName();
//            if(hn.indexOf("N???i")>0){
//                addText+=" H?? N???i.";
//            } else {
//                addText+= data.getSite().getProvinceName();
//            }
                Paragraph addre = new Paragraph("       - ?????a ??i???m: " + addText, font1);
                addre.setAlignment(Element.ALIGN_JUSTIFIED);
                addre.setSpacingBefore(3f);
                addre.setLeading(15f);
                document.add(addre);

                //p3
                Paragraph head3 = new Paragraph("       3. Th??ng tin v??? ph????ng ti???n giao th??ng li??n quan ?????n tai n???n:", boldText);
                head3.setPaddingTop(5f);
                head3.setSpacingBefore(5f);
                head3.setAlignment(Element.ALIGN_LEFT);
                head3.setLeading(15f);
                document.add(head3);

                if (!StringUtil.isNullOrEmpty(data.getInfo())) {
                    Paragraph info = new Paragraph("         - " + data.getInfo(), font1);
                    info.setAlignment(Element.ALIGN_JUSTIFIED);
                    info.setLeading(15f);
                    document.add(info);
                }

                //p3
                Paragraph head4 = new Paragraph("       4. Thi???t h???i ban ?????u:", boldText);
                head1.setAlignment(Element.ALIGN_LEFT);
                head4.setPaddingTop(5f);
                head4.setSpacingBefore(5f);
                head4.setLeading(15f);
                document.add(head4);
                Paragraph person = new Paragraph("       4.1. V??? ng?????i: ", iText);
                person.setPaddingTop(5f);
                person.setSpacingBefore(5f);
                person.setAlignment(Element.ALIGN_LEFT);
                person.setLeading(15f);
                document.add(person);
                String textDead;
                if (data.getNumberDead() != null) {
                    if (data.getNumberDead() < 10 && data.getNumberDead() != 0) {
                        textDead = "0" + data.getNumberDead() + " ng?????i b??? ch???t.";
                    } else {
                        textDead = data.getNumberDead() + " ng?????i b??? ch???t.";
                    }
                    Paragraph personDead = new Paragraph("        - " + textDead, font1);
                    personDead.setPaddingTop(3f);
                    personDead.setSpacingBefore(3f);
                    personDead.setAlignment(Element.ALIGN_LEFT);
                    personDead.setLeading(15f);
                    document.add(personDead);
                }
                if (data.getNumberHurt() != null) {
                    String textHurt;
                    if (data.getNumberHurt() < 10 && data.getNumberHurt() != 0) {
                        textHurt = "0" + data.getNumberHurt() + " ng?????i b??? th????ng.";
                    } else {
                        textHurt = data.getNumberHurt() + " ng?????i b??? th????ng.";
                    }
                    Paragraph personHurt = new Paragraph("        - " + textHurt, font1);
                    personHurt.setAlignment(Element.ALIGN_LEFT);
                    personHurt.setPaddingTop(3f);
                    personHurt.setSpacingBefore(3f);
                    personHurt.setLeading(15f);
                    document.add(personHurt);
                }

                Paragraph vehicle = new Paragraph("       4.2. V??? ph????ng ti???n: ", iText);
                vehicle.setPaddingTop(5f);
                vehicle.setSpacingBefore(5f);
                vehicle.setAlignment(Element.ALIGN_LEFT);
                vehicle.setLeading(15f);
                document.add(vehicle);
                if (data.getVehicle() != null) {
                    for (String tmp : data.getVehicle()) {
                        if (!StringUtil.isNullOrEmpty(tmp)) {
                            Paragraph vehicleInfo = new Paragraph("        - " + tmp + ".", font1);
                            vehicleInfo.setAlignment(Element.ALIGN_JUSTIFIED);
                            vehicleInfo.setPaddingTop(3f);
                            vehicleInfo.setSpacingBefore(3f);
                            vehicleInfo.setLeading(15f);
                            document.add(vehicleInfo);
                        }
                    }
                }


                Paragraph contruction = new Paragraph("       4.3. V??? c??ng tr??nh: ", iText);
                contruction.setPaddingTop(5f);
                contruction.setSpacingBefore(5f);
                contruction.setAlignment(Element.ALIGN_JUSTIFIED);
                contruction.setLeading(15f);
                document.add(contruction);
                if (!StringUtil.isNullOrEmpty(data.getRoad())) {
                    Paragraph contructionInfo = new Paragraph("        - " + data.getRoad() + ".", font1);
                    contructionInfo.setPaddingTop(3f);
                    contructionInfo.setSpacingBefore(3f);
                    contructionInfo.setAlignment(Element.ALIGN_JUSTIFIED);
                    contructionInfo.setLeading(15f);
                    document.add(contructionInfo);
                }
                Paragraph traffic = new Paragraph("       4.4. T??nh tr???ng l??u th??ng c???a tuy???n ???????ng: ", iText);
                traffic.setPaddingTop(5f);
                traffic.setSpacingBefore(5f);
                traffic.setAlignment(Element.ALIGN_JUSTIFIED);
                traffic.setLeading(15f);
                document.add(traffic);

                if (!StringUtil.isNullOrEmpty(data.getTrafficCondition())) {
                    Paragraph trafficInfo = new Paragraph("              " + data.getTrafficCondition() + ".", font1);
                    trafficInfo.setAlignment(Element.ALIGN_JUSTIFIED);
                    trafficInfo.setLeading(15f);
                    document.add(trafficInfo);
                }
                Paragraph head5 = new Paragraph("       5. Nguy??n nh??n ban ?????u:", boldText);
                head5.setPaddingTop(5f);
                head5.setSpacingAfter(5f);
                head5.setSpacingBefore(5f);
                head5.setAlignment(Element.ALIGN_LEFT);
                head5.setLeading(15f);
                document.add(head5);
                String reasonOrigin = data.getReasonOrigin();
                if (!StringUtil.isNullOrEmpty(reasonOrigin)) {
                    while (reasonOrigin.indexOf("\n") > 0) {
                        String text = reasonOrigin.substring(0, reasonOrigin.indexOf("\n"));
                        Paragraph reason = new Paragraph("        " + text, font1);
                        reason.setAlignment(Element.ALIGN_JUSTIFIED);
                        reason.setPaddingTop(3f);
                        reason.setSpacingBefore(3f);
                        reason.setLeading(15f);
                        document.add(reason);
                        reasonOrigin = reasonOrigin.substring(reasonOrigin.indexOf("\n") + 1);
                    }
                    Paragraph reason = new Paragraph("        " + reasonOrigin, font1);
                    reason.setAlignment(Element.ALIGN_JUSTIFIED);
                    reason.setLeading(15f);
                    document.add(reason);
                }


                float[] columnWidthSign = {6f, 4f};
                PdfPTable tableDirector = new PdfPTable(columnWidthSign);
                tableDirector.setSpacingBefore(20f);
//                congty.setPaddingLeft(-1f);
                PdfPCell cellEmtry = new PdfPCell(new Phrase("", font1));
                cellEmtry.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cellEmtry.setBorder(Rectangle.NO_BORDER);
                tableDirector.addCell(cellEmtry);
                PdfPCell dearDirector = new PdfPCell(new Phrase("GI??M ?????C", boldText13));
                dearDirector.setHorizontalAlignment(Element.ALIGN_CENTER);
                dearDirector.setBorder(Rectangle.NO_BORDER);
                tableDirector.addCell(dearDirector);
                tableDirector.setWidthPercentage(100);
                document.add(tableDirector);


//                Paragraph sign = new Paragraph("GI??M ?????C                    ", boldText13);
//                sign.setAlignment(Element.ALIGN_RIGHT);
//                document.add(sign);

                Paragraph recipients = new Paragraph("N??i nh???n:", boldText13);
                recipients.setAlignment(Element.ALIGN_LEFT);
//                recipients.setSpacingAfter(2f);
//                recipients.setSpacingBefore(2f);
                document.add(recipients);
                Paragraph recipients1 = new Paragraph("        - Nh?? tr??n;", font11);
                recipients1.setAlignment(Element.ALIGN_LEFT);
                recipients.setSpacingAfter(-2f);
                recipients.setSpacingBefore(0f);
                recipients.setLeading(15f);
                document.add(recipients1);
                Paragraph recipients2 = new Paragraph("        - VEC O&M (????? b/c);", font11);
                recipients2.setAlignment(Element.ALIGN_LEFT);
                recipients2.setLeading(15f);
                recipients2.setSpacingAfter(-2f);
                recipients2.setSpacingBefore(0f);
                document.add(recipients2);
                Paragraph recipients3 = new Paragraph("        - PG?? Cty Tr???n D??ng Ti???n (????? b/c);", font11);
                recipients3.setAlignment(Element.ALIGN_LEFT);
                recipients3.setSpacingAfter(-2f);
                recipients3.setSpacingBefore(0f);
                recipients3.setLeading(15f);
                document.add(recipients3);
                Paragraph recipients4 = new Paragraph("        - VPGS CGNB;", font11);
                recipients4.setAlignment(Element.ALIGN_LEFT);
                recipients4.setSpacingAfter(-2f);
                recipients4.setSpacingBefore(0f);
                recipients4.setLeading(15f);
                document.add(recipients4);
//                Paragraph recipients5 = new Paragraph("        - L??u: VTTT.", font11);
//                recipients5.setAlignment(Element.ALIGN_LEFT);
//                recipients5.setSpacingAfter(2f);
//                recipients5.setSpacingBefore(2f);
                float[] columnWidth = {6f, 4f};
                PdfPTable tableSign = new PdfPTable(columnWidthSign);
                tableSign.setSpacingBefore(2f);
//                congty.setPaddingLeft(-1f);
                //"- L??u: VTTT."
                PdfPCell recipients5 = new PdfPCell(new Phrase("       - L??u: VTTT.", font11));
//                recipients5.setHorizontalAlignment(Element.ALIGN_LEFT);
                recipients5.setBorder(Rectangle.NO_BORDER);
                tableSign.addCell(recipients5);
                PdfPCell cellSignName = new PdfPCell(new Phrase(category.getName(), boldText13));
                cellSignName.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellSignName.setBorder(Rectangle.NO_BORDER);
                tableSign.addCell(cellSignName);
                tableSign.setWidthPercentage(100);
                document.add(tableSign);

//                Paragraph signName = new Paragraph("   "+ category.getName()+"   ", boldText13);
//                signName.setAlignment(Element.ALIGN_RIGHT);
//                document.add(signName);
//                document.add(recipients5);
                java.io.File path = new java.io.File(urlFile);
                document.close();
                sizeFile = Files.size(pathh);
                LOGGER.info("send upload");
                String link = uploadFile(path);
                LOGGER.info("upload success");
                System.out.println(link);
                EventFile eventFile = new EventFile();
                eventFile.setSize(sizeFile.intValue());
                eventFile.setEventId(eventId);
                eventFile.setFileType("application/pdf");
                eventFile.setId(UUID.randomUUID().toString());
                eventFile.setFileName("B??o c??o nhanh tai n???n giao th??ng - " + data.getEventKey() + ".pdf");
                eventFile.setUploadTime(now);
                eventFile.setCreateBy(uuid);
                eventFile.setCreateDate(now);
                eventFile.setStartTime(now);
                eventFile.setFileUrl(link);
                saveSaveFileCurent(eventFile);

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new EventFile();
    }

    @Override
    public EventFile createReportEventDaily(String fromDate, String toDate, String uuid) throws Exception {
        Document document = new Document(PageSize.A4, 40f, 40f, 45f, 40f);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat hh = new SimpleDateFormat("HH");
        DateFormat mm = new SimpleDateFormat("mm");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat datePasser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat time = new SimpleDateFormat("HH-mm-ss");
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        Long sizeFile = new Long(0);
        try {
            ReportEventDailyResponse reportEventDailyResponse = eventService.getReportDailyEvent(fromDate, toDate);
            if (reportEventDailyResponse != null && reportEventDailyResponse.getData() != null) {
                CategoryResponseDTO categoryResponseDTO = eventService.getPrinted("PRINTED");
                Category category = new Category();
                for (Category tmp : categoryResponseDTO.getData()
                ) {
                    if (tmp.getCode().equals("SIGNER")) {
                        category = tmp;
                    }
                }
                ObjectMapper mapperCrowd = new ObjectMapper();
                mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mapperCrowd.setDateFormat(df1);
                mapperCrowd.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//                String message = mapperCrowd.writeValueAsString(eventInfo.getData());

                ReportEventDaily data = reportEventDailyResponse.getData();
                Date now = new Date();
                String fileName = "B??o c??o khai th??c ng??y-" + date.format(now) + " " + time.format(now) + ".pdf";
                String urlFile = "" + fileName;
                Path pathh = Paths.get(urlFile);
                PdfWriter.getInstance(document, new FileOutputStream(urlFile));
                document.open();
                Font font16BD = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font16BD.setSize(16f);
                font16BD.setStyle(Font.UNDERLINE);
                Font lines = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                lines.setSize(50f);
                lines.setStyle(Font.UNDERLINE);
//                font16BD.setStyle(Font.BOLDITALIC);


                Font font12BD = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font12BD.setSize(12f);
                Font font12BDRed = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font12BDRed.setSize(12f);
                font12BDRed.setColor(BaseColor.RED);
                Font font12BDUnderline = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font12BDUnderline.setSize(12f);
                font12BDUnderline.setStyle(Font.UNDERLINE);
                Font font12 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font12.setSize(12);
//                Font font12 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));

                Font font1 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font2 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font3 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font underline = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));

                underline.setStyle(Font.UNDERLINE);
                Font font11 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font11.setSize(11);
                Font font115 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font115.setSize(11.5f);
                font1.setSize(12);
                font2.setSize(15);
                font3.setSize(21);
                underline.setSize(13);
                BaseFont baseFont = BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font boldText = new Font(baseFont, 12);
                Font boldText11 = new Font(baseFont, 11);
                Font boldText13 = new Font(baseFont, 13);
                Font boldText14 = new Font(baseFont, 14);
                Font iText = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                iText.setSize(13);
                iText.setStyle(Font.ITALIC);
                Paragraph space = new Paragraph("  ", font1);
                Paragraph baocao = new Paragraph("B??O C??O KHAI TH??C NG??Y", font16BD);
                baocao.setAlignment(Element.ALIGN_CENTER);
                document.add(baocao);
                document.add(space);
                Paragraph title = new Paragraph();
                Phrase reportDate = new Phrase("       NG??Y B??O C??O: ", font12BD);
                title.add(reportDate);
                Phrase reportNow = new Phrase(dateFormat.format(datePasser.parse(fromDate)), font12BDRed);
                title.add(reportNow);
                document.add(title);
                Paragraph group = new Paragraph("       ????N V??? B??O C??O: TRUNG T??M ??I???U H??NH ???????NG CAO T???C CG-NB", font12BD);
                document.add(group);

                Chunk under = new Chunk("                                                                    " +
                        "                                                             " + "       ");
                under.setUnderline(3f, -2f); //0.1 thick, -2 y-location
                Paragraph spaceUnderline = new Paragraph();
                Phrase phaser = new Phrase("       ", font12);
                spaceUnderline.add(phaser);
                spaceUnderline.add(under);
                spaceUnderline.setSpacingBefore(-13f);
                document.add(spaceUnderline);
                document.add(space);

                Paragraph head1 = new Paragraph("       I. TH??NG TIN GIAO TH??NG TR??N TUY???N", font12BD);
                document.add(head1);
                document.add(space);

                float[] columnWidths = {0.5f, 2f, 0.9f, 7f};
                PdfPTable tableEvent = new PdfPTable(columnWidths);
                PdfPCell tt = new PdfPCell(new Phrase("TT", font12BD));
                tt.setHorizontalAlignment(Element.ALIGN_CENTER);
                tt.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tt.setBackgroundColor(new BaseColor(217, 226, 243));
                tableEvent.addCell(tt);
                PdfPCell content = new PdfPCell(new Phrase("N???i dung", font12BD));
                content.setHorizontalAlignment(Element.ALIGN_CENTER);
                content.setVerticalAlignment(Element.ALIGN_MIDDLE);
                content.setPaddingBottom(5f);
                content.setBackgroundColor(new BaseColor(217, 226, 243));
                tableEvent.addCell(content);
                PdfPCell countEvent = new PdfPCell(new Phrase("S??? v???", font12BD));
                countEvent.setPadding(2f);
                countEvent.setPaddingBottom(5f);
                countEvent.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEvent.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countEvent.setBackgroundColor(new BaseColor(217, 226, 243));
                tableEvent.addCell(countEvent);
                PdfPCell infoEvent = new PdfPCell(new Phrase("?????a ??i???m, nguy??n nh??n, h???u qu???", font12BD));
                infoEvent.setPaddingBottom(5f);
//                infoEvent.setHorizontalAlignment(Element.ALIGN_CENTER);
                infoEvent.setHorizontalAlignment(Element.ALIGN_CENTER);
                infoEvent.setVerticalAlignment(Element.ALIGN_MIDDLE);
                infoEvent.setBackgroundColor(new BaseColor(217, 226, 243));
                tableEvent.addCell(infoEvent);

                java.util.List<EventInfo> accident = data.getAccidents();
                PdfPCell stt1 = new PdfPCell(new Phrase("1", font12));
                stt1.setHorizontalAlignment(Element.ALIGN_CENTER);
                stt1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                conghoa.setPaddingLeft(-1f);
                tableEvent.addCell(stt1);
                PdfPCell contentAccident = new PdfPCell(new Phrase("Tai n???n giao th??ng", font12));
//                contentAccident.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentAccident.setVerticalAlignment(Element.ALIGN_MIDDLE);
                contentAccident.setPaddingBottom(5f);
                contentAccident.setPaddingLeft(3f);

                tableEvent.addCell(contentAccident);
                String count = "00";
                if (accident != null && !accident.isEmpty()) {
                    count = accident.size() + "";
                }
                if (count.length() == 1) {
                    count = "0" + count;
                }
                PdfPCell countEventAccident = new PdfPCell(new Phrase(count, font12));
                countEventAccident.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEventAccident.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                caotoc.setPaddingLeft(-1f);
                tableEvent.addCell(countEventAccident);
                int index = 1;
                PdfPCell cell = new PdfPCell();
                for (EventInfo event : accident
                ) {
                    if (index != 1) {
                        Paragraph spaceEvent = new Paragraph("-------", font12);
                        baocao.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(spaceEvent);
                    } else {
                        index++;
                    }
                    String contentAddress = "-      ";
                    if (event.getDateReceived() != null) {
                        contentAddress += hh.format(event.getDateReceived()) + "h" + mm.format(event.getDateReceived()) + " " + event.getSite().getSiteName() + " h?????ng " + event.getDirection() + " ";
                    } else {
                        contentAddress += hh.format(event.getEventDate()) + "h" + mm.format(event.getEventDate()) + " " + event.getSite().getSiteName() + " h?????ng " + event.getDirection() + " ";
                    }
                    if (event.getContent() != null) {
                        contentAddress += event.getContent();
                    }
                    if (!contentAddress.endsWith(".")) {
                        contentAddress += ".";
                    }
                    Paragraph address = new Paragraph(contentAddress, font12);
                    address.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(address);
                    if (event.getNumberDead() != null) {
                        Paragraph number = new Paragraph("\u2022" + "      " + event.getNumberDead() + " ng?????i b??? ch???t.", font12);
                        number.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(number);
                    }
                    if (event.getNumberHurt() != null) {
                        Paragraph number = new Paragraph("\u2022" + "      " + event.getNumberHurt() + " ng?????i b??? th????ng.", font12);
                        number.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(number);
                    }
                    String road = event.getRoad();
                    if (road != null) {
                        road.trim();
                        String lowCase = road.substring(0, 1).toLowerCase();
                        road = road.substring(1);
                        road = lowCase + road;
                        Paragraph roadParagraph = new Paragraph("\u2022" + "      " + "T??i s???n ???????ng cao t???c: " + road, font12);
                        roadParagraph.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(roadParagraph);
                    }
                    String vehicle = "";
                    if (event.getVehicle() != null) {
                        for (String tmp : event.getVehicle()) {
                            if (!StringUtil.isNullOrEmpty(tmp)) {
                                tmp.trim();
                                String lowCase = tmp.substring(0, 1).toLowerCase();
                                tmp = tmp.substring(1);
                                tmp = lowCase + tmp;
                                vehicle += tmp + ", ";
                            }
                        }
                    }
                    if (!vehicle.isEmpty()) {
                        vehicle = vehicle.substring(0, vehicle.length() - 2);
                        vehicle = "\u2022" + "      Ph????ng ti???n: " + vehicle;
                        if (!vehicle.endsWith(".")) {
                            vehicle += ".";
                        }
                        Paragraph roadParagraph = new Paragraph(vehicle, font12);
                        roadParagraph.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(roadParagraph);
                    }
                    if (event.getProcess() != null) {
                        String process = event.getProcess();
                        if (!process.endsWith(".")) {
                            process += ".";
                        }
                        Paragraph roadParagraph = new Paragraph(process, font12);
                        roadParagraph.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(roadParagraph);
                    }

                    if (event.getEventDate() != null) {
                        Paragraph roadParagraph = new Paragraph("K???t th??c l??c " + hh.format(event.getDateReceived()) + "h" + mm.format(event.getDateReceived()) + ".", font12);
                        roadParagraph.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(roadParagraph);
                    }

                }
                cell.setPaddingBottom(5f);
                cell.setPaddingLeft(5f);
                tableEvent.addCell(cell);


                //th???ng s??m
                List<EventDTO> explosions = data.getExplosions();
                PdfPCell stt2 = new PdfPCell(new Phrase("2", font12));
                stt2.setHorizontalAlignment(Element.ALIGN_CENTER);
                stt2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                conghoa.setPaddingLeft(-1f);
                tableEvent.addCell(stt2);
                PdfPCell contentExplosion = new PdfPCell(new Phrase("Th???ng s??m, n??? l???p", font12));
//                contentExplosion.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentExplosion.setVerticalAlignment(Element.ALIGN_MIDDLE);
                contentExplosion.setPaddingLeft(3f);
                contentExplosion.setPaddingBottom(5f);
                tableEvent.addCell(contentExplosion);
                count = "00";
                if (explosions != null && !explosions.isEmpty()) {
                    count = explosions.size() + "";
                }
                if (count.length() == 1) {
                    count = "0" + count;
                }
                PdfPCell countEventxEplosions = new PdfPCell(new Phrase(count, font12));
                countEventxEplosions.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEventxEplosions.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                caotoc.setPaddingLeft(-1f);
                tableEvent.addCell(countEventxEplosions);
                index = 1;
                cell = new PdfPCell();
                for (EventDTO event : explosions
                ) {
                    if (index != 1) {
                        Paragraph spaceEvent = new Paragraph("-------", font12);
                        spaceEvent.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(spaceEvent);
                    } else {
                        index++;
                    }
                    String contentAddress = "\u2022      ";
                    contentAddress += hh.format(datePasser.parse(event.getStartTime())) + "h" + mm.format(datePasser.parse(event.getStartTime())) + " " + event.getSite().getSiteName() + " h?????ng " + event.getDirectionCode();
                    if (event.getNote() != null) {
                        contentAddress += " " + event.getNote();
                    }
                    if (!contentAddress.endsWith(".")) {
                        contentAddress += ".";
                    }
                    Paragraph address = new Paragraph(contentAddress, font12);
                    address.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(address);
                }
                cell.setPaddingLeft(5f);
                cell.setPaddingBottom(5f);
                tableEvent.addCell(cell);

                //h?? h???ng
                List<EventDTO> brokenVehicles = data.getBrokenVehicles();
                PdfPCell stt3 = new PdfPCell(new Phrase("3", font12));
                stt3.setHorizontalAlignment(Element.ALIGN_CENTER);
                stt3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEvent.addCell(stt3);
                PdfPCell contentBrokenVehicle = new PdfPCell(new Phrase("Ph????ng ti???n h?? h???ng", font12));
//                contentBrokenVehicle.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentBrokenVehicle.setPaddingLeft(3f);
                contentBrokenVehicle.setPaddingBottom(5f);
                contentBrokenVehicle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEvent.addCell(contentBrokenVehicle);
                count = "00";
                if (brokenVehicles != null && !brokenVehicles.isEmpty()) {
                    count = brokenVehicles.size() + "";
                }
                if (count.length() == 1) {
                    count = "0" + count;
                }
                PdfPCell countEventxBrokenVehicle = new PdfPCell(new Phrase(count, font12));
                countEventxBrokenVehicle.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEventxBrokenVehicle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEvent.addCell(countEventxBrokenVehicle);
                index = 1;
                cell = new PdfPCell();
                for (EventDTO event : brokenVehicles
                ) {
                    if (index != 1) {
                        Paragraph spaceEvent = new Paragraph("-------", font12);
                        spaceEvent.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(spaceEvent);
                    } else {
                        index++;
                    }
                    String contentAddress = "\u2022      ";
                    contentAddress += hh.format(datePasser.parse(event.getStartTime())) + "h" + mm.format(datePasser.parse(event.getStartTime())) + " " + event.getSite().getSiteName() + " h?????ng " + event.getDirectionCode();
                    if (event.getNote() != null) {
                        contentAddress += " " + event.getNote();
                    }
                    if (!contentAddress.endsWith(".")) {
                        contentAddress += ".";
                    }
                    Paragraph address = new Paragraph(contentAddress, font12);
                    address.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(address);
                }
                cell.setPaddingLeft(5f);
                cell.setPaddingBottom(5f);
                tableEvent.addCell(cell);

                //???????ng c???m
                //h?? h???ng
                List<EventDTO> noEntrys = data.getNoEntry();
                PdfPCell stt4 = new PdfPCell(new Phrase("4", font12));
                stt4.setHorizontalAlignment(Element.ALIGN_CENTER);
                stt4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEvent.addCell(stt4);
                PdfPCell contentNoEntry = new PdfPCell(new Phrase("Xe ?????p, xe m??y, xe 3 b??nh l??u th??ng tr??n tuy???n", font12));
//                contentNoEntry.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentNoEntry.setPaddingLeft(3f);
                contentNoEntry.setPaddingBottom(5f);
                contentNoEntry.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEvent.addCell(contentNoEntry);
                count = "00";
                if (noEntrys != null && !noEntrys.isEmpty()) {
                    count = noEntrys.size() + "";
                }
                if (count.length() == 1) {
                    count = "0" + count;
                }
                PdfPCell countEventxNoEntrys = new PdfPCell(new Phrase(count, font12));
                countEventxNoEntrys.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEventxNoEntrys.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEvent.addCell(countEventxNoEntrys);
                index = 1;
                cell = new PdfPCell();
                for (EventDTO event : noEntrys
                ) {
                    if (index != 1) {
                        Paragraph spaceEvent = new Paragraph("-------", font12);
                        spaceEvent.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(spaceEvent);
                    } else {
                        index++;
                    }
                    String contentAddress = "\u2022      ";
                    contentAddress += hh.format(datePasser.parse(event.getStartTime())) + "h" + mm.format(datePasser.parse(event.getStartTime())) + " " + event.getSite().getSiteName() + " h?????ng " + event.getDirectionCode();
                    if (event.getNote() != null) {
                        contentAddress += " " + event.getNote();
                    }
                    if (!contentAddress.endsWith(".")) {
                        contentAddress += ".";
                    }
                    Paragraph address = new Paragraph(contentAddress, font12);
                    address.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(address);
                }
                cell.setPaddingBottom(5f);
                cell.setPaddingLeft(5f);
                tableEvent.addCell(cell);

                //S??? c??? kh??c
                List<EventInfo> others = data.getEventOther();
                PdfPCell stt5 = new PdfPCell(new Phrase("5", font12));
                stt5.setHorizontalAlignment(Element.ALIGN_CENTER);
                stt5.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                conghoa.setPaddingLeft(-1f);
                tableEvent.addCell(stt5);
                PdfPCell contentOther = new PdfPCell(new Phrase("S??? c??? kh??c", font12));
//                contentOther.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentOther.setPaddingLeft(3f);
                contentOther.setPaddingBottom(5f);
                contentOther.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableEvent.addCell(contentOther);
                count = "00";
                if (others != null && !others.isEmpty()) {
                    count = others.size() + "";
                }
                if (count.length() == 1) {
                    count = "0" + count;
                }
                PdfPCell countEventOthers = new PdfPCell(new Phrase(count, font12));
                countEventOthers.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEventOthers.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                caotoc.setPaddingLeft(-1f);
                tableEvent.addCell(countEventOthers);
                index = 1;
                cell = new PdfPCell();
                for (EventInfo event : others
                ) {
                    if (index != 1) {
                        Paragraph spaceEvent = new Paragraph("-------", font12);
                        baocao.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(spaceEvent);
                    } else {
                        index++;
                    }
                    String contentAddress = "-      ";
                    if (event.getDateReceived() != null) {
                        contentAddress += hh.format(event.getDateReceived()) + "h" + mm.format(event.getDateReceived()) + " " + event.getSite().getSiteName() + " h?????ng " + event.getDirection();
                    } else {
                        contentAddress += hh.format(event.getEventDate()) + "h" + mm.format(event.getEventDate()) + " " + event.getSite().getSiteName() + " h?????ng " + event.getDirection();
                    }
                    if (event.getContent() != null) {
                        contentAddress += " " + event.getContent();
                    }
                    if (!contentAddress.endsWith(".")) {
                        contentAddress += ".";
                    }
                    Paragraph address = new Paragraph(contentAddress, font12);
                    address.setAlignment(Element.ALIGN_LEFT);
                    cell.addElement(address);

                    String road = event.getRoad();
                    if (road != null) {
                        road.trim();
                        String lowCase = road.substring(0, 1).toLowerCase();
                        road = road.substring(1);
                        road = lowCase + road;
                        Paragraph roadParagraph = new Paragraph("\u2022" + "      " + "T??i s???n ???????ng cao t???c: " + road, font12);
                        roadParagraph.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(roadParagraph);
                    }
                    String vehicle = "";
                    if (event.getVehicle() != null) {
                        for (String tmp : event.getVehicle()) {
                            if (!StringUtil.isNullOrEmpty(tmp)) {
                                tmp.trim();
                                String lowCase = tmp.substring(0, 1).toLowerCase();
                                tmp = tmp.substring(1);
                                tmp = lowCase + tmp;
                                vehicle += tmp + ", ";
                            }
                        }
                    }
                    if (!vehicle.isEmpty()) {
                        vehicle = vehicle.substring(0, vehicle.length() - 2);
                        vehicle = "\u2022" + "      Ph????ng ti???n: " + vehicle;
                        if (!vehicle.endsWith(".")) {
                            vehicle += ".";
                        }
                        Paragraph roadParagraph = new Paragraph(vehicle, font12);
                        roadParagraph.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(roadParagraph);
                    }
                    if (event.getProcess() != null) {
                        String process = event.getProcess();
                        if (!process.endsWith(".")) {
                            process += ".";
                        }
                        Paragraph roadParagraph = new Paragraph(process, font12);
                        roadParagraph.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(roadParagraph);
                    }

                    if (event.getEndTime() != null) {
                        Paragraph roadParagraph = new Paragraph("K???t th??c l??c " + hh.format(event.getEndTime()) + "h" + mm.format(event.getEndTime()) + ".", font12);
                        roadParagraph.setAlignment(Element.ALIGN_LEFT);
                        cell.addElement(roadParagraph);
                    }

                }
                int countDays = 0;
                if (accident != null && !accident.isEmpty()) {
                    countDays += accident.size();
                }
                if (explosions != null && !explosions.isEmpty()) {
                    countDays += explosions.size();
                }
                if (brokenVehicles != null && !brokenVehicles.isEmpty()) {
                    countDays += brokenVehicles.size();
                }
                if (noEntrys != null && !noEntrys.isEmpty()) {
                    countDays += noEntrys.size();
                }
                if (others != null && !others.isEmpty()) {
                    countDays += others.size();
                }


                cell.setPaddingLeft(5f);
                cell.setPaddingBottom(5f);
                tableEvent.addCell(cell);
                PdfPCell countDayTitle = new PdfPCell(new Phrase(" T???ng c???ng ng??y", font12BD));
                countDayTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                countDayTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countDayTitle.setPaddingBottom(7f);
                countDayTitle.setBackgroundColor(new BaseColor(237, 237, 237));
                countDayTitle.setColspan(2);
                tableEvent.addCell(countDayTitle);

                PdfPCell countDay = new PdfPCell(new Phrase("" + countDays, font12BD));
                countDay.setHorizontalAlignment(Element.ALIGN_CENTER);
                countDay.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countDay.setBackgroundColor(new BaseColor(237, 237, 237));
                countDay.setColspan(2);
                tableEvent.addCell(countDay);

                PdfPCell countMonthTitle = new PdfPCell(new Phrase(" L??y k??? th??ng", font12BD));
                countMonthTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                countMonthTitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countMonthTitle.setPaddingBottom(7f);
                countMonthTitle.setBackgroundColor(new BaseColor(222, 234, 246));
                countMonthTitle.setColspan(2);
                tableEvent.addCell(countMonthTitle);

                PdfPCell countMonth = new PdfPCell(new Phrase("" + data.getCountMonth(), font12BD));
                countMonth.setHorizontalAlignment(Element.ALIGN_CENTER);
                countMonth.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countMonth.setBackgroundColor(new BaseColor(222, 234, 246));
                countMonth.setColspan(2);
                tableEvent.addCell(countMonth);
                tableEvent.setWidthPercentage(100);
                document.add(tableEvent);

//                Paragraph spaceUnderline = new Paragraph("                                                                               ", lines);
//                spaceUnderline.setAlignment(Element.ALIGN_LEFT);
//                spaceUnderline.setSpacingBefore(-50f);
//                document.add(spaceUnderline);

                Paragraph spaceUnderline2 = new Paragraph();
                spaceUnderline2.add(phaser);

                spaceUnderline2.add(under);
                spaceUnderline2.setSpacingBefore(-3f);
                document.add(spaceUnderline2);

                //II Th??ng tin hotline
                document.add(space);
                Paragraph head2 = new Paragraph("       II. TH??NG TIN HOTLINE", font12BD);
                document.add(head2);
                document.add(space);

                float[] columnWidthHotlines = {0.5f, 1.5f, 0.7f, 1.5f, 4f, 3f};
                PdfPTable tableHotline = new PdfPTable(columnWidthHotlines);
                PdfPCell ttHotline = new PdfPCell(new Phrase("TT", font12BD));
                ttHotline.setHorizontalAlignment(Element.ALIGN_CENTER);
                ttHotline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                ttHotline.setBackgroundColor(new BaseColor(217, 226, 243));
                tableHotline.addCell(ttHotline);
                PdfPCell contentHotline = new PdfPCell(new Phrase("N???i dung", font12BD));
                contentHotline.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentHotline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                contentHotline.setBackgroundColor(new BaseColor(217, 226, 243));
                tableHotline.addCell(contentHotline);
                PdfPCell callNumber = new PdfPCell(new Phrase("S??? cu???c g???i", font12BD));
                callNumber.setPadding(2f);
                callNumber.setPaddingBottom(5f);
                callNumber.setHorizontalAlignment(Element.ALIGN_CENTER);
                callNumber.setVerticalAlignment(Element.ALIGN_MIDDLE);
                callNumber.setBackgroundColor(new BaseColor(217, 226, 243));
                tableHotline.addCell(callNumber);
                PdfPCell phoneNumber = new PdfPCell(new Phrase("S??? ??T", font12BD));
                phoneNumber.setPaddingBottom(5f);
//                infoEvent.setHorizontalAlignment(Element.ALIGN_CENTER);
                phoneNumber.setHorizontalAlignment(Element.ALIGN_CENTER);
                phoneNumber.setVerticalAlignment(Element.ALIGN_MIDDLE);
                phoneNumber.setBackgroundColor(new BaseColor(217, 226, 243));
                tableHotline.addCell(phoneNumber);
                PdfPCell callContent = new PdfPCell(new Phrase("N???i dung cu???c g???i", font12BD));
                callContent.setPaddingBottom(5f);
//                infoEvent.setHorizontalAlignment(Element.ALIGN_CENTER);
                callContent.setHorizontalAlignment(Element.ALIGN_CENTER);
                callContent.setVerticalAlignment(Element.ALIGN_MIDDLE);
                callContent.setBackgroundColor(new BaseColor(217, 226, 243));
                tableHotline.addCell(callContent);
                PdfPCell process = new PdfPCell(new Phrase("Trung t??m x??? l??", font12BD));
                process.setPaddingBottom(5f);
//                infoEvent.setHorizontalAlignment(Element.ALIGN_CENTER);
                process.setHorizontalAlignment(Element.ALIGN_CENTER);
                process.setVerticalAlignment(Element.ALIGN_MIDDLE);
                process.setBackgroundColor(new BaseColor(217, 226, 243));
                tableHotline.addCell(process);

                HotlineReportResponseDTO hotlineDTOS = hotlineService.getReport(fromDate, toDate);
                Hotline hotline = hotlineDTOS.getData();
                List<HotlineDTO> security = hotline.getSecurity();
                Integer countRow = 1;
                if (security != null && !security.isEmpty()) {
                    countRow = security.size();
                }
                PdfPCell sttHotline = new PdfPCell(new Phrase("1", font12));
                sttHotline.setHorizontalAlignment(Element.ALIGN_CENTER);
                sttHotline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    sttHotline.setRowspan(countRow);
                }
//                conghoa.setPaddingLeft(-1f);
                tableHotline.addCell(sttHotline);
                PdfPCell contentSecurity = new PdfPCell(new Phrase("S??? c???", font12));
                contentSecurity.setPaddingLeft(3f);
                contentSecurity.setPaddingBottom(5f);
//                contentSecurity.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentSecurity.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    contentSecurity.setRowspan(countRow);
                }
                tableHotline.addCell(contentSecurity);
                count = "00";
                if (security != null && !security.isEmpty()) {
                    count = security.size() + "";
                }
                if (count.length() == 1) {
                    count = "0" + count;
                }
                PdfPCell countEventSecurity = new PdfPCell(new Phrase(count, font12));
                countEventSecurity.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEventSecurity.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    countEventSecurity.setRowspan(countRow);
                }
//                caotoc.setPaddingLeft(-1f);
                tableHotline.addCell(countEventSecurity);
                index = 1;
                if (security != null && !security.isEmpty()) {
                    for (HotlineDTO item : security
                    ) {
                        String phoneNumberTmp = "Kh??ng hi???n s???";
                        if (item.getPhoneNumber() != null && !item.getPhoneNumber().isEmpty()) {
                            phoneNumberTmp = item.getPhoneNumber();
                        }
                        PdfPCell phoneNumberCell = new PdfPCell(new Phrase(phoneNumberTmp, font12));
                        phoneNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        phoneNumberCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        phoneNumberCell.setPaddingBottom(5f);
                        tableHotline.addCell(phoneNumberCell);

                        String phoneContentTmp = "";
                        if (item.getContent() != null && !item.getContent().isEmpty()) {
                            phoneContentTmp = item.getContent();
                        }
                        PdfPCell phoneContentCell = new PdfPCell(new Phrase(phoneContentTmp, font12));
                        phoneContentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        phoneContentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        phoneContentCell.setPaddingBottom(5f);
                        tableHotline.addCell(phoneContentCell);

                        String processTmp = "";
                        if (item.getProcess() != null && !item.getProcess().isEmpty()) {
                            processTmp = item.getProcess();
                        }
                        PdfPCell processCell = new PdfPCell(new Phrase(processTmp, font12));
                        processCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        processCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        processCell.setPaddingBottom(5f);
                        tableHotline.addCell(processCell);

                    }

                } else {
                    PdfPCell tmt = new PdfPCell(new Phrase("", font12));
                    tmt.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt);
                    PdfPCell tmt1 = new PdfPCell(new Phrase("", font12));
                    tmt1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt1);
                    PdfPCell tmt2 = new PdfPCell(new Phrase("", font12));
                    tmt2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt2);
                }

                //h?????ng d???n giao th??ng
                List<HotlineDTO> trafficInstructions = hotline.getTrafficIntructions();
                countRow = 1;
                if (trafficInstructions != null && !trafficInstructions.isEmpty()) {
                    countRow = trafficInstructions.size();
                }
                PdfPCell sttTraffic = new PdfPCell(new Phrase("2", font12));
                sttTraffic.setHorizontalAlignment(Element.ALIGN_CENTER);
                sttTraffic.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    sttTraffic.setRowspan(countRow);
                }
//                conghoa.setPaddingLeft(-1f);
                tableHotline.addCell(sttTraffic);
                PdfPCell contentTrafficInstruction = new PdfPCell(new Phrase("H?????ng d???n giao th??ng", font12));
                contentTrafficInstruction.setPaddingLeft(3f);
                contentTrafficInstruction.setPaddingBottom(5f);
//                contentTrafficInstruction.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentTrafficInstruction.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    contentTrafficInstruction.setRowspan(countRow);
                }
                tableHotline.addCell(contentTrafficInstruction);
                count = "00";
                if (trafficInstructions != null && !trafficInstructions.isEmpty()) {
                    count = trafficInstructions.size() + "";
                }
                if (count.length() == 1) {
                    count = "0" + count;
                }
                PdfPCell countEventTrafficInstruction = new PdfPCell(new Phrase(count, font12));
                countEventTrafficInstruction.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEventTrafficInstruction.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    countEventTrafficInstruction.setRowspan(countRow);
                }
//                caotoc.setPaddingLeft(-1f);
                tableHotline.addCell(countEventTrafficInstruction);
                index = 1;
                if (trafficInstructions != null && !trafficInstructions.isEmpty()) {
                    for (HotlineDTO item : trafficInstructions
                    ) {
                        String phoneNumberTmp = "Kh??ng hi???n s???";
                        if (item.getPhoneNumber() != null && !item.getPhoneNumber().isEmpty()) {
                            phoneNumberTmp = item.getPhoneNumber();
                        }
                        PdfPCell phoneNumberCell = new PdfPCell(new Phrase(phoneNumberTmp, font12));
                        phoneNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        phoneNumberCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        phoneNumberCell.setPaddingBottom(5f);
                        tableHotline.addCell(phoneNumberCell);

                        String phoneContentTmp = "";
                        if (item.getContent() != null && !item.getContent().isEmpty()) {
                            phoneContentTmp = item.getContent();
                        }
                        PdfPCell phoneContentCell = new PdfPCell(new Phrase(phoneContentTmp, font12));
                        phoneContentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        phoneContentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        phoneContentCell.setPaddingBottom(5f);
                        tableHotline.addCell(phoneContentCell);

                        String processTmp = "";
                        if (item.getProcess() != null && !item.getProcess().isEmpty()) {
                            processTmp = item.getProcess();
                        }
                        PdfPCell processCell = new PdfPCell(new Phrase(processTmp, font12));
                        processCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        processCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        processCell.setPaddingBottom(5f);
                        tableHotline.addCell(processCell);

                    }

                } else {
                    PdfPCell tmt = new PdfPCell(new Phrase("", font12));
                    tmt.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt);
                    PdfPCell tmt1 = new PdfPCell(new Phrase("", font12));
                    tmt1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt1);
                    PdfPCell tmt2 = new PdfPCell(new Phrase("", font12));
                    tmt2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt2);
                }

                //Ch???t l?????ng d???ch v???
                List<HotlineDTO> serviceQualitys = hotline.getServiceQuality();
                countRow = 1;
                if (serviceQualitys != null && !serviceQualitys.isEmpty()) {
                    countRow = serviceQualitys.size();
                }
                PdfPCell sttServiceQuality = new PdfPCell(new Phrase("3", font12));
                sttServiceQuality.setHorizontalAlignment(Element.ALIGN_CENTER);
                sttServiceQuality.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    sttServiceQuality.setRowspan(countRow);
                }
//                conghoa.setPaddingLeft(-1f);
                tableHotline.addCell(sttServiceQuality);
                PdfPCell contentServiceQuality = new PdfPCell(new Phrase("Ch???t l?????ng d???ch v??? ??CT", font12));
                contentServiceQuality.setPaddingLeft(3f);
                contentServiceQuality.setPaddingBottom(5f);
//                contentServiceQuality.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentServiceQuality.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    contentServiceQuality.setRowspan(countRow);
                }
                tableHotline.addCell(contentServiceQuality);
                count = "00";
                if (serviceQualitys != null && !serviceQualitys.isEmpty()) {
                    count = serviceQualitys.size() + "";
                }
                if (count.length() == 1) {
                    count = "0" + count;
                }
                PdfPCell countEventServiceQuality = new PdfPCell(new Phrase(count, font12));
                countEventServiceQuality.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEventServiceQuality.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    countEventServiceQuality.setRowspan(countRow);
                }
//                caotoc.setPaddingLeft(-1f);
                tableHotline.addCell(countEventServiceQuality);
                index = 1;
                if (serviceQualitys != null && !serviceQualitys.isEmpty()) {
                    for (HotlineDTO item : serviceQualitys
                    ) {
                        String phoneNumberTmp = "Kh??ng hi???n s???";
                        if (item.getPhoneNumber() != null && !item.getPhoneNumber().isEmpty()) {
                            phoneNumberTmp = item.getPhoneNumber();
                        }
                        PdfPCell phoneNumberCell = new PdfPCell(new Phrase(phoneNumberTmp, font12));
                        phoneNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        phoneNumberCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        phoneNumberCell.setPaddingBottom(5f);
                        tableHotline.addCell(phoneNumberCell);

                        String phoneContentTmp = "";
                        if (item.getContent() != null && !item.getContent().isEmpty()) {
                            phoneContentTmp = item.getContent();
                        }
                        PdfPCell phoneContentCell = new PdfPCell(new Phrase(phoneContentTmp, font12));
                        phoneContentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        phoneContentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        phoneContentCell.setPaddingBottom(5f);
                        tableHotline.addCell(phoneContentCell);

                        String processTmp = "";
                        if (item.getProcess() != null && !item.getProcess().isEmpty()) {
                            processTmp = item.getProcess();
                        }
                        PdfPCell processCell = new PdfPCell(new Phrase(processTmp, font12));
                        processCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        processCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        processCell.setPaddingBottom(5f);
                        tableHotline.addCell(processCell);

                    }

                } else {
                    PdfPCell tmt = new PdfPCell(new Phrase("", font12));
                    tmt.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt);
                    PdfPCell tmt1 = new PdfPCell(new Phrase("", font12));
                    tmt1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt1);
                    PdfPCell tmt2 = new PdfPCell(new Phrase("", font12));
                    tmt2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt2);
                }

                //C??c th??ng tin kh??c
                List<HotlineDTO> otherInfos = hotline.getOtherInfo();
                countRow = 1;
                if (otherInfos != null && !otherInfos.isEmpty()) {
                    countRow = otherInfos.size();
                }
                PdfPCell sttOtherInfo = new PdfPCell(new Phrase("4", font12));
                sttOtherInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
                sttOtherInfo.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    sttOtherInfo.setRowspan(countRow);
                }
//                conghoa.setPaddingLeft(-1f);
                tableHotline.addCell(sttOtherInfo);
                PdfPCell contentOtherInfo = new PdfPCell(new Phrase("C??c th??ng tin kh??c", font12));
                contentOtherInfo.setPaddingLeft(3f);
                contentOtherInfo.setPaddingBottom(5f);
//                contentOtherInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentOtherInfo.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    contentOtherInfo.setRowspan(countRow);
                }
                tableHotline.addCell(contentOtherInfo);
                count = "00";
                if (otherInfos != null && !otherInfos.isEmpty()) {
                    count = otherInfos.size() + "";
                }
                if (count.length() == 1) {
                    count = "0" + count;
                }
                PdfPCell countEventOtherInfo = new PdfPCell(new Phrase(count, font12));
                countEventOtherInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
                countEventOtherInfo.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (countRow != 1) {
                    countEventOtherInfo.setRowspan(countRow);
                }
//                caotoc.setPaddingLeft(-1f);
                tableHotline.addCell(countEventOtherInfo);
                index = 1;
                if (otherInfos != null && !otherInfos.isEmpty()) {
                    for (HotlineDTO item : otherInfos
                    ) {
                        String phoneNumberTmp = "Kh??ng hi???n s???";
                        if (item.getPhoneNumber() != null && !item.getPhoneNumber().isEmpty()) {
                            phoneNumberTmp = item.getPhoneNumber();
                        }
                        PdfPCell phoneNumberCell = new PdfPCell(new Phrase(phoneNumberTmp, font12));
                        phoneNumberCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        phoneNumberCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        phoneNumberCell.setPaddingBottom(5f);
                        tableHotline.addCell(phoneNumberCell);

                        String phoneContentTmp = "";
                        if (item.getContent() != null && !item.getContent().isEmpty()) {
                            phoneContentTmp = item.getContent();
                        }
                        PdfPCell phoneContentCell = new PdfPCell(new Phrase(phoneContentTmp, font12));
                        phoneContentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        phoneContentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        phoneContentCell.setPaddingBottom(5f);
                        tableHotline.addCell(phoneContentCell);

                        String processTmp = "";
                        if (item.getProcess() != null && !item.getProcess().isEmpty()) {
                            processTmp = item.getProcess();
                        }
                        PdfPCell processCell = new PdfPCell(new Phrase(processTmp, font12));
                        processCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        processCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        processCell.setPaddingBottom(5f);
                        tableHotline.addCell(processCell);

                    }

                } else {
                    PdfPCell tmt = new PdfPCell(new Phrase("", font12));
                    tmt.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt);
                    PdfPCell tmt1 = new PdfPCell(new Phrase("", font12));
                    tmt1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt1);
                    PdfPCell tmt2 = new PdfPCell(new Phrase("", font12));
                    tmt2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tmt2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableHotline.addCell(tmt2);
                }

                countDays = 0;
                if (security != null && !security.isEmpty()) {
                    countDays += security.size();
                }
                if (trafficInstructions != null && !trafficInstructions.isEmpty()) {
                    countDays += trafficInstructions.size();
                }
                if (serviceQualitys != null && !serviceQualitys.isEmpty()) {
                    countDays += serviceQualitys.size();
                }
                if (otherInfos != null && !otherInfos.isEmpty()) {
                    countDays += otherInfos.size();
                }

                PdfPCell countDayTitleHotline = new PdfPCell(new Phrase(" T???ng c???ng ng??y", font12BD));
                countDayTitleHotline.setHorizontalAlignment(Element.ALIGN_CENTER);
                countDayTitleHotline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countDayTitleHotline.setPaddingBottom(7f);
                countDayTitleHotline.setBackgroundColor(new BaseColor(237, 237, 237));
                countDayTitleHotline.setColspan(2);
                tableHotline.addCell(countDayTitleHotline);

                PdfPCell countDayHotline = new PdfPCell(new Phrase("" + countDays, font12BD));
                countDayHotline.setHorizontalAlignment(Element.ALIGN_CENTER);
                countDayHotline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countDayHotline.setBackgroundColor(new BaseColor(237, 237, 237));
                countDayHotline.setColspan(4);
                tableHotline.addCell(countDayHotline);

                PdfPCell countMonthTitleHotline = new PdfPCell(new Phrase(" L??y k??? th??ng", font12BD));
                countMonthTitleHotline.setHorizontalAlignment(Element.ALIGN_CENTER);
                countMonthTitleHotline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countMonthTitleHotline.setPaddingBottom(7f);
                countMonthTitleHotline.setBackgroundColor(new BaseColor(222, 234, 246));
                countMonthTitleHotline.setColspan(2);
                tableHotline.addCell(countMonthTitleHotline);

                PdfPCell countMonthHotline = new PdfPCell(new Phrase("" + data.getCountMonth(), font12BD));
                countMonthHotline.setHorizontalAlignment(Element.ALIGN_CENTER);
                countMonthHotline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countMonthHotline.setBackgroundColor(new BaseColor(222, 234, 246));
                countMonthHotline.setColspan(4);
                tableHotline.addCell(countMonthHotline);


                tableHotline.setWidthPercentage(100);
                document.add(tableHotline);

                Paragraph spaceUnderline3 = new Paragraph();
                spaceUnderline3.add(phaser);

                spaceUnderline3.add(under);
                spaceUnderline3.setSpacingBefore(-3f);
                document.add(spaceUnderline3);


//T??nh tr???ng h??? th???ng
                document.add(space);
                Paragraph head3 = new Paragraph("       III. T??NH TR???NG H??? TH???NG THI???T B??? M??Y M??C", font12BD);
                document.add(head3);
                document.add(space);

                float[] columnWidthReportDevices = {1f, 3.5f, 3f, 4f};
                PdfPTable tableReportDevice = new PdfPTable(columnWidthReportDevices);
                PdfPCell ttReportDevice = new PdfPCell(new Phrase("TT", font12BD));
                ttReportDevice.setHorizontalAlignment(Element.ALIGN_CENTER);
                ttReportDevice.setVerticalAlignment(Element.ALIGN_MIDDLE);
                ttReportDevice.setPaddingBottom(5f);
                ttReportDevice.setBackgroundColor(new BaseColor(217, 226, 243));
                tableReportDevice.addCell(ttReportDevice);
                PdfPCell contentDevice = new PdfPCell(new Phrase("Thi???t b??? m??y m??c", font12BD));
                contentDevice.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentDevice.setVerticalAlignment(Element.ALIGN_MIDDLE);
                contentDevice.setPaddingBottom(5f);
                contentDevice.setBackgroundColor(new BaseColor(217, 226, 243));
                tableReportDevice.addCell(contentDevice);
                PdfPCell offline = new PdfPCell(new Phrase("Kh??ng ho???t d???ng", font12BD));
//                offline.setPadding(2f);
                offline.setPaddingBottom(5f);
                offline.setHorizontalAlignment(Element.ALIGN_CENTER);
                offline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                offline.setBackgroundColor(new BaseColor(217, 226, 243));
                tableReportDevice.addCell(offline);
                PdfPCell online = new PdfPCell(new Phrase("Ho???t ?????ng", font12BD));
                online.setPaddingBottom(5f);
//                infoEvent.setHorizontalAlignment(Element.ALIGN_CENTER);
                online.setHorizontalAlignment(Element.ALIGN_CENTER);
                online.setVerticalAlignment(Element.ALIGN_MIDDLE);
                online.setBackgroundColor(new BaseColor(217, 226, 243));
                tableReportDevice.addCell(online);

                DeviceResponseDTO deviceResponseDTO = reportDeviceService.getReport(fromDate, toDate);
                ReportDevice reportDevice = deviceResponseDTO.getData();
                ReportDeviceDTO ptz = reportDevice.getPtz();
                PdfPCell sttPtz = new PdfPCell(new Phrase("1", font12));
                sttPtz.setPaddingBottom(5f);
                sttPtz.setHorizontalAlignment(Element.ALIGN_CENTER);
                sttPtz.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(sttPtz);

                PdfPCell contentPtz = new PdfPCell(new Phrase("Camera quan s??t", font12));
                contentPtz.setPaddingLeft(3f);
                contentPtz.setPaddingBottom(2f);
//                contentSecurity.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentPtz.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(contentPtz);
                if (ptz != null) {

                    count = "00";
                    if (ptz.getOffline() != null) {
                        count = ptz.getOffline() + "";
                    }
                    if (count.length() == 1) {
                        count = "0" + count;
                    }
                    PdfPCell countOfflinePtz = new PdfPCell(new Phrase(count, font12));
                    countOfflinePtz.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflinePtz.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOfflinePtz);

                    count = "00";
                    if (ptz.getOnline() != null) {
                        count = ptz.getOnline() + "";
                    }
                    if (count.length() == 1) {
                        count = "0" + count;
                    }
                    PdfPCell countOnlinePtz = new PdfPCell(new Phrase(count, font12));
                    countOnlinePtz.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlinePtz.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOnlinePtz);
                } else {
                    PdfPCell countOfflinePtz = new PdfPCell(new Phrase("", font12));
                    countOfflinePtz.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflinePtz.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOfflinePtz);

                    PdfPCell countOnlinePtz = new PdfPCell(new Phrase("", font12));
                    countOnlinePtz.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlinePtz.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOnlinePtz);
                }

                ReportDeviceDTO camera = reportDevice.getCamera();
                PdfPCell sttCamera = new PdfPCell(new Phrase("2", font12));
                sttCamera.setHorizontalAlignment(Element.ALIGN_CENTER);
                sttCamera.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(sttCamera);

                PdfPCell contentCamera = new PdfPCell(new Phrase("Camera d?? xe", font12));
                contentCamera.setPaddingLeft(3f);
                contentCamera.setPaddingBottom(5f);
//                contentSecurity.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentCamera.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(contentCamera);
                if (camera != null) {
                    count = "00";
                    if (camera.getOffline() != null) {
                        count = camera.getOffline() + "";
                    }
                    if (count.length() == 1) {
                        count = "0" + count;
                    }
                    PdfPCell countOfflineCamera = new PdfPCell(new Phrase(count, font12));
                    countOfflineCamera.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflineCamera.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOfflineCamera);
                    count = "00";
                    if (camera.getOnline() != null) {
                        count = camera.getOnline() + "";
                    }
                    if (count.length() == 1) {
                        count = "0" + count;
                    }
                    PdfPCell countOnlineCamera = new PdfPCell(new Phrase(count, font12));
                    countOnlineCamera.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlineCamera.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOnlineCamera);
                } else {
                    PdfPCell countOfflineCamera = new PdfPCell(new Phrase("", font12));
                    countOfflineCamera.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflineCamera.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOfflineCamera);

                    PdfPCell countOnlineCamera = new PdfPCell(new Phrase("", font12));
                    countOnlineCamera.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlineCamera.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOnlineCamera);
                }

                ReportDeviceDTO vsmBoard = reportDevice.getVsmBoard();
                PdfPCell sttVsmBoard = new PdfPCell(new Phrase("3", font12));
                sttVsmBoard.setHorizontalAlignment(Element.ALIGN_CENTER);
                sttVsmBoard.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(sttVsmBoard);

                PdfPCell contentVsmBoard = new PdfPCell(new Phrase("Bi???n b??o ??i???n t???", font12));
                contentVsmBoard.setPaddingLeft(3f);
                contentVsmBoard.setPaddingBottom(5f);
//                contentSecurity.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentVsmBoard.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(contentVsmBoard);
                if (vsmBoard != null) {
                    count = "00";
                    if (vsmBoard.getOffline() != null) {
                        count = vsmBoard.getOffline() + "";
                    }
                    if (count.length() == 1) {
                        count = "0" + count;
                    }
                    PdfPCell countOfflineVsmBoard = new PdfPCell(new Phrase(count, font12));
                    countOfflineVsmBoard.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflineVsmBoard.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOfflineVsmBoard);
                    count = "00";
                    if (vsmBoard.getOnline() != null) {
                        count = vsmBoard.getOnline() + "";
                    }
                    if (count.length() == 1) {
                        count = "0" + count;
                    }
                    PdfPCell countOnlineVsmBoard = new PdfPCell(new Phrase(count, font12));
                    countOnlineVsmBoard.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlineVsmBoard.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOnlineVsmBoard);
                } else {
                    PdfPCell countOfflineVsmBoard = new PdfPCell(new Phrase("", font12));
                    countOfflineVsmBoard.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflineVsmBoard.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOfflineVsmBoard);

                    PdfPCell countOnlineVsmBoard = new PdfPCell(new Phrase("", font12));
                    countOnlineVsmBoard.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlineVsmBoard.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOnlineVsmBoard);
                }

                ReportDeviceDTO cadpro = reportDevice.getCadpro();
                PdfPCell sttCadpro = new PdfPCell(new Phrase("3", font12));
                sttCadpro.setHorizontalAlignment(Element.ALIGN_CENTER);
                sttCadpro.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(sttCadpro);

                PdfPCell contentCadpro = new PdfPCell(new Phrase("Cadpro  TEC", font12));
                contentCadpro.setPaddingLeft(3f);
                contentCadpro.setPaddingBottom(5f);
//                contentSecurity.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentCadpro.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(contentCadpro);
                if (cadpro != null) {
                    count = "00";
                    if (cadpro.getOffline() != null && cadpro.getOffline() != 0) {
                        count = cadpro.getOffline() + "";
                        if (cadpro.getOffline() == 1) {
                            count = "Kh??ng ho???t ?????ng";
                        }
                    }
                    PdfPCell countOfflineCadpro = new PdfPCell(new Phrase(count, font12));
                    countOfflineCadpro.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflineCadpro.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    countOfflineCadpro.setPaddingBottom(5f);
                    tableReportDevice.addCell(countOfflineCadpro);
                    count = "00";
                    if (cadpro.getOnline() != null && cadpro.getOnline() != 0) {
                        count = cadpro.getOnline() + "";
                        if (cadpro.getOnline() == 1) {
                            count = "Ho???t ?????ng";
                        }
                    }
                    PdfPCell countOnlineCadpro = new PdfPCell(new Phrase(count, font12));
                    countOnlineCadpro.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlineCadpro.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    countOnlineCadpro.setPaddingBottom(5f);
                    tableReportDevice.addCell(countOnlineCadpro);
                } else {
                    PdfPCell countOfflineCadpro = new PdfPCell(new Phrase("", font12));
                    countOfflineCadpro.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflineCadpro.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOfflineCadpro);

                    PdfPCell countOnlineCadpro = new PdfPCell(new Phrase("", font12));
                    countOnlineCadpro.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlineCadpro.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOnlineCadpro);
                }

                ReportDeviceDTO screen = reportDevice.getScreen();
                PdfPCell sttScreen = new PdfPCell(new Phrase("5", font12));
                sttScreen.setHorizontalAlignment(Element.ALIGN_CENTER);
                sttScreen.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(sttScreen);

                PdfPCell contentScreen = new PdfPCell(new Phrase("M??n h??nh t?????ng", font12));
                contentScreen.setPaddingLeft(3f);
                contentScreen.setPaddingBottom(5f);
//                contentSecurity.setHorizontalAlignment(Element.ALIGN_CENTER);
                contentScreen.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tableReportDevice.addCell(contentScreen);
                if (screen != null) {
                    count = "00";
                    if (screen.getOffline() != null && screen.getOffline() != 0) {
                        count = screen.getOffline() + "";
                        if (screen.getOffline() == 1) {
                            count = "Kh??ng ho???t ?????ng";
                        }
                    }
                    PdfPCell countOfflineScreen = new PdfPCell(new Phrase(count, font12));
                    countOfflineScreen.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflineScreen.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    countOfflineScreen.setPaddingBottom(5f);
                    tableReportDevice.addCell(countOfflineScreen);
                    count = "00";
                    if (screen.getOnline() != null && screen.getOnline() != 0) {
                        count = screen.getOnline() + "";
                        if (screen.getOnline() == 1) {
                            count = "Ho???t ?????ng";
                        }
                    }
                    PdfPCell countOnlineScreen = new PdfPCell(new Phrase(count, font12));
                    countOnlineScreen.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlineScreen.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    countOnlineScreen.setPaddingBottom(5f);
                    tableReportDevice.addCell(countOnlineScreen);
                } else {
                    PdfPCell countOfflineScreen = new PdfPCell(new Phrase("", font12));
                    countOfflineScreen.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOfflineScreen.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOfflineScreen);

                    PdfPCell countOnlineScreen = new PdfPCell(new Phrase("", font12));
                    countOnlineScreen.setHorizontalAlignment(Element.ALIGN_CENTER);
                    countOnlineScreen.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tableReportDevice.addCell(countOnlineScreen);
                }


                int countOffs = 0;
                int countOns = 0;
                if (camera != null) {
                    countOffs += camera.getOffline();
                    countOns += camera.getOnline();
                }
                if (ptz != null) {
                    countOffs += ptz.getOffline();
                    countOns += ptz.getOnline();
                }
                if (vsmBoard != null) {
                    countOffs += vsmBoard.getOffline();
                    countOns += vsmBoard.getOnline();
                }
                if (cadpro != null) {
                    countOffs += cadpro.getOffline();
                    countOns += cadpro.getOnline();
                }
                if (screen != null) {
                    countOffs += screen.getOffline();
                    countOns += screen.getOnline();
                }


                PdfPCell countDayTitleDrive = new PdfPCell(new Phrase(" T???ng c???ng ng??y", font12BD));
                countDayTitleDrive.setHorizontalAlignment(Element.ALIGN_CENTER);
                countDayTitleDrive.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countDayTitleDrive.setPaddingBottom(7f);
                countDayTitleDrive.setBackgroundColor(new BaseColor(237, 237, 237));
                countDayTitleDrive.setColspan(2);
                tableReportDevice.addCell(countDayTitleDrive);

                PdfPCell offDayHotline = new PdfPCell(new Phrase("" + countOffs, font12BD));
                offDayHotline.setHorizontalAlignment(Element.ALIGN_CENTER);
                offDayHotline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                offDayHotline.setBackgroundColor(new BaseColor(237, 237, 237));
                tableReportDevice.addCell(offDayHotline);

                PdfPCell onDayHotline = new PdfPCell(new Phrase("" + countOns, font12BD));
                onDayHotline.setHorizontalAlignment(Element.ALIGN_CENTER);
                onDayHotline.setVerticalAlignment(Element.ALIGN_MIDDLE);
                onDayHotline.setBackgroundColor(new BaseColor(237, 237, 237));
                tableReportDevice.addCell(onDayHotline);

                PdfPCell countMonthTitleDevice = new PdfPCell(new Phrase(" L??y k??? th??ng", font12BD));
                countMonthTitleDevice.setHorizontalAlignment(Element.ALIGN_CENTER);
                countMonthTitleDevice.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countMonthTitleDevice.setPaddingBottom(7f);
                countMonthTitleDevice.setBackgroundColor(new BaseColor(222, 234, 246));
                countMonthTitleDevice.setColspan(2);
                tableReportDevice.addCell(countMonthTitleDevice);

                PdfPCell countMonthOff = new PdfPCell(new Phrase("" + reportDevice.getCountMonth().getOffline(), font12BD));
                countMonthOff.setHorizontalAlignment(Element.ALIGN_CENTER);
                countMonthOff.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countMonthOff.setBackgroundColor(new BaseColor(222, 234, 246));
                tableReportDevice.addCell(countMonthOff);

                PdfPCell countMonthOn = new PdfPCell(new Phrase("" + reportDevice.getCountMonth().getOnline(), font12BD));
                countMonthOn.setHorizontalAlignment(Element.ALIGN_CENTER);
                countMonthOn.setVerticalAlignment(Element.ALIGN_MIDDLE);
                countMonthOn.setBackgroundColor(new BaseColor(222, 234, 246));
                tableReportDevice.addCell(countMonthOn);


                tableReportDevice.setWidthPercentage(100);
                document.add(tableReportDevice);
                java.io.File path = new java.io.File(urlFile);
                document.close();
                sizeFile = Files.size(pathh);
                LOGGER.info("send upload");
                String link = uploadFile(path);
                LOGGER.info("upload success");
                System.out.println(link);
                Map<String, Object> param = putBodyParamReportDailyEvent(uuid, link);
                handleSendNotifyToDevice(param);

//                EventFile eventFile = new EventFile();
//                eventFile.setSize(sizeFile.intValue());
//                eventFile.setEventId(eventId);
//                eventFile.setFileType("application/pdf");
//                eventFile.setId(UUID.randomUUID().toString());
//                eventFile.setFileName("B??o c??o nhanh tai n???n giao th??ng - " + data.getEventKey() + ".pdf");
//                eventFile.setUploadTime(now);
//                eventFile.setCreateBy(uuid);
//                eventFile.setCreateDate(now);
//                eventFile.setStartTime(now);
//                eventFile.setFileUrl(link);
//                saveSaveFileCurent(eventFile);

            }
        } catch (FileNotFoundException ex) {
            LOGGER.info(ex.getMessage());
//            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (DocumentException | IOException ex) {
            LOGGER.info(ex.getMessage());
//            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE,  ex.getMessage(), ex);
        }
        return new EventFile();
    }

    @Override
    public EventFile createFileDayReport(String uuid, String startTime, String endTime) throws ParseException {
        Document document = new Document(PageSize.A4, 30f, 15f, 20f, 20f);
        Long sizeFile = new Long(0);
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date startDate = format.parse(startTime);
        Date endDate = format.parse(endTime);
        HistoryStatusReportDTO pagingDeviceStatus = deviceStatusService.reportStatusDisconnect(startTime, endTime, "", "", null, 0, 200, true);
        List<HistoryStatusDeviceDTO> responseDeviceStatus = pagingDeviceStatus.getData();
        String month = String.valueOf(startDate.getMonth());
        String year = String.valueOf(startDate.getYear());
        if (startDate.getMonth() < 10) {
            month = "0" + month;
        }
        Response response = reportService.getAllShift(month + "-" + year);
        Response responseDailyReport = reportService.getDailyReport(startTime, endTime);
        List<EventJobDTO> responseJob = calendarService.getJobForReport(startDate, endDate, null, null);
        Response responseTrafficReport = reportService.getTrafficReport(startTime, endTime);
        Response responseEvent = reportService.getEventReport(startTime, endTime, 0, 10000);
        DateFormat timeff = new SimpleDateFormat("HH-mm-ss");
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String title_1 = "1. Ca l??m vi???c     ";
        if (response != null) {
            List<ShiftDTO> shiftDTOS = mapper.convertValue(response.getData(), new TypeReference<List<ShiftDTO>>() {
            });
            for (ShiftDTO shift : shiftDTOS) {
                title_1 += "Ca " + shift.getNumber() + ": " + shift.getStartTime() + " - " + shift.getEndTime() + "          ";
            }
        }
        try {
            Date now = new Date();
            String fileName = "B??o c??o ng??y" + date.format(now) + " " + timeff.format(now) + ".pdf";
            String urlFile = "" + fileName;
            Path pathh = Paths.get(urlFile);
            PdfWriter.getInstance(document, new FileOutputStream(urlFile));
            document.open();
            Font font1 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            Font font2 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            Font font3 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            font1.setSize(12);
            font2.setSize(15);
            font3.setSize(21);
            BaseFont baseFont = BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font boldText = new Font(baseFont, 12);

            // title b???ng 1:
            Paragraph tenVanBan = new Paragraph("B??o c??o ng??y " + startDate.getDate() + "/" + (startDate.getMonth() + 1) + "/" + (startDate.getYear() + 1900), font3);
            tenVanBan.setAlignment(Element.ALIGN_CENTER);
            Paragraph first_title = new Paragraph(title_1, font1);
            Paragraph time = new Paragraph("    Th???i gian ghi nh???n: 00:00 - 23:59", font1);
            document.add(tenVanBan);
            document.add(first_title);
            document.add(time);
            // B???ng 1:

            PdfPTable tableShilf = new PdfPTable(5);
            float[] widths = new float[]{3f, 3f, 2f, 3f, 3f};
            tableShilf.setWidths(widths);
            PdfPCell cell = new PdfPCell(new Phrase("T??i kho???n", boldText));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderWidth(1);
            tableShilf.addCell(cell);
            cell = new PdfPCell(new Phrase("H??? v?? t??n", boldText));
            cell.setBorderWidth(1);
            cell.setPadding(3f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableShilf.addCell(cell);
            cell = new PdfPCell(new Phrase("Ca", boldText));
            cell.setBorderWidth(1);
            cell.setPadding(3f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableShilf.addCell(cell);
            cell = new PdfPCell(new Phrase("????ng nh???p", boldText));
            cell.setBorderWidth(1);
            cell.setPadding(3f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableShilf.addCell(cell);
            cell = new PdfPCell(new Phrase("Th???i gian ho???t ?????ng", boldText));
            cell.setBorderWidth(1);
            cell.setPadding(3f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableShilf.addCell(cell);
            tableShilf.setSpacingBefore(10f);

            List<DailyReportDTO> shiftDTOS = null;
            if (responseDailyReport != null) {
                shiftDTOS = mapper.convertValue(responseDailyReport.getData(), new TypeReference<List<DailyReportDTO>>() {
                });
            }

            for (DailyReportDTO dailyReportDTO : shiftDTOS) {

                cell = new PdfPCell(new Phrase(dailyReportDTO.getUsername(), font1));
                cell.setPadding(3f);
                tableShilf.addCell(cell);
                cell = new PdfPCell(new Phrase(dailyReportDTO.getUserFullName(), font1));
                cell.setPadding(3f);
                tableShilf.addCell(cell);

                cell = new PdfPCell(new Phrase(dailyReportDTO.getShift(), font1));
                cell.setPadding(3f);
                tableShilf.addCell(cell);

                cell = new PdfPCell(new Phrase(dailyReportDTO.getLoginHistory(), font1));
                cell.setPadding(3f);
                tableShilf.addCell(cell);

                cell = new PdfPCell(new Phrase(changeTimeTotal(dailyReportDTO.getTotalTime()), font1));
                cell.setPadding(3f);
                tableShilf.addCell(cell);

            }


            document.add(tableShilf);


            // Title b???ng 2
            List<TrafficFlowSite> trafficFlowSites = null;
            if (responseDailyReport != null) {
                trafficFlowSites = mapper.convertValue(responseTrafficReport.getData(), new TypeReference<List<TrafficFlowSite>>() {
                });
            }

            tableShilf.setSpacingBefore(10f);
            Paragraph sec_title = new Paragraph("2. L??u l?????ng: " + trafficFlowSites.size(), font1);
            document.add(sec_title);

            // B???ng 2
            PdfPTable tableTraffic = new PdfPTable(6);
            float[] widthTraffic = new float[]{3f, 3f, 3f, 3f, 3f, 3f};
            tableTraffic.setWidths(widthTraffic);
            PdfPCell cell2 = new PdfPCell(new Phrase("T???", boldText));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorderWidth(1);
            tableTraffic.addCell(cell2);
            cell2 = new PdfPCell(new Phrase("?????n", boldText));
            cell2.setBorderWidth(1);
            cell2.setPadding(3f);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableTraffic.addCell(cell2);
            cell2 = new PdfPCell(new Phrase("?? t??", boldText));
            cell2.setBorderWidth(1);
            cell2.setPadding(3f);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableTraffic.addCell(cell2);
            cell2 = new PdfPCell(new Phrase("Xe kh??ch", boldText));
            cell2.setBorderWidth(1);
            cell2.setPadding(3f);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableTraffic.addCell(cell2);
            cell2 = new PdfPCell(new Phrase("Xe t???i", boldText));
            cell2.setBorderWidth(1);
            cell2.setPadding(3f);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableTraffic.addCell(cell2);
            cell2 = new PdfPCell(new Phrase("T???ng s???", boldText));
            cell2.setBorderWidth(1);
            cell2.setPadding(3f);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableTraffic.addCell(cell2);


            tableTraffic.setSpacingBefore(10f);


            Long totalCar = Long.valueOf(0);
            Long totalBus = Long.valueOf(0);
            Long totalTruck = Long.valueOf(0);
            for (TrafficFlowSite trafficFlowSite : trafficFlowSites) {
                Long car = Long.valueOf(0);
                Long bus = Long.valueOf(0);
                Long truck = Long.valueOf(0);

                for (TrafficDataByObject dataByObject : trafficFlowSite.getTrafficDataByObjects()) {
                    if (dataByObject.getObjectType().equalsIgnoreCase("car")) {
                        car = dataByObject.getVolume();
                        totalCar += dataByObject.getVolume();
                    } else if (dataByObject.getObjectType().equalsIgnoreCase("bus")) {
                        bus = dataByObject.getVolume();
                        totalBus += dataByObject.getVolume();
                    } else if (dataByObject.getObjectType().equalsIgnoreCase("truck")) {
                        truck = dataByObject.getVolume();
                        totalTruck += dataByObject.getVolume();
                    }
                }
                cell2 = new PdfPCell(new Phrase(trafficFlowSite.getSiteName() + " chi???u " + trafficFlowSite.getDirectionCode(), font1));
                cell2.setPadding(3f);
                tableTraffic.addCell(cell2);
                cell2 = new PdfPCell(new Phrase(trafficFlowSite.getSiteAfterNearName() + " chi???u " + trafficFlowSite.getDirectionCode(), font1));
                cell2.setPadding(3f);
                tableTraffic.addCell(cell2);

                cell2 = new PdfPCell(new Phrase(String.valueOf(car), font1));
                cell2.setPadding(3f);
                tableTraffic.addCell(cell2);

                cell2 = new PdfPCell(new Phrase(String.valueOf(bus), font1));
                cell2.setPadding(3f);
                tableTraffic.addCell(cell2);

                cell2 = new PdfPCell(new Phrase(String.valueOf(truck), font1));
                cell2.setPadding(3f);
                tableTraffic.addCell(cell2);

                cell2 = new PdfPCell(new Phrase(String.valueOf(trafficFlowSite.getTotalTraffic()), font1));
                cell2.setPadding(3f);
                tableTraffic.addCell(cell2);

            }

            cell2 = new PdfPCell(new Phrase("T???ng s???", font1));
            cell2.setPadding(3f);
            cell2.setColspan(2);
            tableTraffic.addCell(cell2);


            cell2 = new PdfPCell(new Phrase(String.valueOf(totalCar), font1));
            cell2.setPadding(3f);
            tableTraffic.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(String.valueOf(totalBus), font1));
            cell2.setPadding(3f);
            tableTraffic.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(String.valueOf(totalTruck), font1));
            cell2.setPadding(3f);
            tableTraffic.addCell(cell2);

            cell2 = new PdfPCell(new Phrase(String.valueOf(totalCar + totalBus + totalTruck), font1));
            cell2.setPadding(3f);
            tableTraffic.addCell(cell2);

            document.add(tableTraffic);
            tableShilf.setSpacingBefore(10f);

            // Title b???ng 3
            List<EventDTO> eventDTOS = null;
            if (responseEvent != null) {
                eventDTOS = mapper.convertValue(responseEvent.getData(), new TypeReference<List<EventDTO>>() {
                });
            }
            Paragraph thur_title = new Paragraph("3. S??? ki???n: " + eventDTOS.size(), font1);
            document.add(thur_title);

            // B???ng 3
            PdfPTable tableEvent = new PdfPTable(6);
            float[] widthEvent = new float[]{3f, 3f, 3f, 3f, 3f, 3f};
            tableEvent.setWidths(widthEvent);
            PdfPCell cell3 = new PdfPCell(new Phrase("Th???i gian", boldText));
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell3.setBorderWidth(1);
            tableEvent.addCell(cell3);
            cell3 = new PdfPCell(new Phrase("M?? s??? ki???n", boldText));
            cell3.setBorderWidth(1);
            cell3.setPadding(3f);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEvent.addCell(cell3);
            cell3 = new PdfPCell(new Phrase("Lo???i s??? ki???n", boldText));
            cell3.setBorderWidth(1);
            cell3.setPadding(3f);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEvent.addCell(cell3);
            cell3 = new PdfPCell(new Phrase("V??? tr??", boldText));
            cell3.setBorderWidth(1);
            cell3.setPadding(3f);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEvent.addCell(cell3);
            cell3 = new PdfPCell(new Phrase("Ngu???n", boldText));
            cell3.setBorderWidth(1);
            cell3.setPadding(3f);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEvent.addCell(cell3);
            cell3 = new PdfPCell(new Phrase("Tr???ng th??i", boldText));
            cell3.setBorderWidth(1);
            cell3.setPadding(3f);
            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableEvent.addCell(cell3);
            tableEvent.setSpacingBefore(10f);


            for (EventDTO eventDTO : eventDTOS) {

                cell3 = new PdfPCell(new Phrase(eventDTO.getStartTime(), font1));
                cell3.setPadding(3f);
                tableEvent.addCell(cell3);
                cell3 = new PdfPCell(new Phrase(eventDTO.getKey(), font1));
                cell3.setPadding(3f);
                tableEvent.addCell(cell3);

                cell3 = new PdfPCell(new Phrase(String.valueOf(eventDTO.getEventName()), font1));
                cell3.setPadding(3f);
                tableEvent.addCell(cell3);

                cell3 = new PdfPCell(new Phrase(String.valueOf(eventDTO.getSite().getSiteName()), font1));
                cell3.setPadding(3f);
                tableEvent.addCell(cell3);

                String source = "";
                if (!StringUtil.isNullOrEmpty(eventDTO.getSourceName())) {
                    source = eventDTO.getSourceName();
                } else if (!StringUtil.isNullOrEmpty(eventDTO.getObjectCreate())) {
                    source = eventDTO.getObjectCreate();
                } else {
                    source = "";
                }
                cell3 = new PdfPCell(new Phrase(source, font1));
                cell3.setPadding(3f);
                tableEvent.addCell(cell3);

                cell3 = new PdfPCell(new Phrase(String.valueOf(eventDTO.getNameStatus()), font1));
                cell3.setPadding(3f);
                tableEvent.addCell(cell3);
            }
            document.add(tableEvent);
            tableEvent.setSpacingBefore(10f);

            // Title b???ng 4

            Paragraph four_title = new Paragraph("4. C??ng vi???c: " + responseJob.size(), font1);
            document.add(four_title);
            PdfPTable tableJob = new PdfPTable(9);
            float[] widthJob = new float[]{3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f, 3f};
            tableJob.setWidths(widthJob);
            PdfPCell cell4 = new PdfPCell(new Phrase("Th???i gian", boldText));
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4.setBorderWidth(1);
            tableJob.addCell(cell4);
            cell4 = new PdfPCell(new Phrase("M?? s??? ki???n", boldText));
            cell4.setBorderWidth(1);
            cell4.setPadding(3f);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableJob.addCell(cell4);
            cell4 = new PdfPCell(new Phrase("Lo???i s??? ki???n", boldText));
            cell4.setBorderWidth(1);
            cell4.setPadding(3f);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableJob.addCell(cell4);
            cell4 = new PdfPCell(new Phrase("V??? tr??", boldText));
            cell4.setBorderWidth(1);
            cell4.setPadding(3f);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableJob.addCell(cell4);
            cell4 = new PdfPCell(new Phrase("C??ng vi???c", boldText));
            cell4.setBorderWidth(1);
            cell4.setPadding(3f);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableJob.addCell(cell4);
            cell4 = new PdfPCell(new Phrase("Ng??y h???t h???n", boldText));
            cell4.setBorderWidth(1);
            cell4.setPadding(3f);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableJob.addCell(cell4);
            cell4 = new PdfPCell(new Phrase("Ng??y c???p nh???t", boldText));
            cell4.setBorderWidth(1);
            cell4.setPadding(3f);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableJob.addCell(cell4);
            cell4 = new PdfPCell(new Phrase("Ng?????i x??? l??", boldText));
            cell4.setBorderWidth(1);
            cell4.setPadding(3f);
            tableJob.addCell(cell4);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell4 = new PdfPCell(new Phrase("Tr???ng th??i", boldText));
            cell4.setBorderWidth(1);
            cell4.setPadding(3f);
            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableJob.addCell(cell4);
            tableJob.setSpacingBefore(10f);


            for (EventJobDTO eventJobDTO : responseJob) {

                cell4 = new PdfPCell(new Phrase(String.valueOf(eventJobDTO.getJob().getCreatedDate()), font1));
                cell4.setPadding(3f);
                tableJob.addCell(cell4);

                cell4 = new PdfPCell(new Phrase(eventJobDTO.getEvent().getKey(), font1));
                cell4.setPadding(3f);
                tableJob.addCell(cell4);

                cell4 = new PdfPCell(new Phrase(String.valueOf(eventJobDTO.getEvent().getEventName()), font1));
                cell4.setPadding(3f);
                tableJob.addCell(cell4);

                cell4 = new PdfPCell(new Phrase(String.valueOf(detectSite(eventJobDTO.getJob().getStartSiteId())), font1));
                cell4.setPadding(3f);
                tableJob.addCell(cell4);

                cell4 = new PdfPCell(new Phrase(detectJobType(eventJobDTO.getJob().getJobType()), font1));
                cell4.setPadding(3f);
                tableJob.addCell(cell4);

                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String endTimeJob = "";
                if (eventJobDTO.getJob().getEndTime() != null) {
                    endTimeJob = String.valueOf(eventJobDTO.getJob().getEndTime());
                }

                cell4 = new PdfPCell(new Phrase(endTimeJob, font1));
                cell4.setPadding(3f);
                tableJob.addCell(cell4);

                List<ActionHistory> list = eventJobDTO.getJob().getActionHistory();
                SimpleDateFormat dateFormatByDay = new SimpleDateFormat("yyyy-MM-dd");
                List<ActionHistory> actionHistoryListFilter = list.stream().filter(action -> dateFormatByDay.format(action.getActionTime()).equalsIgnoreCase(dateFormatByDay.format(startDate))).collect(Collectors.toList());


                Date mDate = null;
                String userName = "";
                String jobname = "";
                ActionHistory actionHistory = null;
                if (!CollectionUtils.isEmpty(actionHistoryListFilter)) {
                    actionHistory = list.get(actionHistoryListFilter.size() - 1);
                    mDate = actionHistoryListFilter.get(actionHistoryListFilter.size() - 1).getActionTime();
                    userName = actionHistoryListFilter.get(actionHistoryListFilter.size() - 1).getActorName();
                    jobname = actionHistoryListFilter.get(actionHistoryListFilter.size() - 1).getActionName();
                }
                cell4 = new PdfPCell(new Phrase(format1.format(mDate), font1));
                cell4.setPadding(3f);
                tableJob.addCell(cell4);

                cell4 = new PdfPCell(new Phrase(String.valueOf(userName), font1));
                cell4.setPadding(3f);
                tableJob.addCell(cell4);

                cell4 = new PdfPCell(new Phrase(String.valueOf(getStatusJob(actionHistory, eventJobDTO.getJob().getJobType())), font1));
                cell4.setPadding(3f);
                tableJob.addCell(cell4);
            }

            document.add(tableJob);


            // Title b???ng 5

            Paragraph fine_title = new Paragraph("5. Thi???t b???: " + responseDeviceStatus.size(), font1);
            document.add(fine_title);

            // B???ng 5
            PdfPTable tableDevice = new PdfPTable(6);
            float[] widthDevice = new float[]{3f, 3f, 3f, 3f, 3f, 3f};
            tableDevice.setWidths(widthDevice);
            PdfPCell cell5 = new PdfPCell(new Phrase("ID", boldText));
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell5.setBorderWidth(1);
            tableDevice.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("T??n", boldText));
            cell5.setBorderWidth(1);
            cell5.setPadding(3f);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDevice.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("V??? tr??", boldText));
            cell5.setBorderWidth(1);
            cell5.setPadding(3f);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDevice.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("Tr???ng th??i k???t n???i", boldText));
            cell5.setBorderWidth(1);
            cell5.setPadding(3f);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDevice.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("S??? l???n m???t k???t n???i", boldText));
            cell5.setBorderWidth(1);
            cell5.setPadding(3f);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDevice.addCell(cell5);
            cell5 = new PdfPCell(new Phrase("Th???i gian m???t k???t n???i", boldText));
            cell5.setBorderWidth(1);
            cell5.setPadding(3f);
            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableDevice.addCell(cell5);
            tableDevice.setSpacingBefore(10f);


            for (HistoryStatusDeviceDTO statusDeviceDTO : responseDeviceStatus) {

                cell5 = new PdfPCell(new Phrase(statusDeviceDTO.getDeviceId(), font1));
                cell5.setPadding(3f);
                tableDevice.addCell(cell5);

                cell5 = new PdfPCell(new Phrase(statusDeviceDTO.getDeviceName(), font1));
                cell5.setPadding(3f);
                tableDevice.addCell(cell5);

                cell5 = new PdfPCell(new Phrase(String.valueOf(statusDeviceDTO.getSite().getSiteName()), font1));
                cell5.setPadding(3f);
                tableDevice.addCell(cell5);

                cell5 = new PdfPCell(new Phrase(detectStatus(statusDeviceDTO.getStatus()), font1));
                cell5.setPadding(3f);
                tableDevice.addCell(cell5);

                cell5 = new PdfPCell(new Phrase(String.valueOf(statusDeviceDTO.getNumberOff()), font1));
                cell5.setPadding(3f);
                tableDevice.addCell(cell5);

                String status = "";
                if (!StringUtil.isNullOrEmpty(statusDeviceDTO.getTimeOff())) {
                    status = statusDeviceDTO.getTimeOff();
                }

                cell5 = new PdfPCell(new Phrase(status, font1));
                cell5.setPadding(3f);
                tableDevice.addCell(cell5);
            }

            document.add(tableDevice);


            System.out.println(fileName);
            java.io.File path = new java.io.File(urlFile);
            document.close();
            sizeFile = Files.size(pathh);
            String link = uploadFile(path);
            System.out.println(link);
            EventFile eventFile = new EventFile();
            eventFile.setSize(sizeFile.intValue());
            eventFile.setFileType("application/pdf");
            eventFile.setId(UUID.randomUUID().toString());
            eventFile.setFileName(fileName);
            eventFile.setUploadTime(now);
            eventFile.setCreateBy(uuid);
            eventFile.setCreateDate(now);
            eventFile.setStartTime(now);
            eventFile.setFileUrl(link);
            saveSaveFile(eventFile);
            Map<String, Object> param = putBodyParamReportDay(uuid, link);
            handleSendNotifyToDevice(param);
            LOGGER.info("export done");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new EventFile();
    }


    @Override
    public EventFile createFileEventInfoUpdate(String eventId, String uuid) throws Exception {
        Document document = new Document(PageSize.A4, 30f, 15f, 20f, 20f);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat datePasser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat time = new SimpleDateFormat("HH-mm-ss");
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
//        Path pathh = Paths.get(urlFile);
        Long sizeFile = new Long(0);
        try {
            EventResponseDTO eventResponseDTO = eventService.historyEvent(eventId);
            if (eventResponseDTO != null && eventResponseDTO.getData() != null && !eventResponseDTO.getData().isEmpty()) {
                Map<String, User> mapUser = getAllUser();
                List<EventDTO> eventDTO = eventResponseDTO.getData();
                EventDTO data = eventDTO.get(eventDTO.size() - 1);
                Date now = new Date();
                String fileName = "Th??ng tin s??? ki???n" + data.getKey() + "-" + date.format(now) + " " + time.format(now) + ".pdf";
                String urlFile = "" + fileName;
                Path pathh = Paths.get(urlFile);
                PdfWriter.getInstance(document, new FileOutputStream(urlFile));
                document.open();
                Font font1 = new Font(BaseFont.createFont("config/times.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font2 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                Font font3 = new Font(BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
                font1.setSize(12);
                font2.setSize(15);
                font3.setSize(21);
                BaseFont baseFont = BaseFont.createFont("config/timesbd.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font boldText = new Font(baseFont, 12);
                Paragraph tieuDe = new Paragraph("H??? TH???NG GI??M S??T CAO T???C", font2);
                tieuDe.setAlignment(Element.ALIGN_RIGHT);
                Paragraph space = new Paragraph("         ");
                try {
                    Image image1 = Image.getInstance(new URL(caotoc));
                    image1.scaleToFit(80f, 80f);
                    image1.setAlignment(Element.ALIGN_LEFT);
                    tieuDe.add(image1);
                } catch (IOException ex) {
                    Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                Paragraph tenVanBan = new Paragraph("TH??NG TIN S??? KI???N PH??T HI???N", font3);
                tenVanBan.setAlignment(Element.ALIGN_CENTER);
                Paragraph infoEvent = new Paragraph("Th??ng tin s??? ki???n", boldText);
                Paragraph paragraph = new Paragraph("D??? li???u k???t xu???t l??c: " + timeFormat.format(now) + " - " + dateFormat.format(now), font1);
                Paragraph timeStart = new Paragraph("Th???i gian ph??t hi???n: " + timeFormat.format(datePasser.parse(data.getStartTime())) + " - " + dateFormat.format(datePasser.parse(data.getStartTime())), font1);
                Paragraph paramHistory;
                if (data.getModifiedDate() != null) {
                    paramHistory = new Paragraph("C???p nh???p s??? ki???n: " + timeFormat.format(datePasser.parse(data.getModifiedDate())) + " - " + dateFormat.format(datePasser.parse(data.getModifiedDate())), font1);
                } else {
                    paramHistory = new Paragraph("C???p nh???p s??? ki???n: " + timeFormat.format(data.getCreateDate()) + " - " + dateFormat.format(data.getCreateDate()), font1);
                }
                Paragraph address1 = new Paragraph("V??? tr?? ph??t hi???n: " + data.getSite().getSiteName(), font1);
                Paragraph address2;
                if (data.getSiteCorrect() == null) {
                    address2 = new Paragraph("V??? tr?? ch??nh x??c: " + data.getSite().getSiteName(), font1);
                } else {
                    address2 = new Paragraph("V??? tr?? ch??nh x??c: " + data.getSiteCorrect().getSiteName(), font1);
                }

                Paragraph typeEvent = new Paragraph("Lo???i s??? ki???n: " + data.getEventName(), font1);
                Paragraph sourceName;
                if (StringUtil.isNullOrEmpty(data.getSourceName())) {
                    sourceName = new Paragraph("Ngu???n ph??t hi???n: " + data.getObjectCreate(), font1);
                } else {
                    sourceName = new Paragraph("Ngu???n ph??t hi???n: " + data.getSourceName(), font1);
                }
                Paragraph status = new Paragraph("Tr???ng th??i: " + data.getEventStatus().description(), font1);
                Paragraph note;
                if (StringUtil.isNullOrEmpty(data.getNote())) {
                    note = new Paragraph("Ghi ch??: " + "", font1);
                } else {
                    note = new Paragraph("Ghi ch??: " + data.getNote(), font1);
                }
                Paragraph image = new Paragraph("H??nh ???nh s??? ki???n: ", font1);

                document.add(tieuDe);
                document.add(space);
                document.add(tenVanBan);
                document.add(space);
                document.add(infoEvent);
                document.add(paragraph);
                document.add(timeStart);
                document.add(paramHistory);
                document.add(address1);
                document.add(address2);
                document.add(typeEvent);
                document.add(sourceName);
                document.add(status);
                document.add(note);
                document.add(image);
                document.add(space);
                String imageUrl = data.getImageUrl();
                while (imageUrl != null && imageUrl.indexOf(",") > 0) {
                    try {
                        String url = imageUrl.substring(0, imageUrl.indexOf(","));
                        Image imageEvent;
                        if (url.startsWith("http")) {
                            imageEvent = Image.getInstance(new URL(url));
                        } else {
                            imageEvent = Image.getInstance(new URL(urlImage + url));
                        }
                        imageEvent.scaleToFit(400f, 400f);
                        imageEvent.setAlignment(Element.ALIGN_CENTER);
                        document.add(imageEvent);
                        imageUrl = imageUrl.substring(imageUrl.indexOf(",") + 1);
                    } catch (IOException ex) {
                        Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (!StringUtil.isNullOrEmpty(imageUrl)) {
                    try {
                        Image imageEvent;
                        if (imageUrl.startsWith("http")) {
                            imageEvent = Image.getInstance(new URL(imageUrl));
                        } else {
                            imageEvent = Image.getInstance(new URL(urlImage + imageUrl));
                        }
                        imageEvent.scaleToFit(400f, 400f);
                        imageEvent.setAlignment(Element.ALIGN_CENTER);
                        document.add(imageEvent);
                    } catch (IOException ex) {
                        Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                System.out.println(fileName);
                java.io.File path = new java.io.File(urlFile);
                document.close();
                sizeFile = Files.size(pathh);
                String link = uploadFile(path);
                System.out.println(link);
                EventFile eventFile = new EventFile();
                eventFile.setSize(sizeFile.intValue());
                eventFile.setEventId(eventId);
                eventFile.setFileType("application/pdf");
                eventFile.setId(UUID.randomUUID().toString());
                eventFile.setFileName("Th??ng tin s??? ki???n" + data.getKey() + ".pdf");
                eventFile.setUploadTime(now);
                eventFile.setCreateBy(uuid);
                eventFile.setCreateDate(now);
                eventFile.setStartTime(now);
                eventFile.setFileUrl(link);
                saveSaveFileCurent(eventFile);
                LOGGER.info("export done");

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException | IOException ex) {
            Logger.getLogger(EventFileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
//        EventFile eventFile = new EventFile(UUID.randomUUID().toString(),datePasser.parse(), violationHistory.getParentId(), "pdf", "Th??ng tin s??? vi???c", fileName, sizeFile.intValue());
        return new EventFile();
    }

    @Override
    public EventFile createFileAccident(String eventId, String uuid) throws Exception {
        return null;
    }


    private void saveSaveFile(EventFile eventFile) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/create/file";
        //Payload request dbm.root.url//v1.0/dbm/management/mons/recognitions
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> bodyParam = new HashMap<>();
//        bodyParam.put("id", eventFile.getId());
        bodyParam.put("eventId", eventFile.getEventId());
        bodyParam.put("fileName", eventFile.getFileName());
        bodyParam.put("fileUrl", eventFile.getFileUrl());
//        bodyParam.put("startTime", dateFormat.format(eventFile.getStartTime()));
        bodyParam.put("size", eventFile.getSize());
        bodyParam.put("createBy", eventFile.getCreateBy());
        bodyParam.put("fileType", eventFile.getFileType());
        bodyParam.put("createDate", dateFormat.format(eventFile.getCreateDate()));
        bodyParam.put("uploadTime", dateFormat.format(eventFile.getUploadTime()));
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
    }

    private void saveSaveFileCurent(EventFile eventFile) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/create/file/current";
        //Payload request dbm.root.url//v1.0/dbm/management/mons/recognitions
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> bodyParam = new HashMap<>();
//        bodyParam.put("id", eventFile.getId());
        bodyParam.put("eventId", eventFile.getEventId());
        bodyParam.put("fileName", eventFile.getFileName());
        bodyParam.put("fileUrl", eventFile.getFileUrl());
//        bodyParam.put("startTime", dateFormat.format(eventFile.getStartTime()));
        bodyParam.put("size", eventFile.getSize());
        bodyParam.put("createBy", eventFile.getCreateBy());
        bodyParam.put("fileType", eventFile.getFileType());
        bodyParam.put("createDate", dateFormat.format(eventFile.getCreateDate()));
        bodyParam.put("uploadTime", dateFormat.format(eventFile.getUploadTime()));
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
    }

    private String uploadFile(java.io.File path) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(path));
        body.add("keepFileName", true);
        body.add("localUpload ", true);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseDto> response = restTemplate. postForEntity(RabbitMQProperties.UPLOAD_URL, requestEntity, ResponseDto.class);
        if (response != null && response.getBody() != null && response.getBody().getData() != null) {
            path.delete();
            return response.getBody().getData().getFileDownloadUri();
        }
        return null;
    }

    private Map<String, Object> putBodyParam(String userId, String url, String eventId) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 5);
        bodyParam.put("content", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("url", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("title", "Export file");
        bodyParam.put("objectType", "export-file-event");
        bodyParam.put("objectUuid", eventId);
        bodyParam.put("userId", userId);
        return bodyParam;
    }

    private Map<String, User> getAllUser() {
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath("/v1.0/user/all/internal");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE, RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("[<--] Id return {}", result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    Object data = resultResponse.getData().getData();
                    Map<String, User> mapUser = mapper.convertValue(data, new TypeReference<Map<String, User>>() {
                    });
                    return mapUser;
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    private Map<String, Unit> getAllGroup() {
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath("/v1.0/group/all/internal");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE, RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("[<--] Id return {}", result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    Object data = resultResponse.getData().getData();
                    Map<String, Unit> mapUser = mapper.convertValue(data, new TypeReference<Map<String, Unit>>() {
                    });
                    return mapUser;
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    private String detectSite(String siteId) {
        if (!StringUtil.isNullOrEmpty(siteId)) {
            SiteDTO siteDTO = siteService.findById(siteId);
            if (siteDTO != null) {
                return siteDTO.getSiteName();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    private String detectUser(String uuid) {
        if (!StringUtil.isNullOrEmpty(uuid)) {
            List<User> users = userService.findByListId(uuid);
            if (!CollectionUtils.isEmpty(users)) {
                return users.get(0).getFullName();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    private String detectStatus(Integer status) {
        if (status == 0) {
            return "M???t k???t n???i";
        } else {
            return "B??nh th?????ng";
        }
    }

    private String detectJobType(String type) {
        if (!StringUtil.isNullOrEmpty(type)) {
            if (type.equalsIgnoreCase("VEHICLE_RESCUE")) {
                return "C???u h??? ph????ng ti???n";
            }
            if (type.equalsIgnoreCase("UPDATE_VMS_BOARD")) {
                return "C???p nh???t bi???n b??o VMS";
            }
            if (type.equalsIgnoreCase("CONTACT_RELEVANT_AUTHORITIES")) {
                return "Li??n h??? c?? quan h???u quan";
            }
            if (type.equalsIgnoreCase("CHECK_SCENE")) {
                return "Ki???m tra hi???n tr?????ng";
            }
            if (type.equalsIgnoreCase("TROUBLESHOOT")) {
                return "S???a ch???a, kh???c ph???c s??? c???";
            }
            if (type.equalsIgnoreCase("CLOSE_LANE")) {
                return "????ng l??n ???????ng";
            }
            if (type.equalsIgnoreCase("CLOSE_OPEN_ENTRANCE_EXIT")) {
                return "????ng l???i ra/v??o";
            }
            if (type.equalsIgnoreCase("LIMIT_SPEED")) {
                return "H???n ch??? t???c ?????";
            }
            if (type.equalsIgnoreCase("FORBIDDEN_WAY")) {
                return "C???m ???????ng";
            }
            if (type.equalsIgnoreCase("NOTIFY_INFORMATION")) {
                return "G???i th??ng ??i???p VOV";
            }
            if (type.equalsIgnoreCase("SUPERVISE_SCENE")) {
                return "Gi??m s??t hi???n tr?????ng";
            }
            if (type.equalsIgnoreCase("OTHER")) {
                return "Kh??c";
            }
        } else {
            return "";
        }
        return "";
    }

    private String changeTimeTotal(Integer min) {
        if (min != null) {
            int hour = min / 60;
            int min2 = min % 60;
            return hour + " gi??? " + min2 + " ph??t";
        } else {
            return "0 gi??? 0 ph??t";
        }
    }

    private String getUserName(Job job) {
        String userName = "";
        if (!StringUtil.isNullOrEmpty(job.getUserIds())) {
            userName = detectUser(job.getUserIds());
        } else {
            List<ActionHistory> list = job.getActionHistory();
            for (ActionHistory actionHistory : list) {
                if (actionHistory.getActionCode().equalsIgnoreCase("CONFIRM")) {
                    userName = actionHistory.getActorName();
                    break;
                }
            }
        }
        return userName;
    }

    private Map<String, Object> putBodyParamReportDay(String userId, String url) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 5);
        bodyParam.put("content", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("url", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("title", "Export file");
        bodyParam.put("objectType", "export-file-report-daily");
        bodyParam.put("objectUuid", "");
        bodyParam.put("userId", userId);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamReportDailyEvent(String userId, String url) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 5);
        bodyParam.put("content", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("url", URLDecoder.decode(url, StandardCharsets.UTF_8));
        bodyParam.put("title", "Export file");
        bodyParam.put("objectType", "export-event-daily-report");
        bodyParam.put("objectUuid", "");
        bodyParam.put("userId", userId);
        return bodyParam;
    }

    private void handleSendNotifyToDevice(Map<String, Object> bodyParam) {
        try {
            RequestMessage rbacRpcRequest = new RequestMessage();
            rbacRpcRequest.setRequestMethod("POST");
            rbacRpcRequest.setRequestPath("/v1.0/notify/send-notify");
            rbacRpcRequest.setBodyParam(bodyParam);
            rbacRpcRequest.setUrlParam(null);
            rbacRpcRequest.setHeaderParam(null);
            rbacRpcRequest.setVersion(ResourcePath.VERSION);
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.NOTIFY_RPC_EXCHANGE, RabbitMQProperties.NOTIFY_RPC_QUEUE, RabbitMQProperties.NOTIFY_RPC_KEY, rbacRpcRequest.toJsonString());
            LOGGER.info("send notify status camera - result: " + result);
            if (result != null) {
                ObjectMapper mapper = new ObjectMapper();
                Response resultResponse = null;
                try {
                    resultResponse = mapper.readValue(result, Response.class);
                    if (resultResponse.getStatus() != HttpStatus.OK.value()) {
                        LOGGER.info("Error send notify to from heartbeat service");
                    }
                } catch (Exception ex) {
                    LOGGER.info("Error parse json in handleSendNotifyToDevice from heartbeat service: " + ex);
                }
            }
        } catch (Exception ex) {
            LOGGER.info("Error parse json in handleSendNotifyToDevice from heartbeat service: " + ex);
        }
    }

    private String getStatusJob(ActionHistory actionHistory, String jobType) {
        if (jobType.equalsIgnoreCase("VEHICLE_RESCUE")) {
            if (actionHistory.getActionName().equalsIgnoreCase("T???o c??ng vi???c")) {
                return "Nh???n x??? l??";
            } else if (actionHistory.getActionName().equalsIgnoreCase("X??c nh???n c??ng vi???c") || actionHistory.getActionName().equalsIgnoreCase("X??? l?? l???i") || actionHistory.getActionName().equalsIgnoreCase("Y??u c???u x??c minh l???i")) {
                return "??ang x??? l??";
            } else if (actionHistory.getActionName().equalsIgnoreCase("G???i k???t qu??? x??? l??")) {
                return "Ki???m tra";
            } else if (actionHistory.getActionName().equalsIgnoreCase("K???t th??c s??? ki???n") || actionHistory.getActionName().equalsIgnoreCase("K???t th??c c??ng vi???c") || actionHistory.getActionName().equalsIgnoreCase("H???y b??? c??ng vi???c")) {
                return "Ho??n th??nh";
            }
        } else if (jobType.equalsIgnoreCase("UPDATE_VMS_BOARD")) {
            if (actionHistory.getActionName().equalsIgnoreCase("T???o c??ng vi???c")) {
                return "Nh???n x??? l??";
            } else if (actionHistory.getActionName().equalsIgnoreCase("X??c nh???n c??ng vi???c") || actionHistory.getActionName().equalsIgnoreCase("C???p nh???t bi???n b??o VMS") || actionHistory.getActionName().equalsIgnoreCase("Ch???nh s???a c??ng vi???c")) {
                return "??ang x??? l??";
            } else if (actionHistory.getActionName().equalsIgnoreCase("Ho??n th??nh c???p nh???t")) {
                return "???? c???p nh???t b???n tin";
            } else if (actionHistory.getActionName().equalsIgnoreCase("K???t th??c s??? ki???n") || actionHistory.getActionName().equalsIgnoreCase("H???y b??? c??ng vi???c")) {
                return "Ho??n th??nh";
            } else if (jobType.equalsIgnoreCase("CONTACT_RELEVANT_AUTHORITIES")) {
                if (actionHistory.getActionName().equalsIgnoreCase("T???o c??ng vi???c") || actionHistory.getActionName().equalsIgnoreCase("Ch???nh s???a c??ng vi???c")) {
                    return "??ang x??? l??";
                } else if (actionHistory.getActionName().equalsIgnoreCase("K???t th??c s??? ki???n") || actionHistory.getActionName().equalsIgnoreCase("G???i ??i???n cho c?? quan h???u quan") || actionHistory.getActionName().equalsIgnoreCase("H???y b??? c??ng vi???c")) {
                    return "Ho??n th??nh";
                }
            } else if (jobType.equalsIgnoreCase("CHECK_SCENE")
                    || jobType.equalsIgnoreCase("CLOSE_LANE")
                    || jobType.equalsIgnoreCase("FORBIDDEN_WAY")
                    || jobType.equalsIgnoreCase("CLOSE_OPEN_ENTRANCE_EXIT")) {
                if (actionHistory.getActionName().equalsIgnoreCase("T???o c??ng vi???c")) {
                    return "Nh???n x??? l??";
                } else if (actionHistory.getActionName().equalsIgnoreCase("G???i k???t qu??? x??? l??")) {
                    return "Ki???m tra";
                } else if (actionHistory.getActionName().equalsIgnoreCase("K???t th??c s??? ki???n") || actionHistory.getActionName().equalsIgnoreCase("K???t th??c c??ng vi???c") || actionHistory.getActionName().equalsIgnoreCase("H???y b??? c??ng vi???c")) {
                    return "Ho??n th??nh";
                }
            } else if (jobType.equalsIgnoreCase("TROUBLESHOOT")) {
                if (actionHistory.getActionName().equalsIgnoreCase("T???o c??ng vi???c")) {
                    return "Nh???n x??? l??";
                } else if (actionHistory.getActionName().equalsIgnoreCase("X??c nh???n c??ng vi???c") || actionHistory.getActionName().equalsIgnoreCase("Y??u c???u x??c minh l???i") || actionHistory.getActionName().equalsIgnoreCase("Ch???nh s???a c??ng vi???c")) {
                    return "??ang x??? l??";
                } else if (actionHistory.getActionName().equalsIgnoreCase("G???i k???t qu??? x??? l??")) {
                    return "Ki???m tra";
                } else if (actionHistory.getActionName().equalsIgnoreCase("K???t th??c s??? ki???n") || actionHistory.getActionName().equalsIgnoreCase("K???t th??c c??ng vi???c")) {
                    return "Ho??n th??nh";
                }
            } else if (jobType.equalsIgnoreCase("NOTIFY_INFORMATION") || jobType.equalsIgnoreCase("SUPERVISE_SCENE")) {
                if (actionHistory.getActionName().equalsIgnoreCase("T???o c??ng vi???c") || actionHistory.getActionName().equalsIgnoreCase("Ch???nh s???a c??ng vi???c")) {
                    return "??ang x??? l??";
                } else if (actionHistory.getActionName().equalsIgnoreCase("K???t th??c s??? ki???n") || actionHistory.getActionName().equalsIgnoreCase("C???p nh???t k???t qu???")) {
                    return "Ho??n th??nh";
                }
            }
        }
        return "";
    }
}
