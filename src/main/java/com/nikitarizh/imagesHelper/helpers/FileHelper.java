package com.nikitarizh.imagesHelper.helpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.nikitarizh.imagesHelper.App;

public class FileHelper {

    public static File getFile(String URL) {
        if (URL == null || URL.trim().equals("")) {
            App.logger.error("URL " + URL + " is incorrect");
            return null;
        }

        App.logger.debug("Trying to get file " + URL);

        try {
            File file = new File(URL);

            if (file.exists()) {
                App.logger.info("Returning file " + URL);
                return file;
            }
            else {
                throw new FileNotFoundException();
            }
        }
        catch (NullPointerException e) {
            App.logger.error("File URI can't be null");
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            App.logger.error("File " + URL + " does not exist");
            e.printStackTrace();
        }

        return null;
    }

    public static BufferedImage getImage(String URL) {
        App.logger.debug("Trying to get image " + URL);

        try {
            File imageFile = getFile(URL);
            BufferedImage image = ImageIO.read(imageFile);
            App.logger.info("Returning image " + URL);
            return image;
        }
        catch (IllegalArgumentException e) {
            App.logger.error("Image file can't be null");
            e.printStackTrace();
        }
        catch (IOException e) {
            App.logger.error("An error occured during reading image " + URL + " or unable to create stream");
            e.printStackTrace();
        }

        return null;
    }

    public static void writeImage(String URL, BufferedImage image) {
        App.logger.debug("Preparing for writing image " + URL);

        File file = createFile(URL);

        String[] parts = URL.split("[.]");

        try {
            App.logger.debug("Trying to write image " + image.toString() + " to file " + URL);
            ImageIO.write(image, parts[parts.length - 1], file);
            App.logger.info("Successfully wrote image " + image.toString() + " to file " + URL);
        }
        catch (IllegalArgumentException e) {
            App.logger.error("Parameters of writing can't be null");
            e.printStackTrace();
        }
        catch (IOException e) {
            App.logger.error("An error occured during writing image " + URL + " or unable to create stream");
            e.printStackTrace();
        }
    }

    private static File createFile(String URL) {
        App.logger.debug("Trying to create file " + URL);

        File file;

        try {
            App.logger.debug("Trying to create instance of file " + URL);
            file = new File(URL);
            App.logger.debug("Created instance of file " + URL);
        }
        catch (NullPointerException e) {
            App.logger.error("File URI can't be null");
            e.printStackTrace();
            return null;
        }

        boolean created;
        try {
            created = file.createNewFile();

            if (!created) {
                App.logger.warn("File " + URL + " already exists");
            }
            else {
                App.logger.info("File " + URL + " created");
            }
        }
        catch (IOException e) {
            App.logger.error("I/O error occured during creating file " + URL);
            e.printStackTrace();
            return null;
        }
        catch (SecurityException e) {
            App.logger.error("Security error occured during creating file " + URL);
            e.printStackTrace();
            return null;
        }
        
        return file;
    }
}
