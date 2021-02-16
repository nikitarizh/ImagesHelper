package com.nikitarizh.imagesHelper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.nikitarizh.imagesHelper.helpers.FileHelper;
import com.nikitarizh.imagesHelper.helpers.ImageHelper;
import com.nikitarizh.imagesHelper.resize.ResizeWithBlurRatioSaving;

public class UI {
    
    public static void start() {
        // instantiating scanner
        final Scanner in = new Scanner(System.in);

        // filename input
        System.out.println("\nSpecify initial filename or directory with images");
        String filename = in.nextLine();
        File file = FileHelper.getFile(filename);

        ArrayList<File> files = new ArrayList<>();
        if (file.isDirectory()) {
            for (final File entry : file.listFiles()) {
                if (!entry.isDirectory()) {
                    files.add(entry);
                }
            }
        }
        else {
            files.add(file);
        }

        ArrayList<BufferedImage> imageList = new ArrayList<>();
        ArrayList<File> errorFiles = new ArrayList<>();

        for (final File entry : files) {
            // trying to get image with input filename
            BufferedImage image = FileHelper.getImage(entry);
            if (image == null) {
                App.logger.error("Image is not loaded");
                errorFiles.add(entry);
                imageList.add(null);
            }
            else {
                imageList.add(image);
            }
        }

        // new width input
        System.out.println("\n\nSpecify new width");
        int newWidth = in.nextInt();

        // new height input
        System.out.println("\n\nSpecify new height or print any letter to skip");
        int newHeight = -1;
        try {
            newHeight = in.nextInt();
        }
        catch (InputMismatchException e) {
            System.out.println("Height will be auto abjusted");
        }

        int index = -1;
        for (BufferedImage entry : imageList) {
            index++;
            if (entry == null) {
                continue;
            }

            App.logger.debug("***********************");
            App.logger.debug("Processing image " + entry);
            App.logger.debug("***********************");
            // new name
            // if old name was image.jpg|png, new name will be image-processed.jpg
            String newName = files.get(index).getName().replaceAll("([.][j][p][g])|([.][p][n][g])", "") + "-processed";

            // resizing image
            BufferedImage resizedImage;
            if (newHeight == -1) {
                resizedImage = ImageHelper.resizeImage(entry, newWidth, Image.SCALE_SMOOTH, new ResizeWithBlurRatioSaving());
            }
            else {
                resizedImage = ImageHelper.resizeImage(entry, newWidth, newHeight, Image.SCALE_SMOOTH, new ResizeWithBlurRatioSaving());
            }

            if (resizedImage != null) {
                // writing resized image
                FileHelper.writeImage(newName, "jpg", resizedImage);
                App.logger.info("Successfully processed image " + entry);
            }
            else {
                errorFiles.add(files.get(index));
            }
        }

        int success = files.size() - errorFiles.size();

        if (success > 0) {
            App.logger.info("Successfully processed " + success + " images");
        }
        
        if (errorFiles.size() > 0) {
            App.logger.error("Aborted " + errorFiles.size() + " images");
            for (File f : errorFiles) {
                App.logger.error(f.getPath());
            }
        }

        // closing scanner
        in.close();
    }
}
