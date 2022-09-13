package com.elcom.its.uploadservice.sheduler;

import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadFileScheduler {
    @Scheduled(cron = "00 55 23 * * ?") // chạy vào lúc 23h59p59s mỗi ngày để xóa những file trích xuất vi phạm(.zip)
    public void removeFileZipExtractedViolation(){
        System.out.println("removeFileZipExtractedViolation");
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String today = dateFormat.format(date);

        File[] files = new File("./upload/file").listFiles();
        for(File file : files){
            if(file.getName().contains(today)){
                File[] files1 = new File("./upload/file/" + today).listFiles();
                for(File file1 : files1){
                    if(file1.getName().endsWith(".zip")){
                        System.out.println(file1.getName());
                        file1.delete();
                    }
                }
                File[] files1New = new File("./upload/file/" + today).listFiles();
                if(files1New.length == 0){
                    file.delete();
                }
            }
        }
    }
}
