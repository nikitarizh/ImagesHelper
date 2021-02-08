package com.nikitarizh.imagesHelper;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.nikitarizh.imagesHelper.helpers.FileHelper;
import com.nikitarizh.imagesHelper.helpers.ImageHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    public static Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) {

        BufferedImage image = FileHelper.getImage("1.jpg");

        if (image == null) {
            logger.error("Image is not loaded");
            System.exit(1);
        }

        FileHelper.writeImage("2.png", ImageHelper.resizeImage(image, 500, Image.SCALE_SMOOTH));
        // FileHelper.writeImage("2.jpg", image);
    }
}
