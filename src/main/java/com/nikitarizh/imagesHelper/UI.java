package com.nikitarizh.imagesHelper;

import java.awt.Image;
import java.awt.image.BufferedImage;
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
        System.out.println("\nSpecify initial filename");
        String filename = in.nextLine();

        // trying to get image with input filename
        BufferedImage image = FileHelper.getImage(filename);
        if (image == null) {
            App.logger.error("Image is not loaded");
            System.exit(1);
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

        // new name
        // if old name was image.jpg|png, new name will be image-processed.jpg
        String newName = filename.replaceAll("([.][j][p][g])|([.][p][n][g])", "") + "-processed";

        // resizing image
        BufferedImage resizedImage;
        if (newHeight == -1) {
            resizedImage = ImageHelper.resizeImage(image, newWidth, Image.SCALE_SMOOTH, new ResizeWithBlurRatioSaving());
        }
        else {
            resizedImage = ImageHelper.resizeImage(image, newWidth, newHeight, Image.SCALE_SMOOTH, new ResizeWithBlurRatioSaving());
        }

        // writing resized image
        FileHelper.writeImage(newName, "jpg", resizedImage);

        // closing scanner
        in.close();
    }
}
