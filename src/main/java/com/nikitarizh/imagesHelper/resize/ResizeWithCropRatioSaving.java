package com.nikitarizh.imagesHelper.resize;

import java.awt.image.BufferedImage;

import com.nikitarizh.imagesHelper.App;
import com.nikitarizh.imagesHelper.helpers.ImageHelper;

public class ResizeWithCropRatioSaving implements Resizer {

    @Override
    public BufferedImage resize(BufferedImage image, int newWidth, int newHeight, int hints) {

        final int initialWidth = image.getWidth();
        final int initialHeight = image.getHeight();

        App.logger.debug("Resizing image " + image + " with ratio saving (crop)");

		double widthRatio = (double) newWidth / initialWidth;
        double heightRatio = (double) newHeight / initialHeight;

        BufferedImage resizedImage;
        double quotient = Math.max(widthRatio, heightRatio);

        App.logger.debug("Resizing core image");
        resizedImage = new ResizeWithoutRatioSaving().resize(image, (int) Math.floor(initialWidth * quotient), 
                                                            (int) Math.floor(initialHeight * quotient), BufferedImage.TYPE_INT_RGB);

        App.logger.debug("Cropping image");
        resizedImage = ImageHelper.crop(resizedImage, newWidth, newHeight);
        
        App.logger.debug("Successfully resized image " + image);
        return resizedImage;
    }

}