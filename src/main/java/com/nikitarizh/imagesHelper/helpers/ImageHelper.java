package com.nikitarizh.imagesHelper.helpers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.nikitarizh.imagesHelper.App;
import com.nikitarizh.imagesHelper.resize.*;

public class ImageHelper {
    
    /**
     * Resized image to {@code newWidth} by {@code newHeight}
     * @param image
     * @param newWidth
     * @param newHeight
     * @param hints
     * @return resized {@link BufferedImage}
     */
    public static BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight, int hints, Resizer resizeStrategy) {
        App.logger.debug("Preparing for image resizing");
        App.logger.debug("Checking aspect ratio");

        double oldRatio = (double) image.getWidth() / image.getHeight();
        double newRatio = (double) newWidth / newHeight;

        if (Math.abs(oldRatio - newRatio) > 0.0001) {
            App.logger.debug("New aspect ratio is different, calling resize with blur ratio saving");

            return resizeStrategy.resize(image, newWidth, newHeight, hints);
        }

        return new Resize(new ResizeWithoutRatioSaving()).resize(image, newWidth, newHeight, hints);
    }

    /**
     * Resized image to {@code newWidth} width aspect ratio saving
     * @param image
     * @param newWidth
     * @param hints
     * @return resized {@link BufferedImage}
     */
    public static BufferedImage resizeImage(BufferedImage image, int newWidth, int hints, Resizer resizeStrategy) {
        int newHeight = newWidth  * image.getHeight() / image.getWidth();
        
        return resizeImage(image, newWidth, newHeight, hints, resizeStrategy);
    }

    /**
     * Crops {@image}
     * @param image
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return cropped image
     */
    public static BufferedImage crop(BufferedImage image, int left, int top, int right, int bottom) {
        App.logger.debug("Trying to crop image " + image + " left: " + left + " top: " + top + " right: " + right + " bottom: " + bottom);
        BufferedImage output = new BufferedImage(image.getWidth() - left - right, image.getHeight() - top - bottom, BufferedImage.TYPE_INT_RGB);
        
        Graphics2D graphics = (Graphics2D) output.getGraphics();
        graphics.drawImage(image, -left, -top, image.getWidth() - right, image.getHeight() - bottom, null);

        graphics.dispose();

        App.logger.info("Successfully cropped image " + image);
        return output;
    }

    /**
     * Crops {@image} {@code offset} pixels from each side
     * @param image
     * @param offset
     * @return cropped image
     */
    public static BufferedImage crop(BufferedImage image, int offset) {
        return crop(image, offset, offset, offset, offset);
    }

    /**
     * Crops {@image} to have width {@code newWidth} and height {@code newHeight}
     * @param image
     * @param newWidth
     * @param newHeight
     * @return cropped image
     */
    public static BufferedImage crop(BufferedImage image, int newWidth, int newHeight) {
        int horizontal = (int) Math.abs((newWidth - image.getWidth()) / 2);

        int vertical = (int) Math.abs((newHeight - image.getHeight()) / 2);

        return crop(image, horizontal, vertical, horizontal, vertical);
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

    /**
     * Converts {@image} to int[][] - array of pixels
     * @param image
     * @return int[][]
     */
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

    /**
     * Converts int[][] - array of pixels to {@code BufferedImage}
     * @param matrix
     * @return image
     */
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
