package com.nikitarizh.imagesHelper.helpers;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import com.nikitarizh.imagesHelper.App;

public class FileHelper {

    private static FileHelper instance;

    private FileHelper() {}

    private static void checkInstance() {
        if (instance == null) {
            instance = new FileHelper();
        }
    }

    public static File getFile(String URL) {
        if (URL == null || URL.trim().equals("")) {
            App.logger.error("URL " + URL + " is incorrect");
            return null;
        }

        checkInstance();

        if (URL.charAt(0) != '/') {
            URL = "/" + URL;
        }

        App.logger.debug("Trying to get file " + URL);

        try {
            URI URI = FileHelper.class.getResource(URL).toURI();

            File file = new File(URI);

            if (file.exists()) {
                App.logger.info("Returning file " + URL);
                return file;
            }
            else {
                throw new FileNotFoundException();
            }
        }
        catch (NullPointerException | FileNotFoundException e) {
            App.logger.error("File " + URL + " does not exist");
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            App.logger.error("Exception in URI syntax");
            e.printStackTrace();
        }

        return null;
    }

    public static Image getImage(String URL) {
        checkInstance();

        App.logger.debug("Trying to get image " + URL);

        try {
            File imageFile = FileHelper.getFile(URL);
            if (imageFile != null) {
                Image image = ImageIO.read(imageFile);
                App.logger.info("Returning image " + URL);
                return image;
            }
        }
        catch (IOException e) {
            App.logger.error("An error occured during reading image " + URL + " or unable to create stream");
            e.printStackTrace();
        }

        return null;
    }
}
