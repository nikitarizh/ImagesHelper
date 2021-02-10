package com.nikitarizh.imagesHelper.resize;

import java.awt.image.BufferedImage;

import com.nikitarizh.imagesHelper.App;
import com.nikitarizh.imagesHelper.blur.Blurrer;
import com.nikitarizh.imagesHelper.blur.GaussianBlur;
import com.nikitarizh.imagesHelper.helpers.ImageHelper;

public class ResizeWithBlurRatioSaving implements Resizer {

    @Override
    public BufferedImage resize(BufferedImage image, int newWidth, int newHeight, int hints) {

        final int offset = 10;
        final Blurrer blurAlgorithm = new GaussianBlur();
        final int blurRadius = 10;
        final int initialWidth = image.getWidth();
        final int initialHeight = image.getHeight();

        App.logger.debug("Resizing image " + image + " with ratio saving (blur background)");

        double widthRatio = (double) newWidth / initialWidth;
        double heightRatio = (double) newHeight / initialHeight;

        BufferedImage resizedImage;
        double quotient = Math.min(widthRatio, heightRatio);

        App.logger.debug("Resizing core image");
        resizedImage = new ResizeWithoutRatioSaving().resize(blurAlgorithm.blur(image, 2), (int) Math.floor(initialWidth * quotient), 
                                                            (int) Math.floor(initialHeight * quotient), BufferedImage.TYPE_INT_RGB);

        int widthDelta = Math.abs(resizedImage.getWidth() - newWidth);
        int heightDelta = Math.abs(resizedImage.getHeight() - newHeight);

        App.logger.debug("Drawing background");
        BufferedImage background = getBackground(image, newWidth, newHeight, offset, blurAlgorithm, blurRadius);

        App.logger.debug("Positioning core image on background");
        resizedImage = drawImage(background, resizedImage, widthDelta / 2, heightDelta / 2);

        App.logger.debug("Successfully resized image " + image);
        return resizedImage;
    }

    /**
     * Draws blurred background
     * @param image
     * @param width
     * @param height
     * @param offset
     * @param blurAlgorithm
     * @param blurRadius
     * @return background
     */
    private BufferedImage getBackground(BufferedImage image, int width, int height, int offset, Blurrer blurAlgorithm, int blurRadius) {

        BufferedImage background = ImageHelper.crop(image, offset);
        background = new ResizeWithCropRatioSaving().resize(background, width, height, BufferedImage.TYPE_INT_RGB);

        return blurAlgorithm.blur(background, blurRadius);
    }
    
    /**
     * Draws {@code image} on {@code background} with specified {@code xOffset} and {@code yOffset}
     * @param background
     * @param image
     * @param xOffset
     * @param yOffset
     * @return image combined with background
     */
    private BufferedImage drawImage(BufferedImage background, BufferedImage image, int xOffset, int yOffset) {
        BufferedImage output = new BufferedImage(background.getColorModel(), background.copyData(null), false, null);

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                output.setRGB(j + xOffset, i + yOffset, image.getRGB(j, i));
            }
        }

        return output;
    }
}
