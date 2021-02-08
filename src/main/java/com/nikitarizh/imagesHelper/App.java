package com.nikitarizh.imagesHelper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Scanner;

import com.nikitarizh.imagesHelper.helpers.FileHelper;
import com.nikitarizh.imagesHelper.helpers.ImageHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    public static Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) {

        final Scanner in = new Scanner(System.in);

        System.out.println("\nSpecify initial filename");
        String filename = in.nextLine();

        BufferedImage image = FileHelper.getImage(filename);
        if (image == null) {
            logger.error("Image is not loaded");
            System.exit(1);
        }

        System.out.println("\n\nSpecify new width");
        int newWidth = in.nextInt();

        String newName = filename.replaceAll("([.][j][p][g])|([.][p][n][g])", "") + "-processed";

        FileHelper.writeImage(newName, "jpg", ImageHelper.resizeImage(image, newWidth, Image.SCALE_SMOOTH));

        in.close();
    }
}
