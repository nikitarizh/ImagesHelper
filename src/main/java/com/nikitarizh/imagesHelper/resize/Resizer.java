package com.nikitarizh.imagesHelper.resize;

import java.awt.image.BufferedImage;

public interface Resizer {
    /**
     * Resizes image to {@code newWidth} by {@code newHeight}
     * @param image
     * @param newWidth
     * @param newHeight
     * @param hints
     * @return resized image
     */
    public BufferedImage resize(BufferedImage image, int newWidth, int newHeight, int hints);
}
