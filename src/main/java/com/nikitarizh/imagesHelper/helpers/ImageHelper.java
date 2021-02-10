package com.nikitarizh.imagesHelper.helpers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.nikitarizh.imagesHelper.App;

public class ImageHelper {
    
    /**
     * Resized image to {@code newWidth} by {@code newHeight}
     * @param image
     * @param newWidth
     * @param newHeight
     * @param hints
     * @return resized {@link BufferedImage}
     */
    public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight, int hints) {
        App.logger.debug("Trying to resize image " + image.toString());

        App.logger.debug("Getting scaled instance");
        Image scaled = image.getScaledInstance(newWidth, newHeight, hints);

        App.logger.debug("Converting scaled instance to BufferedImage");
        BufferedImage bufferedImage = toBufferedImage(scaled);

        App.logger.info("Successfully resized image " + image.toString());

        return bufferedImage;
    }

    /**
     * Resized image to {@code newWidth} width aspect ratio saving
     * @param image
     * @param newWidth
     * @param hints
     * @return resized {@link BufferedImage}
     */
    public static BufferedImage resizeImage(BufferedImage image, int newWidth, int hints) {
        int newHeight = newWidth  * image.getHeight() / image.getWidth();
        
        return resizeImage(image, newWidth, newHeight, hints);
    }

    /**
     * Converts {@link Image} to {@link BufferedImage}
     * @param image
     * @return {@link BufferedImage} or null of image is null
     */
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

    public static int[][] imageToMatrix(BufferedImage image) {
        App.logger.debug("Converting image to matrix");

        int width = image.getWidth();
        int height = image.getHeight();

        int[][] output = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                output[i][j] = (r << 16) + (g << 8) + b;
            }
        }

        App.logger.info("Successfully converted image to matrix");
        return output;
    }

    public static BufferedImage matrixToImage(int[][] matrix) {
        App.logger.debug("Converting matrix to image");

        BufferedImage bufferedImage = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                bufferedImage.setRGB(i, j, matrix[i][j]);
            }
        }

        App.logger.info("Successfully converted matrix to image");
        return bufferedImage;
    }
}
