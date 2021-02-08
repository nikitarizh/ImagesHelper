package com.nikitarizh.imagesHelper.helpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.nikitarizh.imagesHelper.App;

public class FileHelper {

    /**
     * Returns file that lies in specified URL
     * @param URL
     * @return {@link File} or null if URL is null or empty
     */
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

    /**
     * Returns image (as {@link BufferedImage}) that lies in specified URL
     * @param URL
     * @return {@link BufferedImage} or null if URL is null or error occured
     */
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

    /**
     * Writes image to file
     * @param filename
     * @param format
     * @param image
     */
    public static void writeImage(String filename, String format, BufferedImage image) {
        String fullFileName = filename + "." + format;

        App.logger.debug("Preparing for writing image " + fullFileName);

        File file = createFile(fullFileName);

        try {
            App.logger.debug("Trying to write image " + image.toString() + " to file " + fullFileName);

            File compressedImageFile = file;
            OutputStream os = new FileOutputStream(compressedImageFile);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
            ImageWriter writer = (ImageWriter) writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(1.0f);
            writer.write(null, new IIOImage(image, null, null), param);

            os.close();
            ios.close();
            writer.dispose();

            App.logger.info("Successfully wrote image " + image.toString() + " to file " + fullFileName);
        }
        catch (IllegalArgumentException e) {
            App.logger.error("Parameters of writing can't be null");
            e.printStackTrace();
        }
        catch (IOException e) {
            App.logger.error("An error occured during writing image " + fullFileName + " or unable to create stream");
            e.printStackTrace();
        }
    }

    /**
     * Writes image to file
     * @param URL
     * @param image
     */
    public static void writeImage(String URL, BufferedImage image) {
        String[] parts = URL.split("[.]");

        if (parts.length < 2) {
            App.logger.error("Specify format");
            return;
        }

        writeImage(parts[0], parts[parts.length - 1], image);
    }

    /**
     * Creates file in specified location
     * @param URL
     * @return {@link File} or null if error occured
     */
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
