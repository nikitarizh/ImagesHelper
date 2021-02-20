package com.nikitarizh.imagesHelper.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nikitarizh.imagesHelper.App;
import com.nikitarizh.imagesHelper.controller.MainController;
import com.nikitarizh.imagesHelper.helpers.FileHelper;
import com.nikitarizh.imagesHelper.helpers.ImageHelper;
import com.nikitarizh.imagesHelper.resize.ResizeWithBlurRatioSaving;

import java.awt.image.BufferedImage;
import java.awt.Image;

import javafx.concurrent.Task;

public class ProcessImagesTask extends Task<Void> {

    private final List<File> files;
    private final File savingDirectory;
    private final int newWidth;
    private final int newHeight;
    private final MainController controller;

    private final double filesToImagesMaxProgress = 50;
    private final double processImagesMaxProgress = 50;
    private final double totalMaxProgress = filesToImagesMaxProgress + processImagesMaxProgress;

    private double currentProgress = 0;

    public ProcessImagesTask(List<File> files, File savingDirectory, int newWidth, int newHeight, MainController controller) {
        this.files = files;
        this.savingDirectory = savingDirectory;
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.controller = controller;
    }

    @Override
    protected Void call() throws Exception {
        List<BufferedImage> imageList = new ArrayList<>();
        List<File> errorFiles = new ArrayList<>();
        int nullImages = 0;

        for (int i = 0; i < files.size(); i++) {
            final File entry = files.get(i);
            controller.updateProgressLabel("Getting image from file " + files.get(i).getName());

            // trying to get image with input filename
            BufferedImage image = FileHelper.getImage(entry);
            if (image == null) {
                errorFiles.add(entry);
                nullImages++;
                imageList.add(null);
            }
            else {
                imageList.add(image);
            }

            updateProgress(filesToImagesMaxProgress / files.size());
        }

        int index = -1;
        for (BufferedImage entry : imageList) {
            index++;

            if (entry == null) {
                continue;
            }

            controller.updateProgressLabel("Resizing image " + files.get(index).getName());
            
            // new name
            // if old name was image.jpg|png, new name will be image-processed.jpg
            String newName = savingDirectory.getAbsolutePath() + "/" + files.get(index).getName().replaceAll("([.][j][p][g])|([.][p][n][g])", "") + "-processed";

            // resizing image
            BufferedImage resizedImage;
            if (newHeight == -1) {
                resizedImage = ImageHelper.resizeImage(entry, newWidth, Image.SCALE_SMOOTH, new ResizeWithBlurRatioSaving());
            }
            else {
                resizedImage = ImageHelper.resizeImage(entry, newWidth, newHeight, Image.SCALE_SMOOTH, new ResizeWithBlurRatioSaving());
            }

            if (resizedImage != null) {
                // writing resized image
                FileHelper.writeImage(newName, "jpg", resizedImage);
                App.logger.info("Successfully processed image " + entry);
            }
            else {
                errorFiles.add(files.get(index));
            }

            updateProgress(processImagesMaxProgress / (imageList.size() - nullImages));
        }

        int success = files.size() - errorFiles.size();

        if (success > 0) {
            App.logger.info("Successfully processed " + success + " images");
        }
        
        if (errorFiles.size() > 0) {
            App.logger.error("Aborted " + errorFiles.size() + " images");
            for (File f : errorFiles) {
                App.logger.error(f.getPath());
            }
        }

        return null;
    }

    @Override
    protected void succeeded() {
        updateProgress(100);
        controller.updateProgressLabel("Successfully resized images");
    }

    private void updateProgress(double val) {
        currentProgress += val;
        updateProgress(currentProgress, totalMaxProgress);
    }
}
