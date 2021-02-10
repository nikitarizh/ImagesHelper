package com.nikitarizh.imagesHelper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Scanner;

import com.nikitarizh.imagesHelper.blur.Blur;
import com.nikitarizh.imagesHelper.blur.GaussianBlur;
import com.nikitarizh.imagesHelper.helpers.FileHelper;
import com.nikitarizh.imagesHelper.helpers.ImageHelper;
import com.nikitarizh.imagesHelper.resize.ResizeWithBlurRatioSaving;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    public static Logger logger = LogManager.getRootLogger();

    private static final boolean prod = true;

    public static void main(String[] args) {
        if (prod) {
            UI.start();
            return;
        }
    }
}
