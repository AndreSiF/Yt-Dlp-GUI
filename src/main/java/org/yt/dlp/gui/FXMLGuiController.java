package org.yt.dlp.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.yt.dlp.downloader.YtDlpDownloaderTask;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.util.prefs.Preferences;

public class FXMLGuiController implements Initializable {

    private Preferences preferences = Preferences.userNodeForPackage(FXMLGuiController.class);
    private static final String SAVE_PATH_KEY = "savePath";

    @FXML
    ProgressBar downloadProgressBar;

    @FXML
    public Button downloadButton;

    @FXML
    private Label status;

    @FXML
    private ChoiceBox<String> videoFileType;

    @FXML
    private TextField videoUrlTextfield;

    @FXML
    private Label selectedPathLabel;

    private File downloadFolder;

    @FXML
    public void buttonActionHandler(ActionEvent event) throws IOException {

        String downloadUrl = videoUrlTextfield.getText();
        String downloadType = videoFileType.getValue();

        videoUrlTextfield.setText("");

        YtDlpDownloaderTask downloadTask = new YtDlpDownloaderTask(downloadUrl, downloadType, downloadFolder);

        downloadProgressBar.progressProperty().bind(downloadTask.progressProperty());
        status.textProperty().bind(downloadTask.messageProperty());

        downloadTask.setOnSucceeded(e -> {
            downloadProgressBar.progressProperty().unbind();
            downloadProgressBar.setProgress(1.0);
            status.textProperty().unbind();
            status.setText("Done!");
        });

        downloadTask.setOnFailed(e -> {
            downloadProgressBar.progressProperty().unbind();
            downloadProgressBar.setProgress(0);
            status.textProperty().unbind();
            status.setText("Error while downloading");
        });

        new Thread(downloadTask).start();
    }

    @FXML
    public void chooseDirectory(ActionEvent actionEvent) {
        DirectoryChooser newDownloadFolder = new DirectoryChooser();
        newDownloadFolder.setTitle("Select a folder to save the videos");

        if (downloadFolder != null) {
            newDownloadFolder.setInitialDirectory(downloadFolder);
        }

        Stage stage = (Stage) selectedPathLabel.getScene().getWindow();
        File directory = newDownloadFolder.showDialog(stage);

        if (directory != null) {
            downloadFolder = directory;
            selectedPathLabel.setText(directory.getAbsolutePath());
            saveDirectory(directory);
        }
    }

    private void saveDirectory(File directory) {
        preferences.put(SAVE_PATH_KEY, directory.getAbsolutePath());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> videoTypes = FXCollections.observableArrayList(
                 "mp4",
                    "mov",
                    "mp3"
        );

        videoFileType.setItems(videoTypes);
        videoFileType.setValue("mp4");

        String savedPath = preferences.get(SAVE_PATH_KEY, null);
        if (savedPath != null) {
            downloadFolder = new File(savedPath);
            selectedPathLabel.setText(savedPath);
        }
    }
}
