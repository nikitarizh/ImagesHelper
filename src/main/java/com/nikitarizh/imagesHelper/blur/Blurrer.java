package com.nikitarizh.imagesHelper.blur;

import java.awt.image.BufferedImage;

public interface Blurrer {

    /**
     * Blurs {@code image}
     * @param image
     * @param radius
     * @return blurred image
     */
    public BufferedImage blur(BufferedImage image, int radius);
}