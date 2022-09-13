/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.zalo.dto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import javax.imageio.ImageIO;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author admin
 */
public class Test {

//    public static void main(String[] args) throws MalformedURLException, IOException {
//        URL imageUrl = new URL("http://103.21.151.157:8181/event/b1277ef9-1a66-486e-89b9-01a3dd1b1e6d/20210423/11/755_RECOGNITION_911_motorbike_21B188633_20210423_115106_a984240b-fc95-4e9d-85aa-128ed2b38942.jpg");  // Sample url, replace with yours
//        String destinationFile = "sample.jpg";
//        /**
//         * *****************Multipart Upload
//         * Method********************************* * To resources like minio or
//         * DB
//         * *************************************************************************
//         */
//        /**
//         * ******
//         * Step 1 Create Buffered Image by Reading from Url using ImageIO
//         * library ******
//         */
//        BufferedImage image = ImageIO.read(imageUrl);
//        /**
//         * ******
//         * Step 2 Create ByteArrayOutputStream object to handle Image data
//         * ******
//         */
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        /**
//         * ******
//         * Step 3 Write as Image with Jpeg extension to byteArrayOutputStream
//         * created in previous step ******
//         */
//        ImageIO.write(image, "jpg", byteArrayOutputStream);
//        /**
//         * ******
//         * Step 4 Flush image created to byteArrayOutoputStream ******
//         */
//        byteArrayOutputStream.flush();
//        /**
//         * ******
//         * Step 5 Create Random file name but unique by adding timestamp with
//         * extension ******
//         */
//        String fileName = new Date().getTime() + ".jpg";
//        /**
//         * ******
//         * Step 6 Now Create MultipartFile using MockMultipartFile by providing
//         *
//         * @param fileName name of the file
//         * @param imageType like "image/jpg"
//         * @param ByteArray from ByteArrayOutputStream ******
//         */
//        //FileItem fileItem = new DiskFileItem("fileData", "image/jpeg", true, fileName, 100000000, new java.io.File(System.getProperty("java.io.tmpdir")));
//        //MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
//        //MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, "image/jpeg", byteArrayOutputStream.toByteArray());
//        byteArrayOutputStream.close(); // Close once it is done saving
//
//        //Upload image
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        headers.add("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZDYwOWE0ZC0zYWZmLTRlZWYtYTUzYy05ZmE3NmQ2NjlhMDMiLCJpYXQiOjE2MjE3MDM0MzgsImV4cCI6MTYyMjMwODIzOH0.Toi8Fvt1oxP_biio7-pWCTRUok3kjB2RnCRj_k4WwXHHqu8HkLq7cfglvlU9sB_jR4todpW3YA7mLo-whPbLKA");
//        //
//        Path path = Paths.get("D:\\test.jpg");
//        byte[] content = null;
//        try {
//            content = Files.readAllBytes(path);
//        } catch (IOException e) {
//        }
//        //MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", content);
//        LinkedMultiValueMap<String, String> imgHeaderMap = new LinkedMultiValueMap<>();
//        imgHeaderMap.add("Content-disposition", "form-data; name=file; filename=test.jpg");
//        imgHeaderMap.add("Content-type", "image/jpeg");
//        //HttpEntity<byte[]> img = new HttpEntity<>(content, imgHeaderMap);
//        HttpEntity<byte[]> img = new HttpEntity<>(byteArrayOutputStream.toByteArray(), imgHeaderMap);
//        LinkedMultiValueMap<String, Object> multipartReqMap = new LinkedMultiValueMap<>();
//        multipartReqMap.add("file", img);
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartReqMap, headers);
//        String serverUrl = "https://openapi.zalo.me/v2.0/oa/upload/image?access_token=ttblL8MTmH3VVcTKX9xB5jPmOLQ4YPDAkJrqTB2Rad3b76bouutEQVKVPr_2yPPwoMzzTDBRaqpoPNfXqkgJOl9OLL2ZoOjGa5aXJAZHna6EJJ1peEpQAR9ODXALxOGesdz40FRQbn7EOa40wTY40Sy-R22KfQmMe1zi092mYaAdAanWZx2UGjepLmoAdRyDgtqHAV7D_ZpUMI0WxEdm1TPVNaUC-QzvXsPIVP_Gc5BEJbSK_U-RLgzrDcVzn9jHwcaqVht8o7cgA00xlg-cB-v8QIpb-QmVUaPYrvztZe335m";
//        //String serverUrl = "http://103.21.151.158/v1.0/upload/vehicle/image";
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.postForObject(serverUrl, requestEntity, String.class);
//        if (response != null) {
//            System.out.println(response);
//        }
//    }
//    public static void main(String[] args){
//        String str = "rtmp://10.0.01:808/live/axis2_link?encoder=1&live=88x92";
//        String abc = str.split("\\?")[0];
//        System.out.println("abc: " + abc);
//        String unicodeStr = "Phát hiện vi phạm";
//        String escapeStr = StringEscapeUtils.escapeJava(unicodeStr);
//        System.out.println("escape: " + escapeStr);
//    }
//    public static void main(String[] args) {
//        String str = "{\"recipient\":{\"user_id\":\"USER_ID\"},\"message\":{\"text\":\"MESSAGE\",\"attachment\":{\"type\":\"template\",\"payload\":{\"template_type\":\"media\",\"elements\":[{\"media_type\":\"image\",\"attachment_id\":\"ATTACHMENT_ID\"}],\"buttons\":[{\"title\":\"URL_TITLE\",\"payload\":{\"url\":\"URL_LINK\"},\"type\":\"oa.open.url\"}]}}}}";
//        int firstIndex = str.indexOf(",\"buttons\"");
//        int lastIndex = firstIndex > 0 ? str.indexOf("]", firstIndex) : 0;
//        if(firstIndex > 0 && lastIndex > 0) {
//            str = str.substring(0, firstIndex) + str.substring(lastIndex + 1);
//        }
//        System.out.println(str);
//    }
}

