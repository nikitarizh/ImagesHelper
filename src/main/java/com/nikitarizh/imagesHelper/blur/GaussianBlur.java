package com.nikitarizh.imagesHelper.blur;

import java.awt.image.BufferedImage;

import com.nikitarizh.imagesHelper.App;
import com.nikitarizh.imagesHelper.helpers.ImageHelper;

public class GaussianBlur implements Blurrer {

    @Override
    public BufferedImage blur(BufferedImage image, int radius) {

        App.logger.debug("Preparing for Gaussian blur");
        
        int[][] pixels = ImageHelper.imageToMatrix(image);
        int[][] output = new int[pixels.length][pixels[0].length];

        App.logger.debug("Starting Gaussian blur on image " + image + " with radius " + radius);
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                output[i][j] = calculateSum(pixels, i, j, radius);
            }
        }

        App.logger.debug("Successfully blurred image " + image.toString() + " with Gaussian blur with radius " + radius);
        return ImageHelper.matrixToImage(output);
    }

    private int calculateSum(int[][] pixels, int y, int x, int radius) {
        int rSum = 0;
        int gSum = 0;
        int bSum = 0;

        int startI = Math.max(0, y - radius);
        int startJ = Math.max(0, x - radius);
        int endI = Math.min(pixels.length, y + radius + 1);
        int endJ = Math.min(pixels[0].length, x + radius + 1);
        int numberOfSurrounding = (endI - startI) * (endJ - startJ);
        int rgb;

        for (int i = startI; i < endI; i++) {
            for (int j = startJ; j < endJ; j++) {
                rgb = pixels[i][j];
                rSum += (rgb >>> 16) & 0xFF;
                gSum += (rgb >>> 8) & 0xFF;
                bSum += rgb & 0xFF;
            }
        }

        rSum = (int) Math.round((double) rSum / numberOfSurrounding);
        gSum = (int) Math.round((double) gSum / numberOfSurrounding);
        bSum = (int) Math.round((double) bSum / numberOfSurrounding);

        int finalRgb = (rSum << 16) | (gSum << 8) | bSum | 0xFF000000;

        return finalRgb;
    }
}
