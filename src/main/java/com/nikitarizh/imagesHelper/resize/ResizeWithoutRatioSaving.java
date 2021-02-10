package com.nikitarizh.imagesHelper.resize;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.nikitarizh.imagesHelper.App;
import com.nikitarizh.imagesHelper.helpers.ImageHelper;

public class ResizeWithoutRatioSaving implements Resizer {

    @Override
    public BufferedImage resize(BufferedImage image, int newWidth, int newHeight, int hints) {
        App.logger.debug("Resizing image " + image.toString() + " without saving aspect ratio");

        App.logger.debug("Getting scaled instance");
        Image scaled = image.getScaledInstance(newWidth, newHeight, hints);

        App.logger.debug("Converting scaled instance to BufferedImage");
        BufferedImage bufferedImage = ImageHelper.toBufferedImage(scaled);

        App.logger.info("Successfully resized image " + image.toString());

        return bufferedImage;
    }
    
}
