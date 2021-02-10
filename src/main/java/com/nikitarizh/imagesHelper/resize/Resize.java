package com.nikitarizh.imagesHelper.resize;

import java.awt.image.BufferedImage;

public class Resize implements Resizer {

    private Resizer resizeAlgorithm;

    public Resize(Resizer resizer) {
        this.resizeAlgorithm = resizer;
    }

    @Override
    public BufferedImage resize(BufferedImage image, int newWidth, int newHeight, int hints) {
        return resizeAlgorithm.resize(image, newWidth, newHeight, hints);
    }
    
}