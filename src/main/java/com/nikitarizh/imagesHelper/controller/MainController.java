package com.nikitarizh.imagesHelper.controller;

import java.io.File;
import java.util.List;
import com.nikitarizh.imagesHelper.tasks.ProcessImagesTask;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {
    
    @FXML
    private Button chooseFileButton;
    @FXML
    private Button newFolderButton;
    @FXML
    private Button processButton;
    @FXML
    private Label filenameLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label progressLabel;
    @FXML
    private Label newFolderLabel;
    @FXML
    private TextField newWidthInput;
    @FXML
    private TextField newHeightInput;

    private List<File> files;
    private File newDirectory;

    @FXML
    public void chooseFileButtonPressed() {
        FileChooser fc = new FileChooser();

        files = fc.showOpenMultipleDialog(new Stage());

        if (files != null) {
            if (files.size() % 10 == 1) {
                filenameLabel.setText(files.size() + " file");
            }
            else {
                filenameLabel.setText(files.size() + " files");
            }
        }
    }

    @FXML
    public void newFolderButtonPressed() {
        DirectoryChooser dc = new DirectoryChooser();

        newDirectory = dc.showDialog(new Stage());

        if (newDirectory != null) {
            newFolderLabel.setText(newDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void processButtonPressed() {
        if (files == null || files.size() == 0) {
            new Alert(AlertType.ERROR, "Choose files to process", ButtonType.OK).showAndWait();
            return;
        }

        if (newDirectory == null) {
            new Alert(AlertType.ERROR, "Specify new saving directory", ButtonType.OK).showAndWait();
            return;
        }

        int newWidth;
        int newHeight;

        try {
            newWidth = Integer.parseInt(newWidthInput.getText());
        }
        catch (NumberFormatException e) {
            new Alert(AlertType.ERROR, "Enter correct width value", ButtonType.OK).showAndWait();
            return;
        }

        try {
            newHeight = Integer.parseInt(newHeightInput.getText());
        }
        catch (NumberFormatException e) {
            new Alert(AlertType.WARNING, "Height will be auto adjusted", ButtonType.OK).showAndWait();
            newHeight = -1;
        }

        Task<Void> task = new ProcessImagesTask(files, newDirectory, newWidth, newHeight, this);

        progressBar.progressProperty().bind(task.progressProperty());

        new Thread(task).start();
    }

    public void updateProgressLabel(String newText) {
        Platform.runLater(() -> {
            progressLabel.setText(newText);
        });
    }
}
