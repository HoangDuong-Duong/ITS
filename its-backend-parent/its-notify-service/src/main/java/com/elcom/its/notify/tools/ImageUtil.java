/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.tools;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author Admin
 */
public class ImageUtil {

    public static ByteArrayOutputStream resize(BufferedImage inputImage, String imageName, int scaledWidth, int scaledHeight) {
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            if (imageName != null && imageName.toLowerCase().endsWith(".png")) {
                ImageIO.write(outputImage, "png", byteArrayOutputStream);
            } else {
                ImageIO.write(outputImage, "jpg", byteArrayOutputStream);
            }
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            ex.printStackTrace();
        }
        return byteArrayOutputStream;
    }

    public static ByteArrayOutputStream resize(BufferedImage inputImage, String imageName, int newWidth) {
        int newHeight = (int) (inputImage.getHeight() * newWidth / inputImage.getWidth());
        System.out.println("newWidth: " + newWidth + ", origin width: " + inputImage.getWidth()
                + ", new height: " + newHeight);
        return resize(inputImage, imageName, newWidth, newHeight);
    }
}
