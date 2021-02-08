package com.nikitarizh.imagesHelper.helpers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.nikitarizh.imagesHelper.App;

public class ImageHelper {
    
    public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight, int hints) {
        App.logger.debug("Trying to resize image " + image.toString());

        App.logger.debug("Getting scaled instance");
        Image scaled = image.getScaledInstance(newWidth, newHeight, hints);

        App.logger.debug("Converting scaled instance to BufferedImage");
        BufferedImage bufferedImage = toBufferedImage(scaled);

        App.logger.info("Successfully resized image " + image.toString());

        return bufferedImage;
    }

    public static BufferedImage resizeImage(BufferedImage image, int newWidth, int hints) {
        int newHeight = newWidth  * image.getHeight() / image.getWidth();
        
        return resizeImage(image, newWidth, newHeight, hints);
    }

    public static BufferedImage toBufferedImage(Image img) {
        if (img == null) {
            App.logger.error("Image can't be null");
            return null;
        }

        App.logger.debug("Trying to convert Image" + img.toString() + " to BufferedImage");
        
        if (img instanceof BufferedImage) {
            App.logger.warn("Image " + img.toString() + " is already a BufferedImage");
            return (BufferedImage) img;
        }

        App.logger.debug("Creating new buffered image");
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        App.logger.debug("Creating graphics");
        Graphics2D bufferedGraphics2d = bufferedImage.createGraphics();
        App.logger.debug("Drawing image");
        bufferedGraphics2d.drawImage(img, 0, 0, null);
        App.logger.debug("Disposing");
        bufferedGraphics2d.dispose();

        App.logger.info("Successfully converted " + img.toString() + " to BufferedImage");

        return bufferedImage;
    }
}
