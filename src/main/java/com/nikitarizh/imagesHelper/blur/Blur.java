package com.nikitarizh.imagesHelper.blur;

import java.awt.image.BufferedImage;

public class Blur implements Blurrer {

    private Blurrer blurAlgorithm;

    public Blur(Blurrer blurrer) {
        this.blurAlgorithm = blurrer;
    }

    @Override
    public BufferedImage blur(BufferedImage image, int radius) {
        return blurAlgorithm.blur(image, radius);
    }
}
