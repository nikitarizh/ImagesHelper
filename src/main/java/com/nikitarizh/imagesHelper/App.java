package com.nikitarizh.imagesHelper;

import java.awt.Image;

import com.nikitarizh.imagesHelper.helpers.FileHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    public static Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) {

        Image image = FileHelper.getImage("1.jpg");

        if (image == null) {
            logger.error("Image is not loaded");
            System.exit(1);
        }

        image = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        
        
    }
}
